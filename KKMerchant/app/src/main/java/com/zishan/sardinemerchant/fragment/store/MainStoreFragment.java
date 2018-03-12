package com.zishan.sardinemerchant.fragment.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.store.ProductEntity;
import com.example.wislie.rxjava.model.store.ProductGroupEntity;
import com.example.wislie.rxjava.presenter.base.store.goods.GoodsListPresenter;
import com.example.wislie.rxjava.view.base.store.goods.GoodsListView;
import com.hg.ftas.util.ToastUtil;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.store.ClassifyManageActivity;
import com.zishan.sardinemerchant.activity.store.StoreGoodsAddActivity;
import com.zishan.sardinemerchant.fragment.BFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.eventbus.BaseEvent;
import top.ftas.ftaswidget.view.CustomToolBar;

/**
 * 商品管理
 * Created by yang on 2017/9/12.
 */

public class MainStoreFragment extends BFragment<GoodsListView, GoodsListPresenter>
        implements GoodsListView {

    @BindView(R.id.custom_tool_bar)
    CustomToolBar mToolBar;
    @BindView(R.id.goods_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.store_vp)
    ViewPager mVPage;
    private final int PAGE_SIZE = 10;
    private int mCurrentPage = 0;
    //临时的storeId,用来判断是否需要重新加载
    private long mTempStoreId;
    //判断是否有权限, 分类管理如果有商品，商品管理的分类不更新
    private boolean isPermissionAllowed = true;

    //菜单
    private ArrayList<ProductGroupEntity> mProductGroups = new ArrayList<>();

    private class StoreFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private List<ProductGroupEntity> productGroups;
        private List<GoodsListFragment> goodsListFragment;

        public StoreFragmentPagerAdapter(FragmentManager fm, List<ProductGroupEntity> productGroups,
                                         List<GoodsListFragment> goodsListFragment) {
            super(fm);
            this.productGroups = productGroups;
            this.goodsListFragment = goodsListFragment;
        }


        @Override
        public int getItemPosition(Object object) {
            if (!goodsListFragment.contains(object)) {
                return PagerAdapter.POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= productGroups.size()) return "";
            ProductGroupEntity productGroup = productGroups.get(position);
            String title = "";
            if (productGroup != null) {
                title = productGroup.getName();
            }
            return title;
        }

        @Override
        public Fragment getItem(int position) {

            if (position >= goodsListFragment.size()) return null;
            return goodsListFragment.get(position);
        }


        @Override
        public int getCount() {
            return goodsListFragment.size();
        }

        public void addFragment(GoodsListFragment fragment, ProductGroupEntity productGroup) {
            productGroups.add(productGroup);

            if (!fragment.isAdded()) {
                goodsListFragment.add(fragment);
            }
            notifyDataSetChanged();
        }

        public void removeFragment(ProductGroupEntity productGroup) {
            int index = productGroups.indexOf(productGroup);
            productGroups.remove(productGroup);
            goodsListFragment.remove(index);

            notifyDataSetChanged();

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            GoodsListFragment fg = (GoodsListFragment) getItem(position);
            if (fg != null) {
                fg = null;
            }
        }
    }


    @Override
    public void loadData() { //为什么从没有元素的切到有元素的会崩溃

    }
    private StoreFragmentPagerAdapter mPagerAdapter;
    private void loadGoodsData() {
        if (mProductGroups.size() > 3) return;
        mPresenter.getGoodsList(UserConfig.getInstance(ClientApplication.getApp()).getStoreId(),
                mCurrentPage, PAGE_SIZE, null, null, null, null, null, true);
    }

    @Override
    public void onResume() {
        super.onResume();

        long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
        if (mTempStoreId != storeId) {
            if (mPagerAdapter.getCount() >= 3) {
                for (int i = mProductGroups.size() - 1; i >= 3; i--) {
                    ProductGroupEntity productGroup = mProductGroups.get(i);
                    mPagerAdapter.removeFragment(productGroup);
                }
                loadGoodsData();
                //                loadData();
            }
            mTempStoreId = storeId;
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_store;
    }


    @Override
    protected void initBizView() {
        mToolBar.setCustomToolBarListener(new CustomToolBar.CustomToolBarListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                if(!permissionAllowed(5)){
                    showPermisssionDialog("商品管理功能权限");
                    return;
                }

                if (mProductGroups.size() <= 3) {
                    ToastUtil.show("请先添加分类");
                    return;
                }
                //商品添加
                Intent intent = new Intent(getActivity(), StoreGoodsAddActivity.class);
                intent.putExtra(Constant.CONFIG_PRODUCT_GROUP, getProductGroups());
                startActivity(intent);
            }
        });
        initData();

    }

    //得到商品的groups
    private ArrayList<ProductGroupEntity> getProductGroups(){
        ArrayList<ProductGroupEntity> dataList = new ArrayList<>();
        for(int i = 3; i<mProductGroups.size(); i++){
            dataList.add(mProductGroups.get(i));
        }
        return dataList;
    }

    @Override
    protected GoodsListPresenter createPresenter() {
        return new GoodsListPresenter(getActivity(), this);
    }

    private List<GoodsListFragment> mGoodFragments = new ArrayList<>();
    private void initData() {
        mProductGroups.add(newProductGroupEntity(Constant.STORE_MENUE_ALL, "全部"));
        mProductGroups.add(newProductGroupEntity(Constant.STORE_MENUE_DISCOUNT, "折扣"));
        mProductGroups.add(newProductGroupEntity(Constant.STORE_MENUE_RECOMMEND, "推荐"));

        //没有group_id的设置group_id为-1
        mGoodFragments.add(GoodsListFragment.newInstance(false, false, Constant.STORE_MENUE_ALL)); //全部
        mGoodFragments.add(GoodsListFragment.newInstance(true, false, Constant.STORE_MENUE_DISCOUNT)); //折扣
        mGoodFragments.add(GoodsListFragment.newInstance(false, true, Constant.STORE_MENUE_RECOMMEND)); //推荐

        mPagerAdapter = new StoreFragmentPagerAdapter(getChildFragmentManager(), mProductGroups, mGoodFragments);
        mVPage.setOffscreenPageLimit(mGoodFragments.size());//这么写最多5个页面缓存
        mVPage.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mVPage);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                GoodsListFragment goodsFragment = (GoodsListFragment) mPagerAdapter.getItem(tab.getPosition());
                if (mGoodFragments != null) {
                    goodsFragment.initData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //创建ProductGroupEntity
    private ProductGroupEntity newProductGroupEntity(Long id, String name) {
        ProductGroupEntity productGroupEntity = new ProductGroupEntity();
        productGroupEntity.setId(id);
        productGroupEntity.setStoreId(0l);
        productGroupEntity.setName(name);
        productGroupEntity.setIcon("");
        productGroupEntity.setSort("");
        productGroupEntity.setProductNum(0);
        productGroupEntity.setSelectedCount(0);
        return productGroupEntity;
    }

    @OnClick({R.id.store_separate_manager})
    public void onClick(View view) {
        switch (view.getId()) {
            //分类管理
            case R.id.store_separate_manager:
                if(!permissionAllowed(5)){
                    showPermisssionDialog("商品管理功能权限");
                }else{
                    Intent intent = new Intent(getActivity(), ClassifyManageActivity.class);
                    startActivityForResult(intent, ClassifyManageActivity.CLASSIFY_MANAGE_BACK);

                }
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

    @Override
    public void getGoodsFailed() {

    }

    @Override
    public void showNoData() {
        isPermissionAllowed = true;
    }

    @Override
    public void newGoodsList(ArrayList<ProductEntity> dataList) {
        isPermissionAllowed = true;
    }

    @Override
    public void addGoodsList(ArrayList<ProductEntity> dataList) {

    }

    @Override
    public void addGoodsGroups(ArrayList<ProductGroupEntity> dataList) {
        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                ProductGroupEntity productGroup = dataList.get(i);
                productGroup.setSelectedCount(0);
                if(containsGroupid(productGroup)) continue;

                GoodsListFragment goodFragment = GoodsListFragment.newInstance(false, false, productGroup.getId());
                mPagerAdapter.addFragment(goodFragment, productGroup);
            }
            mVPage.setOffscreenPageLimit(mVPage.getOffscreenPageLimit() + dataList.size());
        }

    }

    @Override
    public void getPermissionFailed() {
        isPermissionAllowed = false;
    }


    @Override
    public void showCompleteAllData() {

    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {

    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        if (baseEvent.getTagString().equals(ACTION_NAME)) {

            Bundle bundle = (Bundle) baseEvent.getData();
            int classifyType = bundle.getInt(Constant.CONFIG_CLASSIFY_TYPE);
            switch (classifyType) {
                //重新刷新
                case Constant.CLASSIFY_GROUP:
                    if (mPagerAdapter.getCount() <= 3) {
                        ArrayList<ProductGroupEntity> productGroups = bundle.getParcelableArrayList(Constant.CONFIG_PRODUCT_GROUP);
                        for (int i = 0; i < productGroups.size(); i++) {
                            ProductGroupEntity productGroup = productGroups.get(i);
                            GoodsListFragment goodFragment = GoodsListFragment.newInstance(false, false, productGroup.getId());
                            mPagerAdapter.addFragment(goodFragment, productGroup);
                        }
                    }

                    break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            //分类管理返回
            case ClassifyManageActivity.CLASSIFY_MANAGE_BACK:
                //如果无权限
                if (!isPermissionAllowed) return;
                ArrayList<ProductGroupEntity> dataList = data.getParcelableArrayListExtra("product_group");
                //需要和当前的进行比较
                resetList(dataList);
                break;
        }
    }

    //会出错，但不知道哪里出错 看打印name:
    private void resetList(ArrayList<ProductGroupEntity> dataList) {
        //查找mProductGroups中存在，dataList中不存在的集合

        //未添加的集合
        List<ProductGroupEntity> removeList = new ArrayList<>();
        for (int i = 3; i < mProductGroups.size(); i++) {
            ProductGroupEntity oldGroup = mProductGroups.get(i);
            boolean isExsit = false;
            for (int j = 0; j < dataList.size(); j++) {
                ProductGroupEntity newGroup = dataList.get(j);
                if (oldGroup.getId().longValue() == newGroup.getId().longValue()) {
                    oldGroup.setName(newGroup.getName());
                    isExsit = true;
                }
            }
            if (!isExsit) {
                removeList.add(oldGroup);
            }
        }

        for (int i = removeList.size() - 1; i >= 0; i--) {
            ProductGroupEntity removeGroup = removeList.get(i);
            Log.e("wislie", "remove name:" + removeGroup.getName());
            mPagerAdapter.removeFragment(removeGroup);
            mVPage.setOffscreenPageLimit(mVPage.getOffscreenPageLimit() - 1);
        }


        //刚添加的集合
        List<ProductGroupEntity> addList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ProductGroupEntity newGroup = dataList.get(i);
            boolean isExsit = false;
            for (int j = 0; j < mProductGroups.size(); j++) {
                ProductGroupEntity oldGroup = mProductGroups.get(j);
                if (newGroup.getId().longValue() == oldGroup.getId().longValue()) {
                    isExsit = true;
                    break;
                }
            }
            if (!isExsit) {
                addList.add(newGroup);
            }
        }

        for (int i = 0; i < addList.size(); i++) {
            ProductGroupEntity newGroup = addList.get(i);
            Log.e("wislie", "add name:" + newGroup.getName());
            GoodsListFragment goodFragment = GoodsListFragment.newInstance(false, false, newGroup.getId());
            ProductGroupEntity add_productGroup = newProductGroupEntity(newGroup.getId(), newGroup.getName());
            mPagerAdapter.addFragment(goodFragment, add_productGroup);
            mVPage.setOffscreenPageLimit(mVPage.getOffscreenPageLimit() + 1);
        }

        if (addList.size() == 0 || removeList.size() == 0) {
            mPagerAdapter.notifyDataSetChanged();
        }

    }

    //判断是否包含groupid
    private boolean containsGroupid(ProductGroupEntity group){
        for (int i = 0; i < mProductGroups.size(); i++) {
            ProductGroupEntity productGroup = mProductGroups.get(i);
            if (productGroup.getId().longValue() == group.getId().longValue()) return true;
        }
        return false;
    }

}
