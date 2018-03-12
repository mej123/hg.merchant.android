package com.zishan.sardinemerchant;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;

import com.hg.ftas.frame.FtasApplication;
import com.hg.ftas.util.CommonUtil;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.easeui.UIProvider;

import top.ftas.ftasbase.common.util.DensityUtil;


public class ClientApplication extends FtasApplication {
    Thread.UncaughtExceptionHandler mDefaultHandler;
    Thread.UncaughtExceptionHandler mHandler;

    public static ClientApplication myApplication;

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
        config.actionBarConfig.titleSize = DensityUtil.dip2px(this, 20);
        config.actionBarConfig.height = CommonUtil.getActionBarHeight(this, 48);
        config.actionBarConfig.backResIconId = R.mipmap.back_icon;
        config.uiConfig.bodyBackgroundColor = Color.parseColor("#0000ff");
        config.uiConfig.clearIconId = R.drawable.kk_personalinfo_name_delete;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;


        ///////////////////////////////////////////////  环信  //////////////////////////////////////////////////

        ChatClient.Options options = new ChatClient.Options();
        options.setAppkey("1479171114061842#kefuchannelapp49811");//必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
        options.setTenantId("49811");//必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”

        // Kefu SDK 初始化
        if (!ChatClient.getInstance().init(this, options)){
            return;
        }
        // Kefu EaseUI的初始化
        UIProvider.getInstance().init(this);
    }

    public static Context getApp() {
        return myApplication;
    }
}