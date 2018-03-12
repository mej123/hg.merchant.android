package com.zishan.sardinemerchant.activity.store;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.goods.GoodsListPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.store.goods.GoodsListView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.store.GoodsBrandAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.DensityUtil;
import top.ftas.ftaswidget.recyclerview.WrapStaggeredGridLayoutManager;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.recyclerview.model.SpaceItemDecoration;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * Created by yang on 2017/9/29.
 * <p>
 * 商品某个分类
 */

public class GoodsBrandActivity extends BActivity<GoodsListView, GoodsListPresenter> implements GoodsListView {


    @BindView(R.id.goods_brand_springview)
    SpringView mSpringView;
    @BindView(R.id.goods_brand_recycler_view)
    RecyclerView mRecycler;

    private GoodsBrandAdapter mAdapter;
    private List<ProductEntity> mDataList = new ArrayList<>();

    private ArrayList<ProductGroupEntity> mProductGroupList = new ArrayList<>();
    private ProductGroupEntity mProductGroup;


    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;

    private long custom_group_id = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_brand;
    }


    @Override
    public void initActionBar() {
        super.initActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<ProductGroupEntity> dishList = intent.getParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP);
            if (dishList != null && dishList.size() > 0) {
                mProductGroupList.clear();
                mProductGroupList.addAll(dishList);
                mProductGroup = dishList.get(0);
            }
        }

        if (mProductGroup != null && !TextUtils.isEmpty(mProductGroup.getName())) {
            setActionbarTitle(mProductGroup.getName());
            custom_group_id = mProductGroup.getId();
        }
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);

    }

    @Override
    protected GoodsListPresenter createPresenter() {
        return new GoodsListPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        initSpringView();
        WrapStaggeredGridLayoutManager gridLayoutManager = new WrapStaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.addItemDecoration(new SpaceItemDecoration(
                DensityUtil.dip2px(this, 5), DensityUtil.dip2px(this, 14),
                DensityUtil.dip2px(this, 5), 0));
        mAdapter = new GoodsBrandAdapter(R.layout.item_goods_brand, mDataList);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductEntity product = mAdapter.getItem(position);
                if (product == null) return;
                Intent intent = new Intent(GoodsBrandActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constant.CONFIG_PRODUCT, product);
                intent.putParcelableArrayListExtra(Constant.CONFIG_PRODUCT_GROUP, mProductGroupList);
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad(); //停止加载
                } else {
                    requestStoreManageList(null, null, null, custom_group_id, null);
                }
            }
        });

        showProgressDialog();
        initRequestStoreManageList(null, null, null, custom_group_id, null);
    }


    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();//停止加载
                } else {
                    initRequestStoreManageList(null, null, null, custom_group_id, null);
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initRequestStoreManageList(null, null, null, custom_group_id, null);
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
            mSpringView.onFinishFreshAndLoad();
            mPresenter.getGoodsList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(), mCurrentPage,
                    PAGE_SIZE, state, is_sold_out, is_recommend, custom_group_id, is_discount, false);
        } else {
            mSpringView.onFinishFreshAndLoad(); //停止加载
        }

    }


    @OnClick({R.id.add_good})
    public void onClick(View view) {
        switch (view.getId()) {
            //添加商品
            case R.id.add_good:
                //商品添加
                Intent intent = new Intent(this, StoreGoodsAddActivity.class);
                intent.putExtra(Constant.CONFIG_PRODUCT_GROUP, mProductGroupList);
                startActivity(intent);
                break;
        }
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
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    public void getGoodsFailed() {

    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mAdapter.addFooterView(null);

    }

    @Override
    public void newGoodsList(ArrayList<ProductEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelectedCount(0);
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
    public void showCompleteAllData() {
        mAdapter.notifyDataChangedAfterLoadMore(false);
        mAdapter.addFooterView(showFooterNoMoreData());
    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mPageCount = pageCount;
    }

    @Override
    public void addGoodsGroups(ArrayList<ProductGroupEntity> dataList) {

    }

    @Override
    public void getPermissionFailed() {

    }

}
