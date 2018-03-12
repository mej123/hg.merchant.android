package com.zishan.sardinemerchant.activity.personal.setting;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.UserAccountEntity;
import com.example.wislie.rxjava.presenter.base.personal.SwitchIdentityPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.personal.SwitchIdentityView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.activity.personal.SellerEntranceActivity;
import com.zishan.sardinemerchant.adapter.personal.SwitchUserAdapter;
import com.zishan.sardinemerchant.utils.AccountUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;
import top.ftas.ftaswidget.view.UnderlineTextView;

/**
 * 切换身份
 * Created by wislie on 2017/11/28.
 */

public class SwitchUserActivity extends BActivity<SwitchIdentityView, SwitchIdentityPresenter>
        implements SwitchIdentityView {

    @BindView(R.id.identity_springview)
    SpringView mSpringView;
    @BindView(R.id.identity_recycler_view)
    RecyclerView mRecycler;

    @BindView(R.id.become_seller)
    UnderlineTextView mUnderlineText;

    private SwitchUserAdapter mAdapter;

    private List<UserAccountEntity> mDataList = new ArrayList<>();

    @Override
    protected SwitchIdentityPresenter createPresenter() {
        return new SwitchIdentityPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_switch_user;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.switch_user));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SwitchUserAdapter(R.layout.item_switch_user, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isChecked(position)) {
                    finish();
                    return;
                }
                setChecked(position);
                finish();
            }
        });
        showProgressDialog();
        loadData();
    }

    //是否选中状态
    private boolean isChecked(int position) {
        List<UserAccountEntity> dataList = mAdapter.getData();
        UserAccountEntity data = dataList.get(position);
        if (data.getSelected() != null && data.getSelected() == Boolean.TRUE) {
            return true;
        }
        return false;
    }

    //设置选中
    private void setChecked(int position) {
        List<UserAccountEntity> dataList = mAdapter.getData();
        for (int i = 0; i < dataList.size(); i++) {
            UserAccountEntity data = dataList.get(i);
            if (data.getSelected() != null && data.getSelected() == Boolean.TRUE
                    && position != i) {
                data.setSelected(false);
            }
        }
        UserAccountEntity data = dataList.get(position);
        data.setSelected(true);
        AccountUtil.saveAccount(data);
        mAdapter.notifyDataSetChanged();
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
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {


            }
        });
    }

    private void loadData() {
        mPresenter.getIdentityList(UserConfig.getInstance(ClientApplication.getApp()).getAccountId());
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
    public void showNoData() {

    }

    @Override
    public void getUserAccountSuccess(List<UserAccountEntity> dataList) {
        mAdapter.setNewData(rankUserAccountList(dataList));
        finishRefreshAndLoad();
        boolean isSeller = isSeller(dataList);
        mUnderlineText.setVisibility(isSeller ? View.GONE : View.VISIBLE);
    }

    //降序
    private List<UserAccountEntity> rankUserAccountList(List<UserAccountEntity> dataList) {
        List<UserAccountEntity> accountList = new ArrayList<>();

        int index = 0;
        for (int i = 0; i < dataList.size(); i++) {
            UserAccountEntity data = dataList.get(i);
            long employee_id = UserConfig.getInstance(ClientApplication.getApp()).getEmployeeId();
            if (employee_id == data.getEmployeeId().longValue()) {
//                Log.e("wislie", "我的employee_id:" + employee_id);
                accountList.add(data);
                data.setSelected(true);
                index = i;
                break;
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            UserAccountEntity data = dataList.get(i);
            if (i != index) {
                data.setSelected(false);
                accountList.add(data);
            }
        }

        return accountList;
    }


    @Override
    public void getUserAccountFailed() {
        finishRefreshAndLoad();
    }

    //判断是否是商家
    private boolean isSeller(List<UserAccountEntity> dataList) {
        boolean isSeller = false;
        for (int i = 0; i < dataList.size(); i++) {
            UserAccountEntity data = dataList.get(i);
            if (data.getOwner() != null && data.getOwner() == Boolean.TRUE) {
                isSeller = true;
                break;
            }
        }
        return isSeller;
    }

    @OnClick({R.id.become_seller})
    public void onClick(View view) {
        switch (view.getId()) {
            //商家入驻
            case R.id.become_seller:
                Intent intent = new Intent(this, SellerEntranceActivity.class);
                startActivity(intent);
                break;
        }
    }
}
