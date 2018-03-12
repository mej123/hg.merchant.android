package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.page.my_store.MyStorePresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.page.my_store.MyStoreView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.personal.store_msg.StoreMsgEditActivity;
import com.zishan.sardinemerchant.adapter.page.MyStoreAdapter;
import com.zishan.sardinemerchant.adapter.page.StoreAddressAdapter;
import com.zishan.sardinemerchant.adapter.page.StoreStateAdapter;
import com.zishan.sardinemerchant.entity.MyStoreStateEntity;
import com.zishan.sardinemerchant.view.AddressMenuView;
import com.zishan.sardinemerchant.view.SingleMenuView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;
import top.ftas.ftaswidget.view.DropDownMenus;

/**
 * 我的门店
 * Created by wislie on 2018/1/11.
 */

public class MyStoreActivity extends BActivity<MyStoreView, MyStorePresenter>
        implements MyStoreView {

    @BindView(R.id.drop_down_menu)
    DropDownMenus mDropDownMenu;
    SpringView mSpringView;
    RecyclerView mRecycler;
    LinearLayout mEmptyLayout;

    private List<View> mPopupViews = new ArrayList<>();//菜单列表视图
    private List<StoreMsgEntity> mDataList = new ArrayList<>();

    private List<MyStoreStateEntity> mStoreDataList = new ArrayList<>();
    private List<MyStoreStateEntity> mStateDataList = new ArrayList<>();
    private StoreAddressAdapter mAddressAdapter;
    private StoreStateAdapter mStoreAdapter;
    private StoreStateAdapter mStateAdapter;
    private MyStoreAdapter mAdapter;
    //省id
    private Long province_id;
    //市id
    private Long city_id;
    //区id
    private Long district_id;
    //0全部门店1我的门店
    private Integer is_all_store = 0;
    //2停业整顿，3开始营业，4封停，5开店中 全部则不传
    private Integer state;

    @Override
    protected MyStorePresenter createPresenter() {
        return new MyStorePresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_store;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setToolBarColor(R.color.top_actionbar_bg_color_2);
        TextView titleText = setActionbarTitle(getString(R.string.my_store));
        setActionBarHomeIcon(R.mipmap.back_black_icon);
        setActionBarMenuIcon(-1);
        titleText.setTextColor(ContextCompat.getColor(this, R.color.top_actionbar_title_color_2));
    }

    @Override
    protected void initContentView() {

        Intent intent = getIntent();
        if (intent != null) {
            int getState = intent.getIntExtra("state", 0);
            state = getState == 0 ? null : getState;
        }

        mStoreDataList.add(new MyStoreStateEntity(0, "全部门店"));
        mStoreDataList.add(new MyStoreStateEntity(1, "我的门店"));

        //2停业整顿，3开始营业，4封停，5开店中
        mStateDataList.add(new MyStoreStateEntity(-1, "全部"));
        mStateDataList.add(new MyStoreStateEntity(3, "营业中"));
        mStateDataList.add(new MyStoreStateEntity(5, "开店中"));
        mStateDataList.add(new MyStoreStateEntity(2, "停业修整"));
        mStateDataList.add(new MyStoreStateEntity(4, "封停"));

        mAddressAdapter = new StoreAddressAdapter(R.layout.item_single_menu, mDataList);
        mStoreAdapter = new StoreStateAdapter(R.layout.item_single_menu, mStoreDataList);
        mStateAdapter = new StoreStateAdapter(R.layout.item_single_menu, mStateDataList);

        initPopViews();

        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyStoreAdapter(R.layout.item_my_store, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent storeMsgIntent = new Intent(MyStoreActivity.this,
                                StoreMsgEditActivity.class);
                        StoreMsgEntity data = mAdapter.getItem(position);
                        if (data == null || data.getStoreId() == null) return;
                        storeMsgIntent.putExtra("store_id", data.getStoreId());
                        startActivity(storeMsgIntent);
                    }
                });

        showProgressDialog();
        initData();
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {
                    getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    private void initData() {
        is_all_store = 0;
        getMyStoreInfo(null, null, null, is_all_store, state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
    }

    private void getMyStoreInfo(Long province_id, Long city_id,
                                Long district_id, Integer is_all_store, Integer state) {
        if (mDataList.size() == 0) {
            mPresenter.getMyStoreInfo(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                    null, null, null, 0, null, true);
        }
        mPresenter.getMyStoreInfo(UserConfig.getInstance(ClientApplication.getApp()).getMerchantId(),
                province_id, city_id, district_id, is_all_store, state, false);
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

    private void initPopViews() {
        String[] headers = new String[]{"全国", "全部门店", "全部状态"};


        //地区
        final AddressMenuView addressMenuView = new AddressMenuView(this) {
            @Override
            protected BaseQuickAdapter getAdapter() {
                return mAddressAdapter;
            }

            @Override
            protected void onTabClick(int position, int[] menuIndexs) {

                mAddressAdapter.setLevel(position);

                List<StoreMsgEntity> dataList = new ArrayList<>();
                if (position - 1 >= 0 && menuIndexs[position - 1] >= 0) {

                    StoreMsgEntity data;
                    if (menuIndexs[position - 1] < mAddressAdapter.getData().size()) {
                        data = mAddressAdapter.getItem(menuIndexs[position - 1]);
                    } else {
                        data = mAddressAdapter.getItem(0);
                    }

                    if (position == 1) {
                        dataList.addAll(getCityList(data.getProvinceId()));
                    } else if (position == 2) {
                        dataList.addAll(getDistrictList(data.getCityId()));
                    }
                } else {
                    dataList.addAll(getProvinceList());
                }

                mAddressAdapter.setNewData(dataList);
                //设置适配器中选中的位置
                mAddressAdapter.setSelectedPos(menuIndexs[position]);
            }

            @Override
            protected void onConfirm(int position, int[] menuIndexs) {
                if (position == -1) {
                    mDropDownMenu.closeMenu();
                    return;
                }
                int pos = menuIndexs[position];


                StoreMsgEntity data;
                if (pos < mAddressAdapter.getData().size()) {
                    data = mAddressAdapter.getItem(pos);
                } else {
                    data = mAddressAdapter.getItem(0);
                }

                if (position == 0) {
                    mDropDownMenu.setTabText(data.getProvinceName());
                } else if (position == 1) {
                    mDropDownMenu.setTabText(data.getCityName());
                } else if (position == 2) {
                    mDropDownMenu.setTabText(data.getDistrictName());
                }
                mDropDownMenu.closeMenu();

                //全国(相当于不筛选)
                if (!TextUtils.isEmpty(data.getProvinceName()) && data.getProvinceName().equals("全国")) {
                    province_id = null;
                    city_id = null;
                    district_id = null;
                    getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                    return;
                }

                //省级别
                if (data.getCityName().equals("全市") || position == 0) {
                    province_id = Long.valueOf(data.getProvinceId());
                    city_id = null;
                    district_id = null;
                    getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                    return;
                }

                if (position == 1) { //市级别
                    province_id = Long.valueOf(data.getProvinceId());
                    city_id = data.getCityId();
                    district_id = null;
                    getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                } else if (position == 2) { //区级别
                    province_id = Long.valueOf(data.getProvinceId());
                    city_id = data.getCityId();
                    district_id = Long.valueOf(data.getDistrictId());
                    getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                }
            }

            @Override
            protected void onCancel() {
                mDropDownMenu.closeMenu();
            }
        };

        mAddressAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        StoreMsgEntity data = mAddressAdapter.getItem(position);
                        //设置选中的tab
                        int selectedTabPosition = addressMenuView.getSelectedTabPosition();

                        switch (selectedTabPosition) {
                            case 0:
                                addressMenuView.setTabTitle(selectedTabPosition,
                                        data.getProvinceName());
                                addressMenuView.setMenuSelectedPos(position);
                                if (position == 0) { //全国
                                    //设置适配器中选中的位置
                                    mAddressAdapter.setSelectedPos(position);
                                    return;
                                }
                                addressMenuView.addTab();
                                break;
                            case 1:
                                addressMenuView.setTabTitle(selectedTabPosition,
                                        data.getCityName());
                                addressMenuView.setMenuSelectedPos(position);
                                if (position == 0) { //全市
                                    //设置适配器中选中的位置
                                    mAddressAdapter.setSelectedPos(position);
                                    return;
                                }
                                addressMenuView.addTab();
                                break;
                            case 2:
                                addressMenuView.setTabTitle(selectedTabPosition,
                                        data.getDistrictName());
                                addressMenuView.setMenuSelectedPos(position);
                                //设置适配器中选中的位置
                                mAddressAdapter.setSelectedPos(position);
                                break;
                        }
                    }
                });

        //门店
        SingleMenuView storeMenuView = new SingleMenuView(this) {
            @Override
            protected BaseQuickAdapter getAdapter() {
                return mStoreAdapter;
            }
        };

        mStoreAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MyStoreStateEntity data = mStoreAdapter.getItem(position);
                        mStoreAdapter.setSelectedPos(position);
                        mDropDownMenu.setTabText(data.getName());
                        mDropDownMenu.closeMenu();
                        is_all_store = data.getState();
                        //门店筛选
                        getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                    }
                });

        //门店状态
        SingleMenuView statusMenuView = new SingleMenuView(this) {
            @Override
            protected BaseQuickAdapter getAdapter() {
                return mStateAdapter;
            }
        };
        mStateAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MyStoreStateEntity data = mStateAdapter.getItem(position);
                        mStateAdapter.setSelectedPos(position);
                        mDropDownMenu.setTabText(data.getName());
                        mDropDownMenu.closeMenu();

                        if (data.getState() == -1) {
                            state = null;
                        } else {
                            state = data.getState();
                        }
                        //门店筛选
                        getMyStoreInfo(province_id, city_id, district_id, is_all_store, state);
                    }
                });


        mPopupViews.add(addressMenuView);
        mPopupViews.add(storeMenuView);
        mPopupViews.add(statusMenuView);

        //初始化内容视图
        View contentView = LayoutInflater.from(this).inflate(R.layout.content_my_store, null);
        mSpringView = (SpringView) contentView.findViewById(R.id.my_store_springview);
        mRecycler = (RecyclerView) contentView.findViewById(R.id.my_store_recycler_view);
        mEmptyLayout = (LinearLayout) contentView.findViewById(R.id.empty_store_layout);

        //装载
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), mPopupViews, contentView);
        //改变状态
        if (state != null) {
            int pos = 0;
            switch (state.intValue()) {
                case 2:
                    pos = 3;
                    break;
                case 3:
                    pos = 1;
                    break;
                case 4:
                    pos = 4;
                    break;
                case 5:
                    pos = 2;
                    break;
            }
            MyStoreStateEntity data = mStateAdapter.getItem(pos);
            mStateAdapter.setSelectedPos(pos);
            mDropDownMenu.setTabPosition(4); //4是根据点击状态得到的
            mDropDownMenu.setTabText(data.getName());
        }
    }

    //获得省列表
    private List<StoreMsgEntity> getProvinceList() {
        List<StoreMsgEntity> dataList = new ArrayList<>();
        dataList.addAll(mDataList);
        for (int i = 0; i < dataList.size() - 1; i++) {
            for (int j = dataList.size() - 1; j >= i; j--) {
                if (dataList.get(j).getProvinceId() == dataList.get(i).getProvinceId()) {
                    dataList.remove(j);
                }
            }
        }
        StoreMsgEntity data = new StoreMsgEntity();
        data.setProvinceName("全国");
        dataList.add(0, data);
        return dataList;
    }


    //先得到某个省的列表
    private List<StoreMsgEntity> getSameProvinceList(long provinceId) {
        List<StoreMsgEntity> dataList = new ArrayList<>();
        dataList.addAll(mDataList);
        for (int i = dataList.size() - 1; i >= 0; i--) {
            if (dataList.get(i).getProvinceId() != provinceId) {
                dataList.remove(i);
            }
        }

        return dataList;
    }

    //再得到市列表 去重复
    private List<StoreMsgEntity> getCityList(long provinceId) {
        List<StoreMsgEntity> dataList = new ArrayList<>();
        dataList.addAll(getSameProvinceList(provinceId));
        for (int i = 0; i < dataList.size() - 1; i++) {
            for (int j = dataList.size() - 1; j >= i; j--) {
                if (dataList.get(j).getCityId() == dataList.get(i).getCityId()) {
                    dataList.remove(j);
                }
            }
        }
        StoreMsgEntity data = new StoreMsgEntity();
        data.setProvinceId((int) provinceId);
        data.setCityName("全市");
        dataList.add(0, data);
        return dataList;
    }

    //得到市列表
    private List<StoreMsgEntity> getSameCityList(long cityId) {
        List<StoreMsgEntity> dataList = new ArrayList<>();
        dataList.addAll(mDataList);
        for (int i = dataList.size() - 1; i >= 0; i--) {
            if (cityId != dataList.get(i).getCityId()) {
                dataList.remove(i);
            }
        }

        return dataList;
    }

    //再得到区列表 去重复
    private List<StoreMsgEntity> getDistrictList(long cityId) {
        List<StoreMsgEntity> dataList = new ArrayList<>();
        dataList.addAll(getSameCityList(cityId));
        for (int i = 0; i < dataList.size() - 1; i++) {
            for (int j = dataList.size() - 1; j >= i; j--) {
                if (dataList.get(j).getDistrictId() == dataList.get(i).getDistrictId()) {
                    dataList.remove(j);
                }
            }
        }
        return dataList;
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color_2);
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
    public void getMyStoreInfoSuccess(List<StoreMsgEntity> dataList, boolean is_all) {
        if (is_all) { //所有门店
            mDataList.clear();
            mDataList.addAll(dataList);
            mAddressAdapter.setNewData(getProvinceList());
        } else {
            mAdapter.setNewData(dataList);
            mRecycler.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
        finishRefreshAndLoad();
    }

    @Override
    public void getMyStoreInfoFailed() {
        mAdapter.removeAll();
        mRecycler.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }

    @Override
    public void showNoData() {
        mAdapter.removeAll();
        mRecycler.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        finishRefreshAndLoad();
    }
}
