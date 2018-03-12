package com.zishan.sardinemerchant.activity;

import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.hg.ftas.util.ToastUtil;
import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;

import butterknife.BindView;
import butterknife.OnClick;
import top.ftas.ftasbase.eventbus.BaseEvent;

/**
 * bridge测试
 * Created by wislie on 2018/2/10.
 */

public class BridgeTestActivity extends BaseWebActivity {
    @BindView(R.id.webview_container)
    LinearLayout mContainer;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;

    @BindView(R.id.hide_btn)
    Button mHideBtn;

    @BindView(R.id.content)
    TextView mContentText;
    //默认是隐藏的
    private boolean isHided = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bridge_test;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle("测试");
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

    }

    @OnClick({R.id.search_btn, R.id.hide_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                loadUrl();
                break;
            //显示或隐藏
            case R.id.hide_btn:
                isHided = !isHided;
                if(isHided){
                    mContentText.setVisibility(View.GONE);
                    mContainer.setVisibility(View.VISIBLE);
                }else{
                    mContentText.setVisibility(View.VISIBLE);
                    mContainer.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void loadUrl() {
        String url = mSearchEdit.getText().toString();
        if(!Patterns.WEB_URL.matcher(url).matches() ){
            ToastUtil.show("不合法的url");
            return;
        }
        syncCookie(this, url);
        getCurrentWebView().loadUrl(url + "?shopid=" +
                UserConfig.getInstance(ClientApplication.getApp()).getStoreId());
    }

    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
    }
}
