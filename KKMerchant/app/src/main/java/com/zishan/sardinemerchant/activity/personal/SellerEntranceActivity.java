package com.zishan.sardinemerchant.activity.personal;

import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseWebActivity;

import butterknife.BindView;

/**
 * 商家入驻
 * Created by wislie on 2017/12/13.
 */

public class SellerEntranceActivity extends BaseWebActivity {

    @BindView(R.id.webview_container)
    LinearLayout mContainer;
    //商家入驻url
    private final String business_enter_url = "https://h5.jie365.cn/sardine/portal/settled/";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seller_entrance;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.seller_entrance));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
        setActionBarDivderVisible(false);
    }

    @Override
    protected void initContentView() {
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
        syncCookie(this, business_enter_url);
        webview.loadUrl(business_enter_url + "?shopid=" +
                UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }


}
