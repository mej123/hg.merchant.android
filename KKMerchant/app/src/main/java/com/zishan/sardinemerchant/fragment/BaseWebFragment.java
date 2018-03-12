package com.zishan.sardinemerchant.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.wislie.rxjava.UserConfig;
import com.hg.ftas.event.impl.FtasWebViewEvent;
import com.hg.ftas.frame.FtasApplication;
import com.hg.ftas.plugin.IFtasPlugin;
import com.hg.ftas.util.OpenPageUtil;
import com.hg.ftas.view.AbstractFtasWebView;
import com.hg.ftas.view.FtasWebViewManager;
import com.hg.ftas.view.IFtasView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;


/**
 * web fragment基类
 * Created by wislie on 2017/12/1.
 */

public abstract class BaseWebFragment extends BFragment implements IFtasView {

    private WebSettings mWebSettings;
    protected AbstractFtasWebView mWebView;
    private IFtasPlugin callbackPlugin;

    private ProgressBar mProgressBar;

    protected FtasWebViewManager mFtasWebViewManager;

    @Override
    protected void initBizView() {
        FtasApplication.getInstance().setCurrentFtasView(this);
        mFtasWebViewManager = new FtasWebViewManager(this);

        initWebView();
    }


    private void initWebView() {
        mWebView = mFtasWebViewManager.getFtasWebView(new FtasWebViewEvent(this));
        LinearLayout.LayoutParams webViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        mWebView.setLayoutParams(webViewParams);

        addWebView(mWebView);

        setCurrentWebView(mWebView);
        mWebSettings = mWebView.getSettings();
        // 设置 缓存模式
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        mWebSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        mWebSettings.setDatabaseEnabled(true);
        // 支持javascript
        mWebSettings.setJavaScriptEnabled(true);

        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setAllowFileAccess(true);
        // 扩大比例的缩放
        mWebSettings.setUseWideViewPort(true);
        // 自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setLoadWithOverviewMode(true);
        //开启DomStorage缓存
        mWebSettings.setLoadsImagesAutomatically(true);
//        String ua = mWebSettings.getUserAgentString();
//        mWebSettings.setUserAgentString(ua + "; KuKuJsBridge/1");
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebChromeClient(new WebChromeClient());
        addProgressBar();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWebView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) mProgressBar.getLayoutParams();
                    lp.x = scrollX;
                    lp.y = scrollY;
                    mProgressBar.setLayoutParams(lp);
                }
            });
        }
        loadUrlByWebView(mWebView);
    }

    private void addProgressBar(){
        mProgressBar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 5);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = getResources().getDrawable(
                R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        mWebView.addView(mProgressBar);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE)
                    mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    //保存cookie并通过cookie登录
    protected void syncCookie(Context context, String url) {
        try {
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
//            String oldCookie = cookieManager.getCookie(url);
            String cookieString = "_atk=" + UserConfig.getInstance(ClientApplication.getApp()).getAccessToken()
                    + "; domain=tenv.mttstudio.net"
                    + "; path=/";
            cookieManager.setCookie(url, cookieString);
            CookieSyncManager.getInstance().sync();
//            String newCookie = cookieManager.getCookie(url);
        } catch (Exception e) {
        }
    }

    @Override
    public void close() {
        this.finish();
        this.free();
    }



    @Override
    public void free() {
    }

    @Override
    public AbstractFtasWebView getCurrentWebView() {
        return this.mWebView;
    }

    @Override
    public void setCurrentWebView(AbstractFtasWebView currentWebView) {
        this.mWebView = currentWebView;
    }

    @Override
    public void nextPageWithUrl(String url, boolean isAnimation) {
//        Log.e("wislie","nextPageWithUrl url:"+url);
        OpenPageUtil.startUrl(url, this);
    }

    @Override
    public FtasWebViewManager getWebViewManager() {
        return this.mFtasWebViewManager;
    }

    @Override
    public void back() {
        this.finish();
    }

    @Override
    public void startActivityForResult(IFtasPlugin ftasPlugin, Intent intent, int requestCode) {
        this.callbackPlugin = ftasPlugin;
        startActivityForResult(intent, requestCode);
    }


    //添加webview
    protected abstract void addWebView(AbstractFtasWebView webview);

    protected abstract void loadUrlByWebView(AbstractFtasWebView webview);


  /*  //加载url
    protected void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) return;
        isError = false;
        mWebView.loadUrl(Uri.parse(url).toString());
        mWebView.clearHistory();

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("wislie", "onPageStarted url:" + url);
                //showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("wislie", "onPageFinished url:" + url);
                //dismissLoadingDialog();
                if (mWebViewListener != null) {
                    if (!isError) {
                        mWebViewListener.loadSuccess();
                    }
                }

                *//*if (url.contains("detail")) {
                    OpenPageUtil.startUrl("https://h5.tenv.mttstudio.net/bridge/index.html", BaseWebFragment.this);
                }*//*
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError = true;
                if (mWebViewListener != null) {
                    mWebViewListener.loadFailed();
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Uri uri = request.getUrl();
                    String url = uri.toString();

                    if (url.contains("tel://")) {
//                        String mobile = url.substring(url.lastIndexOf("/") + 1);
//                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        return true;

                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }
*/

/*    //获取webview
    protected abstract WebView getWebView();


    public void setWebViewListener(WebViewListener webViewListener) {
        this.mWebViewListener = webViewListener;
    }

    public interface WebViewListener {

        void loadSuccess();

        void loadFailed();

    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearFormData();
            mWebView.clearCache(true);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
//        mAdditionalHttpHeaders.clear();
    }
}
