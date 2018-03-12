package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseWebActivity;

import butterknife.BindView;

/**
 * 加载网页链接url
 * Created by wislie on 2018/1/19.
 */

public class LoadingUrlActivity extends BaseWebActivity {

    private String url;
    @BindView(R.id.webview_container)
    LinearLayout mContainer;

    @Override
    protected void addWebView(AbstractFtasWebView webview) {
        mContainer.addView(webview);
        setCurrentWebView(webview);
    }

    @Override
    protected void loadUrlByWebView(AbstractFtasWebView webview) {

        if(!TextUtils.isEmpty(url) && webview != null){
            syncCookie(this, url);
            getCurrentWebView().loadUrl(url );
        }
    }

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
        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("url");
        }
        initWebView();

    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }
}
