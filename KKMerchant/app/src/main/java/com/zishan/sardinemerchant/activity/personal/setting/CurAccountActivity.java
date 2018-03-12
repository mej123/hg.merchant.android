package com.zishan.sardinemerchant.activity.personal.setting;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.dialog.ConfirmOrCancelDialog;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StringUtil;
import top.ftas.ftaswidget.dialog.BaseDialogFragment;

/**
 * Created by yang on 2017/10/21
 * <p>
 * 当前账号
 */

public class CurAccountActivity extends BActivity {

    @BindView(R.id.mobile)
    TextView mMobileText;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.current_account));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    protected void initContentView() {
        mMobileText.setText(StringUtil.formatPhoneNum(
                UserConfig.getInstance(ClientApplication.getApp()).getUserMobile()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMobileText.setText(StringUtil.formatPhoneNum(
                UserConfig.getInstance(ClientApplication.getApp()).getUserMobile()));
    }



    @OnClick({R.id.bind_mobile_layout, R.id.account_safety_layout,
            R.id.login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bind_mobile_layout://绑定手机
                Intent hasBindPhoneIntent = new Intent(this, MobileAlreadyBindActivity.class);
                startActivity(hasBindPhoneIntent);
                break;
            case R.id.account_safety_layout://账户安全
                Intent mobileVerifyIntent = new Intent(this, MobileVerifyActivity.class);
                startActivity(mobileVerifyIntent);
                break;
            case R.id.login_out://退出登录

                ConfirmOrCancelDialog dialog = ConfirmOrCancelDialog.newInstance("确认退出登录?", null);
                dialog.showDialog(getFragmentManager());
                dialog.setOnDialogListener(new BaseDialogFragment.DialogListener() {
                    @Override
                    public void onConfirm() {
                        reOnLogin();
                    }

                    @Override
                    public void onInputConfirm(String... values) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }


}
