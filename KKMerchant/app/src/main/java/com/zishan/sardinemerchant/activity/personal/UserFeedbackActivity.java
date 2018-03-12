package com.zishan.sardinemerchant.activity.personal;

import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseWebActivity;

import butterknife.BindView;

/**
 * 用户信息反馈
 * Created by wislie on 2018/2/2.
 */

public class UserFeedbackActivity extends BaseWebActivity {
    @BindView(R.id.webview_container)
    LinearLayout mContainer;
//    private String url = "https://www.wenjuan.in/s/yyEFnm6";
    private String url = "https://h5.jie365.cn/jsridge/index.html";
//    private String url = "https://h5.jie365.cn/sardine/portal/settled/";


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seller_entrance;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.suggest_feedback));
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        setActionBarMenuIcon(-1);
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
        syncCookie(this, url);
        webview.loadUrl(url);
    }


}
