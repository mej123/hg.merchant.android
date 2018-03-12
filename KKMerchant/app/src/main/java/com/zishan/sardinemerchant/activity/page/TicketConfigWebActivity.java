package com.zishan.sardinemerchant.activity.page;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.hg.ftas.util.AssetsUtil;
import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseWebActivity;

import butterknife.BindView;

/**
 * 用户信息反馈
 * Created by wislie on 2018/2/2.
 */

public class TicketConfigWebActivity extends BaseWebActivity {
    @BindView(R.id.webview_container)
    LinearLayout mContainer;
//    private String url = "http://192.168.0.160:7533/coupons/index.html#/add?mchid=13706";

    private String url = "file:///android_asset/coupons/index.html#/add?mchid=13693";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seller_entrance;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
    }

    @Override
    protected void initContentView() {

//        String content = AssetsUtil.getFromAssets(this,"coupons/index.html");
//        Log.e("wislie","content:"+content);

        initWebView();
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

    @Override
    protected void addWebView(AbstractFtasWebView webview) {
        mContainer.addView(webview);
        setCurrentWebView(webview);
    }

    @Override
    protected void loadUrlByWebView(AbstractFtasWebView webview) {

//        String path = url + "#/add"+"?mchid=" +
//                UserConfig.getInstance(ClientApplication.getApp()).getMerchantId();
        Log.e("wislie","my merchantid:"+ UserConfig.getInstance(ClientApplication.getApp()).getMerchantId());
//        syncCookie(this, path);
        webview.loadUrl(url);
    }


}
