package com.zishan.sardinemerchant.activity.personal.store_msg;

import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.example.wislie.rxjava.UserConfig;
import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BaseWebActivity;

import butterknife.BindView;

/**
 * 商家预览
 * Created by wislie on 2017/12/15.
 */

public class SellerPreviewActivity extends BaseWebActivity {

    @BindView(R.id.webview_container)
    LinearLayout mContainer;
    //商家入驻url
    private final String business_preview_url = "https://h5.tenv.mttstudio.net/sardine/merchant/#/";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seller_entrance;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle(getString(R.string.seller_preview));
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
        webview.loadRemoteUrl(business_preview_url + "?id=" +
                UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }
}
