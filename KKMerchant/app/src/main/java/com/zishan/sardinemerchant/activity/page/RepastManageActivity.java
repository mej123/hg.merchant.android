package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.UserAccountEntity;
import com.example.wislie.rxjava.model.page.GroupItem;
import com.example.wislie.rxjava.model.page.OpenStoresNumEntity;
import com.example.wislie.rxjava.model.personal.StoreMsgEntity;
import com.example.wislie.rxjava.presenter.base.page.main.MainPagePresenter;
import com.example.wislie.rxjava.view.base.page.MainPageView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.EmptyFragment;
import com.zishan.sardinemerchant.activity.personal.store_msg.StoreModelActivity;
import com.zishan.sardinemerchant.fragment.BFragment;
import com.zishan.sardinemerchant.fragment.page.MainPageEatBeforePayFragment;
import com.zishan.sardinemerchant.fragment.page.MainPagePayBeforeEatFragment;

import java.util.List;

/**
 * 餐饮管理
 * Created by wislie on 2017/12/29.
 */

public class RepastManageActivity extends BActivity<MainPageView, MainPagePresenter>
        implements MainPageView {

    private MainPageEatBeforePayFragment mEatFragment;
    private MainPagePayBeforeEatFragment mPayFragment;
    private BFragment mCurrentFragment;

    @Override
    protected MainPagePresenter createPresenter() {
        return new MainPagePresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_repast_manage;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.repast_manage));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(R.mipmap.run_model_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent storeModelIntent = new Intent(
                        RepastManageActivity.this, RepastModelActivity.class); //StoreModelActivity
                startActivity(storeModelIntent);
            }
        });
    }

    @Override
    protected void initContentView() {
        mCurrentFragment = EmptyFragment.newInstance();
        mEatFragment = MainPageEatBeforePayFragment.newInstance();
        mPayFragment = MainPagePayBeforeEatFragment.newInstance();
    }


    private void loadData() {
        String accessToken = UserConfig.getInstance(ClientApplication.getApp()).getAccessToken();
        if (TextUtils.isEmpty(accessToken)) {
            return;
        }
        Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
        if (storeId == null || storeId.longValue() == 0) {
            mPresenter.getUserAccount(UserConfig.getInstance(ClientApplication.getApp()).getAccountId());
        } else {
            mPresenter.getStoreMsg(storeId);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }


    @Override
    public void getUserAccountSuccess(List<UserAccountEntity> dataList) { //通过access_token请求得到的返回结果

        if (dataList == null || dataList.size() == 0) {
            return;
        }

        UserAccountEntity data = dataList.get(0);
        if (data != null) {
            long storeId = data.getStoreId();
            //保存storeid
            UserConfig.getInstance(ClientApplication.getApp()).setStoreId(storeId);
            //保存 员工id
            UserConfig.getInstance(ClientApplication.getApp()).setEmployeeId(data.getEmployeeId());
            //保存角色id
            UserConfig.getInstance(ClientApplication.getApp()).setRoleId(data.getRoleId());
            //保存商店名称
            UserConfig.getInstance(ClientApplication.getApp()).setStoreName(data.getStoreName());

            //设置权限列表
            setPermissionList(data.getPermissionGroupDTO());

            mPresenter.getStoreMsg(storeId);
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
    public void getUserAccountFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void getStoreMsgSuccess(StoreMsgEntity data) {
        if (data == null) return;
//        UserConfig.getInstance(ClientApplication.getApp()).setMerchantId(data.getMerchantId());//商户id
        //设置头像
//        UserConfig.getInstance(ClientApplication.getApp()).setPersonalAvatar(data.getLogoPicUrl());
        int runModel = data.getRunModel();


        //运行模式(1:先吃后付,2:先付后吃)
        if (runModel == 1) {
            //先吃后付
            UserConfig.getInstance(ClientApplication.getApp()).setIsEatFrist(true);
            switchContent(mCurrentFragment, mEatFragment);

        } else if (runModel == 2) {
            //先付后吃
            UserConfig.getInstance(ClientApplication.getApp()).setIsEatFrist(false);
            switchContent(mCurrentFragment, mPayFragment);
        }
    }


    //切换fragment
    public void switchContent(BFragment from, BFragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            if (this == null) return;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                // 隐藏当前的fragment，add下一个到Activity中
                ft.hide(from).add(R.id.fragment_page_container, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                ft.hide(from).show(to).commit();
            }
        }
    }

    @Override
    public void getStoreMsgFailed() {

    }

    @Override
    public void getGroupToolsSuccess(List<GroupItem> dataList) {

    }

    @Override
    public void getGroupToolsFailed() {

    }

    @Override
    public void getOpenStoresNumSuccess(OpenStoresNumEntity data) {

    }

    @Override
    public void getOpenStoresNumFailed() {

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
