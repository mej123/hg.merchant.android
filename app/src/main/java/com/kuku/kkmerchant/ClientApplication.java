package com.kuku.kkmerchant;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.hg.ftas.frame.FtasApplication;
import com.hg.ftas.util.CommonUtil;

import top.ftas.ftasbase.common.util.DensityUtil;

public class ClientApplication extends FtasApplication {
    Thread.UncaughtExceptionHandler mDefaultHandler;
    Thread.UncaughtExceptionHandler mHandler;

    private static ClientApplication myApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //开启apk拆包
//		MultiDex.install(this);
    }

    @Override
    protected void initConfig(MobileConfig config) {
        config.hotFixAppId = "81280-1";
        config.bugTagsAppKey = "24c104fb86a95a93d7373cbdba467909";


        Thread.setDefaultUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
        config.actionBarConfig.backgroundColor = ContextCompat.getColor(this, R.color.colorPrimary);
        config.actionBarConfig.titleColor = Color.parseColor("#000000");
        config.actionBarConfig.titleSize = DensityUtil.dip2px(this,20);
        config.actionBarConfig.height = CommonUtil.getActionBarHeight(this,48);
        config.actionBarConfig.backResIconId = R.drawable.kk_common_back;
        config.uiConfig.bodyBackgroundColor = Color.parseColor("#0000ff");
        config.uiConfig.clearIconId = R.drawable.kk_personalinfo_name_delete;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static Context getApp() {
        return myApplication;
    }
}