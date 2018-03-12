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

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.common.util.StringUtil;

/**
 * 已绑定手机号
 * Created by wislie on 2017/11/29.
 */

public class MobileAlreadyBindActivity extends BActivity {

    @BindView(R.id.mobile_phone)
    TextView mBindPhoneText;

    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mobile_already_bind;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.bind_mobile));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

        mBindPhoneText.setText(StringUtil.formatPhoneNum(
                UserConfig.getInstance(ClientApplication.getApp()).getUserMobile()));
    }

    @OnClick({R.id.change_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            //更换手机号
            case R.id.change_phone:
                Intent intent = new Intent(this, OldMobileChangeBindActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
