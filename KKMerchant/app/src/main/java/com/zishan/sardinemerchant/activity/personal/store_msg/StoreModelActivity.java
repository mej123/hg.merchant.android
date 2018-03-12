package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.personal.StoreModelPresenter;
import com.example.wislie.rxjava.util.ToastUtil;
import com.example.wislie.rxjava.view.base.personal.StoreModelView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 经营模式
 * Created by wislie on 2017/11/28.
 */

public class StoreModelActivity extends BActivity<StoreModelView, StoreModelPresenter>
        implements StoreModelView {

    @BindView(R.id.pay_before_eat_icon)
    ImageView mPayBeforeEatIcon;
    @BindView(R.id.eat_before_pay_icon)
    ImageView mEatBeforePayIcon;
    private boolean isEatFirst;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_model;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.manage_model));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected StoreModelPresenter createPresenter() {
        return new StoreModelPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        isEatFirst = UserConfig.getInstance(ClientApplication.getApp()).isEatFirst();
        updateModel();
    }

    //更新模式
    private void updateModel() {
        if (isEatFirst) {
            mEatBeforePayIcon.setSelected(true);
            mPayBeforeEatIcon.setSelected(false);
        } else {
            mEatBeforePayIcon.setSelected(false);
            mPayBeforeEatIcon.setSelected(true);
        }
    }

    @OnClick({R.id.confirm_store_model, R.id.pay_before_eat_layout, R.id.eat_before_pay_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //先吃后付
            case R.id.eat_before_pay_layout:
                isEatFirst = true;
                updateModel();
                break;
            //先付后吃
            case R.id.pay_before_eat_layout:
                isEatFirst = false;
                updateModel();
                break;
            //确定
            case R.id.confirm_store_model:
                int run_model = isEatFirst ? 1 : 2;
                mPresenter.changeStoreModel(UserConfig.getInstance(this).getStoreId(), run_model, isEatFirst);
                break;
        }
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
    public void settingStoreModelSuccess(String data, boolean is_eat_first) {
        UserConfig.getInstance(ClientApplication.getApp()).setIsEatFrist(is_eat_first);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        ToastUtil.show(this,"保存成功");
        finish();
    }

    @Override
    public void settingStoreModelFailed() {

    }
}
