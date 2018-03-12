package com.zishan.sardinemerchant.activity.personal.staffs;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.MyStaffEntity;
import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.personal.staff.NewStaffListPresenter;
import com.example.wislie.rxjava.view.personal.staff.NewStaffListView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.NewStaffListAdapter;
import com.zishan.sardinemerchant.adapter.personal.StaffStoreSelectAdapter;
import com.zishan.sardinemerchant.utils.Skip;
import com.zishan.sardinemerchant.view.DropMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 新  员工列表
 */

public class NewStaffListActivity extends BActivity<NewStaffListView, NewStaffListPresenter> implements NewStaffListView {

    @BindView(R.id.tv_all_store)
    TextView mAllStore;
    @BindView(R.id.all_staff_recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.filtrate_icon)
    ImageView mFiltrateIcon;
    @BindView(R.id.drop_menu)
    DropMenu mDropMenu;//筛选下拉框
    @BindView(R.id.withdraw_list_empty_layout)
    LinearLayout mWithdrawListEmptyLayout;//空布局
    private Integer is_all_store = 0;//0：全部门店
    private boolean isAllStore = true;//添加当前是否显示所有门店信息标记  默认显示全部

    //弹出框
    private View mPopView;
    private NewStaffListAdapter mAdapter;

    private List<MyStaffEntity> mDataList = new ArrayList<>();//员工列表集合
    private List<StoreMsgEntity> mStoreSelectDataList = new ArrayList<>();//门店筛选集合
    private RecyclerView storeSelectRecycleView;//员工列表recycleView
    private StaffStoreSelectAdapter mStaffStoreSelectAdapter;//门店筛选recycleView

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_staff_list));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
        setActionBarMenuText(getString(R.string.record)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Skip.toActivity(NewStaffListActivity.this, NewOperatingRecordActivity.class);
            }
        });
    }

    @Override
    protected NewStaffListPresenter createPresenter() {
        return new NewStaffListPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_staff_list;
    }

    @Override
    protected void initContentView() {

        mAdapter = new NewStaffListAdapter(R.layout.item_new_staff_list, mDataList);
        mRecycleView.setLayoutManager(new LinearLayoutManager(NewStaffListActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyStaffEntity myStaffEntity = mAdapter.getItem(position);
                Intent waiterIntent = new Intent(NewStaffListActivity.this, NewStaffDetailsActivity.class);
                waiterIntent.putExtra("myStaff", myStaffEntity);
                startActivity(waiterIntent);
            }
        });
        initPopupView();//初始化popupView
        loadData();//加载数据
    }

    private void initPopupView() {

        //弹出框
        mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_new_staff_list_store_select, null);
        mPopView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        storeSelectRecycleView = (RecyclerView) mPopView.findViewById(R.id.store_select_recycle_view);
        mStaffStoreSelectAdapter = new StaffStoreSelectAdapter(R.layout.item_store_select, mStoreSelectDataList);
        storeSelectRecycleView.setLayoutManager(new LinearLayoutManager(NewStaffListActivity.this,
                LinearLayoutManager.VERTICAL, false));
        storeSelectRecycleView.setAdapter(mStaffStoreSelectAdapter);

        mStaffStoreSelectAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<StoreMsgEntity> dataList = mStaffStoreSelectAdapter.getData();
                //设置点击门店时的标记
                for (int i = 0; i < dataList.size(); i++) {
                    if (position == i) {
                        dataList.get(i).setClick(true);//选中
                    } else {
                        dataList.get(i).setClick(false);//未选中
                    }
                }
                mStaffStoreSelectAdapter.notifyDataSetChanged();
                mDropMenu.hideDropMenu(getActivity());
                //position=0,默认加载所有店铺
                StoreMsgEntity storeMsgEntity = mStaffStoreSelectAdapter.getItem(position);
                if (storeMsgEntity == null) return;
                mAllStore.setText(storeMsgEntity.getStoreName());//重新回显选择门店名称
                //position等于0时，加载所有门店下的员工,其它情况加载选中门店对应的员工信息
                if (position == 0) {
                    isAllStore = true;
                    mPresenter.getMyStaff(null, UserConfig.getInstance
                            (NewStaffListActivity.this).getMerchantId());
                } else {
                    isAllStore = false;
                    mPresenter.getMyStaff(storeMsgEntity.getStoreId(), UserConfig.getInstance
                            (NewStaffListActivity.this).getMerchantId());
                }
            }
        });

        mDropMenu.initDropMenuView(getActivity(), mPopView, mFiltrateIcon,
                R.mipmap.all_store_select_icon, R.mipmap.all_store_select_icon);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    private void loadData() {
        Long merchantId = UserConfig.getInstance(this).getMerchantId();
        mPresenter.getMyStaff(null, merchantId);
        mPresenter.getMyStoreInfo(merchantId,
                null, null, null, is_all_store, null, true);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.rl_all_store_layout, R.id.tv_staff_add, R.id.tv_position_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_all_store_layout://所有门店
                if (mDropMenu.isMenuVisible()) {
                    mDropMenu.hideDropMenu(getActivity());
                    return;
                }
                mDropMenu.showDropMenu(getActivity());
                break;
            case R.id.tv_staff_add://员工添加
                Skip.toActivity(this, NewStaffAddActivity.class);
                break;
            case R.id.tv_position_manager://职位管理
                Skip.toActivity(this, NewPositionListActivity.class);
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
    public void getMyStaffSuccess(List<MyStaffEntity> dataList) {
        if (dataList == null) return;
        if (dataList.size() > 0) {
            //如果当前显示全部门店,显示员工对应的店铺数量，反之不显示
            for (int i = 0; i < dataList.size(); i++) {
                if (isAllStore) {
                    dataList.get(i).setShowStoreNum(true);
                } else {
                    dataList.get(i).setShowStoreNum(false);
                }
            }
            mRecycleView.setVisibility(View.VISIBLE);
            mWithdrawListEmptyLayout.setVisibility(View.GONE);
            mAdapter.setNewData(dataList);
            mAdapter.notifyDataSetChanged();

            if (dataList.size()>8){
                mAdapter.addFooterView(showFooterNoMoreData());
            }
        }
    }

    @Override
    public void getMystaffFailed() {

    }

    @Override
    public void getMyStoreInfoSuccess(List<StoreMsgEntity> dataList, boolean is_all) {

        if (dataList == null) return;
        if (is_all) { //所有门店
            StoreMsgEntity firstStoreMsg = new StoreMsgEntity();
            firstStoreMsg.setStoreName("全部门店");
            dataList.add(0, firstStoreMsg);
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setClick(false);
            }
            mStaffStoreSelectAdapter.setNewData(dataList);
        }
        mStaffStoreSelectAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMyStoreInfoFailed() {

    }

    @Override
    public void showNoData() {


    }
}
