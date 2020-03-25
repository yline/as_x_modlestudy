package com.view.webview.webview.interceptor.js;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.view.webview.webview.interceptor.OnWebInterceptor;
import com.yline.utils.LogUtil;

import org.json.JSONObject;

/**
 * 交互原理：
 * 可以看的文章：
 * https://blog.csdn.net/u012971339/article/details/50770854
 * https://github.com/pedant/safe-java-js-webview-bridge
 *
 * js调用java
 * js :  prompt('kjtpayApp://{\"type\":\"'+type+'\"}'); 调用这个
 * java: onJsPrompt() 方法的实现，message作为内容
 *
 * @author yline 2020/3/25 -- 14:17
 */
public class JSInterceptor extends OnWebInterceptor {
    private static final int JS_CODE = 1000;

    private OnJSBridge mJsBridge;

    public JSInterceptor(JSInterceptor.OnJSBridge jsBridge) {
        mJsBridge = jsBridge;
    }

    //不会产生内存泄漏问题
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == JS_CODE) {   //处理java ，js交互
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (TextUtils.isEmpty(jsonObject.getString("type"))) {
                        return;
                    }

                    switch (jsonObject.getString("type")) {
                        case "hideTitle":
                            getJsBridge().hideTitle();

                            break;
                        case "back":
                            getJsBridge().back();
                            break;
                        case "close":
                            getJsBridge().close();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onPageFinished(Context context, WebView view, String url) {
        evaluateJavascript(context, view);
    }

    @Override
    public void onProgressChanged(Context context, WebView view, int newProgress) {
        int progress = newProgress * 5 / 4;
        if (progress > 100) {
            evaluateJavascript(context, view);
        }
    }

    @Override
    public boolean onJsPrompt(Context context, WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (!TextUtils.isEmpty(message) && message.startsWith(JSBridge.HEAD)) {
            Message msg = new Message();
            msg.what = JS_CODE;
            msg.obj = message.substring(JSBridge.HEAD.length());
            mHandler.sendMessage(msg);
            result.confirm();
            return true;
        } else {
            return super.onJsPrompt(context, view, url, message, defaultValue, result);
        }
    }

    @Override
    public void onDestroy(Context context) {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        // 清除cookie
        // CookieSyncManager.createInstance(this);
        // CookieManager.getInstance().removeAllCookie();

        super.onDestroy(context);
    }

    private void evaluateJavascript(Context context, WebView webView) {
        if (null != webView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(JSBridge.loadInitJS(context.getApplicationContext()), null);
            }
        }
    }

    private OnJSBridge getJsBridge() {
        if (null != mJsBridge) {
            return mJsBridge;
        }
        return DefaultJSBridge;
    }

    private OnJSBridge DefaultJSBridge = new OnJSBridge() {
        @Override
        public void hideTitle() {
            LogUtil.v("hideTitle");
        }

        @Override
        public void back() {
            LogUtil.v("back");
        }

        @Override
        public void close() {
            LogUtil.v("close");
        }
    };

    public interface OnJSBridge {
        void hideTitle();

        void back();

        void close();
    }
}
