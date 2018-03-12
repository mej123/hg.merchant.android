package com.zishan.sardinemerchant.fragment.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.goods.GoodsManagePresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.store.goods.GoodsManageView;

import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.store.GoodsDetailActivity;
import com.zishan.sardinemerchant.activity.store.StoreGoodsAddActivity;
import com.zishan.sardinemerchant.activity.store.StoreManageActivity;
import com.zishan.sardinemerchant.adapter.store.GoodsListAdapter;
import com.zishan.sardinemerchant.adapter.store.GoodsOptionsAdapter;
import com.zishan.sardinemerchant.fragment.BFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DensityUtil;


import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftasbase.eventbus.BaseEventManager;
import top.ftas.ftaswidget.recyclerview.adapter.CommonAdapter;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 商品管理 商品列表
 * Created by wislie on 2017/9/21.
 */

public class GoodsListFragment extends BFragment<GoodsManageView, GoodsManagePresenter>
        implements GoodsManageView {

    @BindView(R.id.goods_option_recycler_view)
    RecyclerView mOptionRecycler;
    @BindView(R.id.goods_option)
    TextView mGoodsOptionText;
    @BindView(R.id.goods_menu_icon)
    ImageView mMenuIcon;

    @BindView(R.id.goods_springview)
    SpringView mSpringView;
    @BindView(R.id.goods_recycler_view)
    RecyclerView mGoodsRecycler;

    @BindView(R.id.empty_layout)
    LinearLayout mEmptyLayout;

    //是否查询折扣
    private boolean is_discount;
    //是否查询推荐
    private boolean is_recommend;
    //分组id
    private long custom_group_id = -1;
    //菜单
    private ArrayList<ProductGroupEntity> mProductGroups = new ArrayList<>();

    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;

    private GoodsListAdapter mAdapter;
    //列表
    private List<ProductEntity> mProducts = new ArrayList<>();

    private List<String> mOptions = new ArrayList<String>(Arrays.asList("全部", "在售", "下架", "售空"));
    private GoodsOptionsAdapter mGoodsOptionsAdapter;
    private int mSelectedType = Constant.STORE_ALL; //默认情况下的筛选为 "全部"

    public GoodsListFragment() {
    }

    public static GoodsListFragment newInstance(boolean is_discount, boolean is_recommend,
                                                long custom_group_id) {

        GoodsListFragment fragment = new GoodsListFragment();
        Bundle data = new Bundle();
        data.putBoolean("is_discount", is_discount);
        data.putBoolean("is_recommend", is_recommend);
        data.putLong("custom_group_id", custom_group_id);

        if(fragment.isRemoving()){
            fragment.getArguments().putAll(data);
        }else{
            Log.e("wislie", "newInstance custom_group_id:" + custom_group_id);
            fragment.setArguments(data);
        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            is_discount = getArguments().getBoolean("is_discount");
            is_recommend = getArguments().getBoolean("is_recommend");
            custom_group_id = getArguments().getLong("custom_group_id");
        }
        Log.e("wislie", "oncreate custom_group_id:" + custom_group_id);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initBizView() {
        initSpringView();
        mGoodsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mGoodsRecycler.addItemDecoration(new SpaceItemDecoration(0, 0, 0, DensityUtil.dip2px(getActivity(), 8)));
        mAdapter = new GoodsListAdapter(R.layout.item_store_goods_manager, mProducts, mProductGroups);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mGoodsRecycler.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ProductEntity product = mAdapter.getItem(position);

                if (product == null) return;
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra(Constant.CONFIG_PRODUCT, product);
                //getProductGroups(product.getCustomGroupId())
                intent.putParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP, mProductGroups);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemListener(new BaseQuickAdapter.OnAdapterItemListener() {
            @Override
            public void onGoodsOn(int position, int state) { //上架 下架

                if (!permissionAllowed(5)) {
                    showPermisssionDialog("商品管理功能权限");
                    return;
                }

                ProductEntity product = mAdapter.getItem(position);
                if (product == null) return;
                mPresenter.requestPutGoods(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        product.getId(), state, UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId(), position);
            }

            @Override
            public void onSoldout(int position, int is_sold_out) {//售空 发售

                if (!permissionAllowed(5)) {
                    showPermisssionDialog("商品管理功能权限");
                    return;
                }

                ProductEntity product = mAdapter.getItem(position);
                if (product == null) return;
                mPresenter.requestSellGoods(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                        product.getId(), is_sold_out, UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId(), position);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad(); //停止加载
                } else {
                    loadDataByMenuTypeAndSelectType(mSelectedType);
                }
            }
        });


        //菜单列表
        mGoodsOptionsAdapter = new GoodsOptionsAdapter(getActivity(), mOptions);
        mOptionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mOptionRecycler.setAdapter(mGoodsOptionsAdapter);
        mGoodsOptionsAdapter.setOnItemListener(new CommonAdapter.OnAdapterItemListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) { //全部
                    mSelectedType = Constant.STORE_ALL;
                    mGoodsOptionText.setText(mOptions.get(0));
                } else if (position == 1) { //在售
                    mSelectedType = Constant.STORE_SALE;
                    mGoodsOptionText.setText(mOptions.get(1));
                } else if (position == 2) { //下架
                    mSelectedType = Constant.STORE_OFF;
                    mGoodsOptionText.setText(mOptions.get(2));
                } else if (position == 3) { //售空
                    mSelectedType = Constant.STORE_EMPTY;
                    mGoodsOptionText.setText(mOptions.get(3));
                }
                mOptionRecycler.setVisibility(View.GONE);
                mGoodsOptionText.setVisibility(View.VISIBLE);
                mGoodsOptionsAdapter.notifySelectedPosition(position);
                initRequestByMenuTypeAndSelectType(mSelectedType);
            }
        });
    }

    @Override
    protected GoodsManagePresenter createPresenter() {
        return new GoodsManagePresenter(getActivity(), this);
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(getActivity()));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();//停止加载
                } else {
                    initRequestByMenuTypeAndSelectType(mSelectedType);
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    @Override
    protected void loadData() {
        showProgressDialog();
        initData();
    }

    public void initData() {
        //初始化数据
        initRequestByMenuTypeAndSelectType(Constant.STORE_ALL);
    }

    /**
     * 筛选 会调用初始化请求
     *
     * @param selectType
     */
    private void initRequestByMenuTypeAndSelectType(int selectType) {
        int[] state = null;
        Boolean is_sold_out = null;
        //筛选的类型
        switch (selectType) {
            case Constant.STORE_SALE: //状态(筛选时候用) 0上架，1下架  is_sold_out true
                is_sold_out = false;
                break;
            case Constant.STORE_OFF:
                state = new int[]{1};
                break;
            case Constant.STORE_EMPTY:
                is_sold_out = true;
                break;
        }

        //折扣
        if (is_discount) {
            initRequestStoreManageList(state, is_sold_out, null, null, is_discount);
            return;
        }

        //推荐
        if (is_recommend) {
            initRequestStoreManageList(state, is_sold_out, is_recommend, null, null);
            return;
        }

        //其他菜单
        if (custom_group_id >= 0) {
            initRequestStoreManageList(state, is_sold_out, null, custom_group_id, null);
            return;
        }

        //菜单中的全部
        initRequestStoreManageList(state, is_sold_out, null, null, null);
    }


    /**
     * 筛选 非第一次发起请求
     *
     * @param selectType
     */
    private void loadDataByMenuTypeAndSelectType(int selectType) {

        int[] state = null;
        Boolean is_sold_out = null;
        //筛选的类型
        switch (selectType) {
            case Constant.STORE_SALE:
                state = new int[]{0};
                break;
            case Constant.STORE_OFF:
                state = new int[]{1};
                break;
            case Constant.STORE_EMPTY:
                is_sold_out = true;
                break;
        }

        //折扣
        if (is_discount) {
            requestStoreManageList(state, is_sold_out, null, null, is_discount);
            return;
        }

        //推荐
        if (is_recommend) {
            requestStoreManageList(state, is_sold_out, is_recommend, null, null);
            return;
        }

        //其他菜单
        if (custom_group_id >= 0) {
            requestStoreManageList(state, is_sold_out, null, custom_group_id, null);
            return;
        }

        //菜单中的全部
        requestStoreManageList(state, is_sold_out, null, null, null);

    }


    /*
     * 初始化请求
     */
    private void initRequestStoreManageList(int[] state, Boolean is_sold_out,
                                            Boolean is_recommend, Long custom_group_id, Boolean is_discount) {

        mCurrentPage = 0;
        mPresenter.getGoodsList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), mCurrentPage,
                PAGE_SIZE, state, is_sold_out, is_recommend, custom_group_id, is_discount, true);
    }

    /**
     * 非第一次发起请求
     */
    private void requestStoreManageList(int[] state, Boolean is_sold_out,
                                        Boolean is_recommend, Long custom_group_id, Boolean is_discount) {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getGoodsList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), mCurrentPage,
                    PAGE_SIZE, state, is_sold_out, is_recommend, custom_group_id, is_discount, false);
        } else {
            finishRefreshAndLoad();
        }

    }

    @OnClick({R.id.goods_menu_icon, R.id.empty_layout, R.id.goods_option})
    public void onClick(View view) {
        switch (view.getId()) {
            //向下翻转的箭头
            case R.id.goods_menu_icon:
                if (mOptionRecycler.getVisibility() == View.VISIBLE) {
                    mMenuIcon.setImageResource(R.mipmap.store_next_icon);
                    mOptionRecycler.setVisibility(View.GONE);
                    mGoodsOptionText.setVisibility(View.VISIBLE);
                } else {
                    mMenuIcon.setImageResource(R.mipmap.store_previous_icon);
                    mOptionRecycler.setVisibility(View.VISIBLE);
                    mGoodsOptionText.setVisibility(View.INVISIBLE);
                }
                break;

            //空页面添加商品
            case R.id.empty_layout:

                if (!permissionAllowed(5)) {
                    showPermisssionDialog("商品管理功能权限");
                    return;
                }

                if (mProductGroups.size() <= 0) {
                    ToastUtil.show("请先添加分类");
                    return;
                }
                //推荐或者折扣
                if (custom_group_id == Constant.STORE_MENUE_RECOMMEND || custom_group_id == Constant.STORE_MENUE_DISCOUNT) {
                    Intent intent = new Intent(getActivity(), StoreGoodsAddActivity.class);
                    intent.putParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP, mProductGroups);
                    startActivity(intent);
                    break;
                }

                Intent intent = new Intent(getActivity(), StoreGoodsAddActivity.class);
                ArrayList<ProductGroupEntity> groups = getProductGroups(custom_group_id);
                intent.putParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP, groups);
                startActivity(intent);
                break;

        }
    }

    //得到商品列表
    private ArrayList<ProductGroupEntity> getProductGroups(long customGroupId) {
        if (custom_group_id == -1) return mProductGroups;

        ArrayList<ProductGroupEntity> groups = new ArrayList<>();
        for (int i = 0; i < mProductGroups.size(); i++) {
            ProductGroupEntity productGroup = mProductGroups.get(i);
            if (productGroup == null) continue;
            if (productGroup.getId() == customGroupId) {
                groups.add(productGroup);
                break;
            }
        }
        return groups;
    }


    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }


    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpringView != null)
                    mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }


    @Override
    public void getGoodsFailed() {
        mAdapter.addFooterView(null);
        mAdapter.removeAll();
        mEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void showNoData() {
        mAdapter.addFooterView(null);
        mAdapter.removeAll();
        if (mEmptyLayout != null)
            mEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void newGoodsList(ArrayList<ProductEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelectedCount(0);
        }
        if (dataList.size() > 0) {
            if (mEmptyLayout != null)
                mEmptyLayout.setVisibility(View.GONE);
            if (mSpringView != null)
                mSpringView.setVisibility(View.VISIBLE);
        }
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void addGoodsList(ArrayList<ProductEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelectedCount(0);
        }
        mAdapter.notifyDataChangedAfterLoadMore(dataList, true);
    }

    @Override
    public void addGoodsGroups(ArrayList<ProductGroupEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelectedCount(0);
        }
        if (mProductGroups.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.CONFIG_CLASSIFY_TYPE, Constant.CLASSIFY_GROUP);
            bundle.putParcelableArrayList(Constant.CONFIG_PRODUCT_GROUP, dataList);
            BaseEventManager.post(bundle, StoreManageActivity.class.getName());
        }
        mProductGroups.clear();
        mProductGroups.addAll(dataList);
    }

    @Override
    public void getPermissionFailed() {

    }

    @Override
    public void showCompleteAllData() {
        mAdapter.notifyDataChangedAfterLoadMore(false);
        mAdapter.addFooterView(showFooterNoMoreData());
    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mPageCount = pageCount;
    }

    @Override
    public void putGoodsSuccess(int position) {
        ProductEntity product = mAdapter.getItem(position);
        if (product == null) return;
        if (product.getState() == 0) {
            product.setState(1);
        } else if (product.getState() == 1) {
            product.setState(0);
        }
        //根据状态更新列表
        updateGoodsList(product, position);
    }


    @Override
    public void putGoodsFailed() {
        finishRefreshAndLoad();
    }

    @Override
    public void sellGoodsSuccess(int position) {
        ProductEntity product = mAdapter.getItem(position);
        if (product == null) return;
        product.setSoldOut(!product.getSoldOut());
        updateGoodsList(product, position);

    }

    @Override
    public void sellGoodsFailed() {
        finishRefreshAndLoad();
    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);

        if (baseEvent.getTagString().equals(ACTION_NAME)) {
            Bundle bundle = (Bundle) baseEvent.getData();
            //商品状态
            int productState = bundle.getInt(Constant.CONFIG_PRODUCT_STATE);
            ProductEntity product = bundle.getParcelable(Constant.CONFIG_PRODUCT);

            if (productState == Constant.GOODS_UPDATE) {
                Log.e("wislie", "商品更新");
                //商品更新
                searchSameProduct(product); //推荐和折扣的还没进行更新
            } else if (productState == Constant.GOODS_ADD) {
                Log.e("wislie", "商品添加");
                //商品添加
                addProduct(product);
            }
        }
    }

    //添加商品到列表中
    private void addProduct(ProductEntity product) {
        List<ProductEntity> dataList = mAdapter.getData();
        if (dataList == null) return;
        //当分类列表为没有数据时
        if (dataList.size() == 0) {
            if (custom_group_id == product.getCustomGroupId().longValue()) {
                addGoodsList(product, 0);
            }
            return;
        }

        //全部
        if (custom_group_id == Constant.STORE_MENUE_ALL) {
            addGoodsList(product, 0);
            return;
        }
        //折扣
        if (custom_group_id == Constant.STORE_MENUE_DISCOUNT && product.getDiscount()) {
            addGoodsList(product, 0);
            return;
        }

        //推荐
        if (custom_group_id == Constant.STORE_MENUE_RECOMMEND && product.getRecommend()) {
            addGoodsList(product, 0);
            return;
        }


        //分类列表已经有数据
        for (int i = 0; i < dataList.size(); i++) {
            ProductEntity data = dataList.get(i);
            if (data.getCustomGroupId().longValue() == product.getCustomGroupId().longValue()) {

                addGoodsList(product, 0);
                break;
            }
        }
    }

    //添加到列表中
    private void addGoodsList(ProductEntity product, int position) {

        switch (mSelectedType) {
            //全部
            case Constant.STORE_ALL:
                //局部刷新
                if (!product.getDeleted()) {
                    mAdapter.add(position, product);
                }
                break;
            //在售
            case Constant.STORE_SALE:
                if (!product.getSoldOut() && !product.getDeleted()) {
                    mAdapter.add(position, product);
                }

                break;
            //下架
            case Constant.STORE_OFF:
                if (product.getState() != 0 && !product.getDeleted()) {
                    mAdapter.add(position, product);
                }
                break;
            //售空
            case Constant.STORE_EMPTY:
                if (product.getSoldOut() && !product.getDeleted()) {
                    mAdapter.add(position, product);
                }
                break;
        }
        if (mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
        }
    }


    //查找相同product,并做更新
    private void searchSameProduct(ProductEntity product) {
        List<ProductEntity> dataList = mAdapter.getData();
        if (dataList == null) return;
        for (int i = 0; i < dataList.size(); i++) {

            ProductEntity data = dataList.get(i);
            if (data.getId().longValue() == product.getId().longValue()) {
                assignProduct(product, data);
                //如果is_recommend = true
                updateGoodsList(data, i);
                break;
            }
        }
    }


    //将fromProduct的属性赋值给 toProduct
    private void assignProduct(ProductEntity fromProduct, ProductEntity toProduct) {
        toProduct.setState(fromProduct.getState());
        toProduct.setStrategyPrice(fromProduct.getStrategyPrice());
        toProduct.setPrice(fromProduct.getPrice());
        toProduct.setRealPrice(fromProduct.getRealPrice());
        toProduct.setCustomGroupId(fromProduct.getCustomGroupId());
        toProduct.setName(fromProduct.getName());
        toProduct.setDiscount(fromProduct.getDiscount());
        toProduct.setEnableWholeShopDiscount(fromProduct.getEnableWholeShopDiscount());
        toProduct.setUnit(fromProduct.getUnit());
        toProduct.setLogoPicUrl(fromProduct.getLogoPicUrl());
        toProduct.setPicUrl(fromProduct.getPicUrl());
        toProduct.setDescription(fromProduct.getDescription());
        toProduct.setDeleted(fromProduct.getDeleted());
        toProduct.setRecommend(fromProduct.getRecommend());
        toProduct.setTasteDesc(fromProduct.getTasteDesc());
        toProduct.setTitle(fromProduct.getTitle());
        toProduct.setSetMeal(fromProduct.getSetMeal());
        toProduct.setSort(fromProduct.getSort());
        toProduct.setDescription(fromProduct.getDescription());
    }

    //局部刷新
    private void updateGoodsList(ProductEntity product, int position) {

        switch (mSelectedType) {
            //全部
            case Constant.STORE_ALL:

                if (product.getDeleted() ||
                        (is_recommend && product.getRecommend() != null && product.getRecommend() == Boolean.FALSE) ||
                        (is_discount && product.getDiscount() != null && product.getDiscount() == Boolean.FALSE)) {
                    mAdapter.remove(position);
                } else {
                    mAdapter.notifyItemRangeChanged(position, 1, "");
                }

                break;
            //在售
            case Constant.STORE_SALE:
                if (product.getSoldOut() || product.getDeleted() ||
                        (is_recommend && product.getRecommend() != null && product.getRecommend() == Boolean.FALSE) ||
                        (is_discount && product.getDiscount() != null && product.getDiscount() == Boolean.FALSE)) {
                    mAdapter.remove(position);
                } else {
                    mAdapter.notifyItemRangeChanged(position, 1, "");
                }

                break;
            //下架
            case Constant.STORE_OFF:
                if (product.getState() == 0 || product.getDeleted() ||
                        (is_recommend && product.getRecommend() != null && product.getRecommend() == Boolean.FALSE) ||
                        (is_discount && product.getDiscount() != null && product.getDiscount() == Boolean.FALSE)) {
                    mAdapter.remove(position);
                } else {
                    mAdapter.notifyItemRangeChanged(position, 1, "");
                }
                break;
            //售空
            case Constant.STORE_EMPTY:
                if (!product.getSoldOut() || product.getDeleted() ||
                        (is_recommend && product.getRecommend() != null && product.getRecommend() == Boolean.FALSE) ||
                        (is_discount && product.getDiscount() != null && product.getDiscount() == Boolean.FALSE)) {
                    mAdapter.remove(position);
                } else {
                    mAdapter.notifyItemRangeChanged(position, 1, "");
                }
                break;
        }
        if (mAdapter.getData().size() == 0) {
            mAdapter.addFooterView(null);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }
}
