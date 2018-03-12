package com.zishan.sardinemerchant.activity.page;

import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseWebActivity;

import butterknife.BindView;

/**
 * 经营数据
 * Created by wislie on 2018/1/3.
 */

public class ChartActivity extends BaseWebActivity {
    @BindView(R.id.webview_container)
    LinearLayout mContainer;
    private String url = "https://h5.tenv.mttstudio.net/sardinemch/dashboard/index.html";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seller_entrance;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.manage_data));
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
        webview.loadUrl(url);
    }
}
