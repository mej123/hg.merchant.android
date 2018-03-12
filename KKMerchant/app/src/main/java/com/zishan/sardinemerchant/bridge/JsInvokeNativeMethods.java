package com.zishan.sardinemerchant.bridge;


import android.webkit.JavascriptInterface;

/**
 * js调用本地
 * Created by wislie on 2018/3/9.
 */

public class JsInvokeNativeMethods {

    private static JsInvokeNativeMethods instance;
    private JSCallback jsCallback;

    public JsInvokeNativeMethods(JSCallback jsCallback) {
        this.jsCallback = jsCallback;
    }

    public static JsInvokeNativeMethods getInstance(JSCallback jsCallback) {
        if (instance == null) {
            synchronized (JsInvokeNativeMethods.class) {
                if (instance == null) {
                    instance = new JsInvokeNativeMethods(jsCallback);
                }
            }
        }
        return instance;
    }

    @JavascriptInterface
    public void setResult(final String action, final String callId, final String paramsJsonStr) { //Bridge 线程
        if(jsCallback != null){
            jsCallback.getJsCallback(action, callId, paramsJsonStr);
        }
    }

}
