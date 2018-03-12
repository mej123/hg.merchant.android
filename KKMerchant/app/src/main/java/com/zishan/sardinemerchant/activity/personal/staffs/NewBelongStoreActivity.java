package com.zishan.sardinemerchant.activity.personal.staffs;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.personal.staff.NewBelongStorePresenter;
import com.example.wislie.rxjava.view.personal.staff.NewBelongStoreView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.personal.NewBelongStoreAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;

/**
 * Created by yang on 2018/1/19.
 * <p>
 * 归属店铺
 */

public class NewBelongStoreActivity extends BActivity<NewBelongStoreView,
        NewBelongStorePresenter> implements NewBelongStoreView, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.check_belong_icon)
    ImageView mCheckBelongIcon;
    @BindView(R.id.all_staff_recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.all_store_switch)
    CheckBox mAllStoreSwitch;//集团按钮开关
    @BindView(R.id.confirm_and_save)
    TextView mConfirmAndSave;//确认按钮
    private boolean switchAllStoreState = false;//集团选择按钮默认关闭
    private List<StoreMsgEntity> mDataList = new ArrayList<>();
    private ArrayList<String> mSelectStoreDataList = new ArrayList<>();//选中的店铺,用来返回数据集合
    private ArrayList<String> mSelectStoreDataLists = new ArrayList<>();//选中的店铺,比对状态
    private NewBelongStoreAdapter mAdapter;
    private Integer is_all_store = 0;//0：全部门店
    private String mStoreIds = "empty";
    private String newStoreIds;

    @Override
    protected NewBelongStorePresenter createPresenter() {
        return new NewBelongStorePresenter(this, this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.new_belong_store));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_belong_store;
    }

    @Override
    protected void initContentView() {
        if (getIntent() != null) {
            //拼接好的storeIds
            mStoreIds = getIntent().getStringExtra("mStoreIds");

        }
        //回显数据
        addCheckBoxListener();//监听当前CheckBox开关
        mAdapter = new NewBelongStoreAdapter(R.layout.item_new_belong_store, mDataList);
        mRecycleView.setLayoutManager(new LinearLayoutManager(NewBelongStoreActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //当用户点击时候,去拿到现在的选中条目的storeId和第一次的进行比较

                StoreMsgEntity storeMsgEntity = mAdapter.getItem(position);
                if (storeMsgEntity == null) return;
                boolean check = storeMsgEntity.isCheck();
                if (check) {
                    storeMsgEntity.setCheck(false);
                } else {
                    storeMsgEntity.setCheck(true);
                }

                mAdapter.notifyDataSetChanged();

                List<StoreMsgEntity> dataList = mAdapter.getData();
                mSelectStoreDataLists.clear();
                for (int i = 0; i < dataList.size(); i++) {
                    boolean checks = dataList.get(i).isCheck();
                    if (checks) {
                        mSelectStoreDataLists.add(String.valueOf(dataList.get(i).getStoreId()));//选中的店铺放到选中店铺的集合中
                    }
                }

                if (mSelectStoreDataLists.size() == 0) {
                    newStoreIds = "empty";
                } else {
                    //最新的条目状态
                    newStoreIds = getStoreIds(mSelectStoreDataLists);
                }

                //当前mStoreIds为空
                if (newStoreIds.equals(mStoreIds)) {
                    mConfirmAndSave.setEnabled(false);
                    mConfirmAndSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
                } else {
                    mConfirmAndSave.setEnabled(true);
                    mConfirmAndSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                }
            }
            //updateConfirmState();//更变底部按钮状态


        });

        mPresenter.getMyStoreInfo(UserConfig.getInstance(this).getMerchantId(),
                null, null, null, is_all_store, null, true);
    }


    public String getStoreIds(ArrayList<String> mSelectStoreDataList) {
        String newStoreIds = "";//清空上次请求的数据,防止重复拼接
        for (int i = 0; i < mSelectStoreDataList.size(); i++) {
            String storeId = mSelectStoreDataList.get(i) + ",";
            newStoreIds = newStoreIds + storeId;
        }
        return newStoreIds.substring(0, newStoreIds.length() - 1);
    }

    private void updateConfirmState() {
        List<StoreMsgEntity> dataList = mAdapter.getData();

        for (int i = 0; i < dataList.size(); i++) {
            boolean currentCheck = dataList.get(i).isCheck();//当前只要有一个被选中,就可以点击
            if (currentCheck) {
                mConfirmAndSave.setEnabled(true);
                mConfirmAndSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                return;
            } else {
                mConfirmAndSave.setEnabled(false);
                mConfirmAndSave.setBackgroundColor(getResources().getColor(R.color.new_login_confirm_bg));
            }
        }
    }

    private void addCheckBoxListener() {
        mAllStoreSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @OnClick({R.id.all_store_switch, R.id.rl_group_store_layout, R.id.confirm_and_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all_store_switch:
                break;
            case R.id.rl_group_store_layout:
                break;
            case R.id.confirm_and_save:
                List<StoreMsgEntity> dataList = mAdapter.getData();
                //全部按钮选中,所有店铺被选中
                if (switchAllStoreState) {
                    //全部开关打开，传递数
                    for (int i = 0; i < dataList.size(); i++) {
                        mSelectStoreDataList.add(String.valueOf(dataList.get(i).getStoreId()));//选中的店铺放到选中店铺的集合中
                    }
                } else {
                    for (int i = 0; i < dataList.size(); i++) {
                        boolean check = dataList.get(i).isCheck();
                        if (check) {
                            mSelectStoreDataList.add(String.valueOf(dataList.get(i).getStoreId()));//选中的店铺放到选中店铺的集合中
                        }
                    }
                }
                returnData(mSelectStoreDataList);//关闭当前界面并返回数据
                break;
        }
    }

    private void returnData(ArrayList<String> mSelectStoreDataList) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectStoreNumber", mSelectStoreDataList.size());
        resultIntent.putExtra("switchAllStoreState", switchAllStoreState);
        resultIntent.putStringArrayListExtra("mSelectStoreDataList", mSelectStoreDataList);
        setResult(RESULT_OK, resultIntent);
        finish();
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
        if (dataList == null) return;
        if (is_all) { //所有门店
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setCheck(false);
            }
            mAdapter.setNewData(dataList);
        }
        mAdapter.notifyDataSetChanged();
        showLastSelect();//回显选择数据
    }

    private void showLastSelect() {
        if (!TextUtils.isEmpty(mStoreIds) && mStoreIds.length() > 0) {
            List<StoreMsgEntity> mDataList = mAdapter.getData();
            for (int i = 0; i < mDataList.size(); i++) {
                StoreMsgEntity storeMsgEntity = mDataList.get(i);
                if (mStoreIds.contains(String.valueOf(storeMsgEntity.getStoreId()))) {
                    mDataList.get(i).setCheck(true);
//                    mConfirmAndSave.setEnabled(true);
//                    mConfirmAndSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
                } else {
                    mDataList.get(i).setCheck(false);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getMyStoreInfoFailed() {

    }

    @Override
    public void showNoData() {


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String tag = (String) buttonView.getTag();
        if (TextUtils.isEmpty(tag)) return;
        if (tag.equals("belongStore")) {
            switchAllStoreState = isChecked;
            if (isChecked) {
                mRecycleView.setVisibility(View.GONE);
                mConfirmAndSave.setEnabled(true);
                mConfirmAndSave.setBackgroundColor(getResources().getColor(R.color.text_color_blue_6));
            } else {
                mRecycleView.setVisibility(View.VISIBLE);
                updateConfirmState();
            }
        }
    }
}
