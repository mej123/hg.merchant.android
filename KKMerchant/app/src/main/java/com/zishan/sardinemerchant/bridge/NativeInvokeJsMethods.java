package com.zishan.sardinemerchant.bridge;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;


/**
 * native 调用 js的方法
 * Created by wislie on 2018/3/9.
 */
@SuppressLint("NewApi")
public class NativeInvokeJsMethods {
    public final static String JS_nativeAppInvokeCallback_FROM_JAVA =
            "javascript:SardineJSBridge._nativeInvokeCallback('%s','%s');";
    public final static String JS_nativeAppEventCallback_FROM_JAVA =
            "javascript:SardineJSBridge.__nativeAppEventCallback('%s','%s');";

    private static NativeInvokeJsMethods INSTANCE;

    public static NativeInvokeJsMethods getInstance() {
        if (INSTANCE == null) {
            synchronized (NativeInvokeJsMethods.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NativeInvokeJsMethods();
                }
            }
        }
        return INSTANCE;
    }


    public void nativeAppCallback(final WebView webView, String jsAction, String callId, String eventName, String resultJsonStr) {
        final String javascriptCommand = getStringByAction(jsAction, callId, eventName, resultJsonStr);
        if (TextUtils.isEmpty(javascriptCommand)) return;
        webView.post(new Runnable() {

            @Override
            public void run() {
                Log.e("wislie", "javascriptCommand:" + javascriptCommand);
                webView.evaluateJavascript(javascriptCommand, null);
            }
        });
    }

    private String getStringByAction(String jsAction, String callId, String eventName, String resultJsonStr) {
        if (jsAction != null && jsAction.equals(JS_nativeAppInvokeCallback_FROM_JAVA)) {
            return String.format(JS_nativeAppInvokeCallback_FROM_JAVA, callId, resultJsonStr);
        } else if (jsAction != null && jsAction.equals(JS_nativeAppEventCallback_FROM_JAVA)) {
            return String.format(JS_nativeAppEventCallback_FROM_JAVA, eventName, resultJsonStr);
        }
        return null;
    }

    //调用__nativeAppInvokeCallback
    public void sendAppInvoke(WebView webView, String callId, String params) {
        nativeAppCallback(webView, JS_nativeAppInvokeCallback_FROM_JAVA, callId, null, params);
    }

    //调用__nativeAppEventCallback
    public void sendAppEvent(WebView webView, String eventName, String params) {
        nativeAppCallback(webView, JS_nativeAppEventCallback_FROM_JAVA, null, eventName, params);
    }


}
