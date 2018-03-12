package com.zishan.sardinemerchant.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.presenter.base.BPresenter;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.logins.NewPswLoginActivity;

import top.ftas.ftasbase.common.util.StatusBarUtil;

/**
 * 启动页
 * Created by wislie on 2017/11/29.
 */

public class StartActivity extends BaseFragmentActivity {
    @Override
    protected BPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initContentView() {
        //设置状态栏
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                String token = UserConfig.getInstance(ClientApplication.getApp())
                        .getAccessToken();
                Long storeId = UserConfig.getInstance(ClientApplication.getApp()).getStoreId();
                if (TextUtils.isEmpty(token) || storeId == null || storeId.longValue() == 0) {
                    startActivity(new Intent(StartActivity.this, NewPswLoginActivity.class));
                } else {
                    startActivity(new Intent(StartActivity.this, MainPageActivity.class));
                }
                finish();
                Log.e("wislie", "启动 StartActivity");
            }
        }, 3000);
    }
}
