package com.zishan.sardinemerchant.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.ImagesLoadEntity;
import com.example.wislie.rxjava.model.store.ImageEnitity;
import com.example.wislie.rxjava.presenter.base.store.goods.ImageUploadPresenter;
import com.example.wislie.rxjava.view.base.store.goods.ImageUploadView;
import com.hg.ftas.event.impl.FtasWebViewEvent;
import com.hg.ftas.func2.func.MobileGetImages;
import com.hg.ftas.func2.func.MobileUploadImages;
import com.hg.ftas.plugin.FtasPlugin;
import com.hg.ftas.upyun.UpyunListener;
import com.hg.ftas.upyun.UpyunManager;
import com.hg.ftas.util.LGImgCompressor;
import com.hg.ftas.util.ToastUtil;
import com.hg.ftas.view.AbstractFtasWebView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.LoadingUrlActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.upyun.library.utils.UpYunUtils;
import com.zishan.sardinemerchant.utils.PhotoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.ftas.ftasbase.common.util.ApkVersionCodeUtils;
import top.ftas.ftasbase.eventbus.BaseEvent;

import static com.hg.ftas.func2.func.MobileGetImages.REQUEST_GetImages;


/**
 * 访问web的基类
 * Created by wislie on 2017/12/13.
 */

public abstract class BaseWebActivity extends BActivity<ImageUploadView, ImageUploadPresenter> implements ImageUploadView,
        PhotoManager.PhotoListener{ //LGImgCompressor.CompressListener
    private WebSettings mWebSettings;

    protected AbstractFtasWebView mWebView;

    private ProgressBar mProgressBar;

    private FtasPlugin mFtasPlugin;

    @Override
    protected ImageUploadPresenter createPresenter() {
        return new ImageUploadPresenter(this, this);
    }

    protected void initWebView() {
        mWebView = mFtasWebViewManager.getFtasWebView(new FtasWebViewEvent(this));
        LinearLayout.LayoutParams webViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        mWebView.setLayoutParams(webViewParams);
        addWebView(mWebView);

        mWebSettings = mWebView.getSettings();
        // 设置 缓存模式
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        mWebSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        mWebSettings.setDatabaseEnabled(true);
        // 支持javascript
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");//设置字符编码
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setAllowFileAccess(true);
        // 扩大比例的缩放
        mWebSettings.setUseWideViewPort(true);
        // 自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebView.requestFocus(View.FOCUS_DOWN);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
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

        UpyunManager.getInstance().setUpyunListener(new UpyunListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void callBack(String localId, boolean isSuccess) {

                if (mFtasPlugin != null) {
                    mLocalIds.add(localId);
                    if (mLocalIds.size() == 1) {
                        dismissProgressDialog();

                        try {
                            JSONArray resultArray = new JSONArray();
                            for (int i = 0; i < mLocalIds.size(); i++) {
                                String json = mLocalIds.get(i);
                                resultArray.put(uploadYun(json, true));
                            }
                            JSONObject json = new JSONObject();
                            json.put("successCount", resultArray.length());
                            json.put("failCount", 0);
                            json.put("results", resultArray);

                            mFtasPlugin.callback(json.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        PhotoManager.getInstance().initImagePicker();
        PhotoManager.getInstance().setPhotoListener(this);
    }

    //上传完成一张图片 即生成一个 json对象
    private JSONObject uploadYun(String localId, boolean isSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("localId", localId);
            if (isSuccess) {
                jsonObject.put("status", "success");
                jsonObject.put("serverId", "gggsddgggaaaaa");
            } else {

                jsonObject.put("status", "error");
                jsonObject.put("errorCode", 123455);
            }
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    private void addProgressBar() {
        mProgressBar = new ProgressBar(this, null,
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
            String cookieString = "_atk=" + UserConfig.getInstance(ClientApplication.getApp()).getAccessToken()
                    + "; domain=tenv.mttstudio.net"
                    + "; path=/";
            cookieManager.setCookie(url, cookieString);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearFormData();
            mWebView.clearCache(true);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().compressImage(this, requestCode, resultCode, data);
    }


    //添加webview
    protected abstract void addWebView(AbstractFtasWebView webview);

    protected abstract void loadUrlByWebView(AbstractFtasWebView webview);

    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }

    @Override
    public void uploadImageSuccess(ImagesLoadEntity data, String filePath) {
        //上传图片到又拍云
        UpyunManager.getInstance().uploadYun(new File(filePath), data.getProviderUploadParamString());

    }

    @Override
    public void uploadImageFailed() {

    }

  /*  @Override
    public void onCompressStart() {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult, int uploadSize) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR) { //压缩失败
            return;
        }
       *//* *//*
    }*/

    @Override
    public void compressFinish(LGImgCompressor.CompressResult compressResult, int uploadSize) {
       /* File imageFile = new File(compressResult.getOutPath());
        if (imageFile != null && imageFile.exists()) {
            ImageEnitity compressedEntity = new ImageEnitity(0, imageFile.getName(), imageFile.getPath(), true,
                    imageFile.length(), UpYunUtils.md5(imageFile, Constant.BLOCK_SIZE));
            //上传图片
            mPresenter.uploadImage(1, compressedEntity.getName(),
                    compressedEntity.getFileMd5(), compressedEntity.getLen(), compressedEntity.getPath());
        }*/
        if (mFtasPlugin != null) {
            mLocalIds.add(compressResult.getOutPath());
            if (mLocalIds.size() == uploadSize) {
                getImages();
            }
        }

    }

    private List<String> mLocalIds = new ArrayList<>();

    private void getImages() {

        JSONArray array = new JSONArray();
        for (int i = 0; i < mLocalIds.size(); i++) {
            array.put(mLocalIds.get(i));
        }

        JSONObject json = new JSONObject();
        try {
            json.put("localIds", array);
           String js =  json.toString();
            mFtasPlugin.callback(js);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageEventMain(BaseEvent baseEvent) {
        super.onMessageEventMain(baseEvent);
        if (baseEvent.getTagString().equals(ACTION_NAME)) { //从本地获取图片
            int tagInt = baseEvent.getTagInt();
            if (tagInt == MobileGetImages.REQUEST_GetImages) {
                Bundle data = (Bundle) baseEvent.getData();
                mFtasPlugin = (FtasPlugin) data.getSerializable("ftas_plugin");
            }else if(tagInt == MobileUploadImages.REQUEST_UploadImages){ //上传图片
                Bundle data = (Bundle) baseEvent.getData();
                String localIds = data.getString("localIds");
                mLocalIds.clear();
                uploadImages(localIds);
            }
        }

    }

    private void uploadImages(String localIds){
        JSONArray array = null;
        try {
            array = new JSONArray(localIds);
            for(int i = 0; i < array.length(); i++){
                File imageFile = new File(array.getString(i));
                if (imageFile != null && imageFile.exists()) {
                    //int imageId, String name, String path, boolean isSelected, long len, String fileMd5
                    ImageEnitity compressedEntity = new ImageEnitity(0, imageFile.getName(), imageFile.getPath(), true,
                            imageFile.length(), UpYunUtils.md5(imageFile, 1024 * 1024)); //MD5Util.getMd5ByFile(imageFile)
                    //上传图片
                    mPresenter.uploadImage(1, compressedEntity.getName(),
                            compressedEntity.getFileMd5(), compressedEntity.getLen(), compressedEntity.getPath());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
