package com.zishan.sardinemerchant.bridge;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.hg.ftas.func2.func.dialog.PhotoDialog;
import com.hg.ftas.util.LGImgCompressor;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zishan.sardinemerchant.Constant;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.page.LoadingUrlActivity;
import com.zishan.sardinemerchant.utils.BitmapUtil;
import com.zishan.sardinemerchant.utils.PhotoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by wislie on 2018/3/9.
 */

public class BridgeTest extends AppCompatActivity implements PhotoManager.PhotoListener {
    //BaseWebActivity实现SwitchIdentityPresenter的功能

    private WebSettings mWebSettings;
    private WebView webView;
    private List<String> mLocalIdList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        webView = (WebView) findViewById(R.id.webview);

        mWebSettings = webView.getSettings();
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


        // 判断系统版本是不是5.0或之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //让系统不屏蔽混合内容和第三方Cookie
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 不支持缩放
        mWebSettings.setSupportZoom(false);

        // 自适应屏幕大小
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);

        webView.requestFocus(View.FOCUS_DOWN);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebViewClient(new BridgeWebViewClient(webView)); //这样js就会跳出来
        String url = "https://h5.jie365.cn/jsridge/index.html";
        JsInvokeNativeMethods jsInvokeNativeMethods = JsInvokeNativeMethods.getInstance(new JSCallback() {
            @Override
            public void getJsCallback(String action, String callId, String paramsJsonStr) {
                Message msg = Message.obtain();
                Bundle bundle = msg.getData();

                BridgeObj data = new BridgeObj(action, callId, paramsJsonStr);
                bundle.putSerializable("data", data);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }


        });
        webView.addJavascriptInterface(jsInvokeNativeMethods, "JsInvokeNativeMethods");
        webView.loadUrl(url);

        PhotoManager.getInstance().initImagePicker();
        PhotoManager.getInstance().setPhotoListener(this);

    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            BridgeObj data = (BridgeObj) bundle.getSerializable("data");
            if (data == null) return;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            new TypeToken<TreeMap<String, Object>>() {
                            }.getType(),
                            new JsonDeserializer<TreeMap<String, Object>>() {
                                @Override
                                public TreeMap<String, Object> deserialize(
                                        JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

                                    TreeMap<String, Object> treeMap = new TreeMap<>();
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        treeMap.put(entry.getKey(), entry.getValue());
                                    }
                                    return treeMap;
                                }
                            }).create();
            TreeMap<String, JsonElement> resultMap =
                    gson.fromJson(data.getParamsJsonStr(), new TypeToken<TreeMap<String, JsonElement>>() {
                    }.getType());
            callId = data.getCallId();
            Log.e("wislie","callid："+callId);
            doAction(data.getAction(), data.getCallId(), resultMap);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().compressImage(this, requestCode, resultCode, data);
    }

    private String callId;

    private void doAction(String action, String callId, Map<String, JsonElement> resultMap) {
        switch (action) {
            case "setTitle":
                resultMap.get("title");
                break;
            case "open":
                JsonElement openElement = resultMap.get("url");

                if (!TextUtils.isEmpty(openElement.getAsString())) {
                    Intent openIntent = new Intent(this,
                            LoadingUrlActivity.class);
                    openIntent.putExtra("url", openElement.getAsString());
                    startActivity(openIntent);
                }
                break;
            case "browser":
                JsonElement browserElement = resultMap.get("url");
                Intent browserIntent = new Intent();
                browserIntent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(browserElement.getAsString());
                browserIntent.setData(content_url);
                startActivity(browserIntent);
                break;
            case "getUser":

               /* JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("isLogin", true);
                    jsonObject.put("nickName", "abc");
                    jsonObject.put("accountId", 1l);
                    NativeInvokeJsMethods.getInstance().sendAppInvoke(webView, callId, jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                break;
            case "getLocation":

                break;
            case "getImages":

                final JsonElement countElement = resultMap.get("count");
                PhotoDialog dialog = PhotoDialog.newInstance();
                dialog.showDialog(getFragmentManager());
                dialog.setPhotoListener(new PhotoDialog.OnPhotoListener() {
                    @Override
                    public void onSelectFromAlbum() {
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(countElement.getAsInt());
                        Intent imageIntent = new Intent(BridgeTest.this, ImageGridActivity.class);
                               /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
                        startActivityForResult(imageIntent, Constant.REQUEST_CODE_SELECT);
                    }

                    @Override
                    public void onTakePhoto() {
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(countElement.getAsInt());
                        Intent takePhotoIntent = new Intent(BridgeTest.this, ImageGridActivity.class);
                        takePhotoIntent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(takePhotoIntent, Constant.REQUEST_CODE_SELECT);
                    }
                });
                break;
            case "getImageData":
                final JsonElement localIdElement = resultMap.get("localId");
                JSONObject json = new JSONObject();
                try {
                    json.put("localData", "data:image/jpg;base64,"+ BitmapUtil.encode(localIdElement.getAsString()));
                    Log.e("wislie","json msg:"+json.toString());
                    NativeInvokeJsMethods.getInstance().sendAppInvoke(webView,callId, json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "uploadImages":
                Log.e("wislie","uploadimages");
                break;
            case "dialog":


                break;
            case "toast":

                break;
            case "loading":

                break;
            case "picker":


                break;
            case "setOptionMenu":

                break;
            case "showOptionMenu":

                break;
            case "close":
                finish();
                break;
            case "sendApi":

                break;


        }
    }

    @Override
    public void compressFinish(LGImgCompressor.CompressResult compressResult, int uploadSize) { //文件压缩完成

        mLocalIdList.add(compressResult.getOutPath());
        if (mLocalIdList.size() == uploadSize) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < mLocalIdList.size(); i++) {
                array.put(mLocalIdList.get(i));
            }

            JSONObject json = new JSONObject();
            try {
                json.put("localIds", array);
                NativeInvokeJsMethods.getInstance().sendAppInvoke(webView,callId, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mLocalIdList.clear();
        }
    }


}
