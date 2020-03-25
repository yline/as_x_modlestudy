package com.yline.webview.study.webview.js;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yline.utils.LogUtil;
import com.yline.webview.study.jsbridge.BridgeHandler;
import com.yline.webview.study.jsbridge.BridgeUtil;
import com.yline.webview.study.jsbridge.CallBackFunction;
import com.yline.webview.study.jsbridge.Message;
import com.yline.webview.study.webview.interceptor.OnWebInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class JsInterceptor extends OnWebInterceptor { // implements WebViewJavascriptBridge

    public static final String toLoadJs = "WebViewJavascriptBridge.js";
    private Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();

    private List<Message> startupMessage = new ArrayList<>();

    public List<Message> getStartupMessage() {
        return startupMessage;
    }

    public void setStartupMessage(List<Message> startupMessage) {
        this.startupMessage = startupMessage;
    }

    private long uniqueId = 0;

    private WebView mWebView;

    public JsInterceptor(WebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void onCreate(Context context, Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
    }

    @Override
    public boolean shouldOverrideUrlLoading(Context context, WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据adb adb
            handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(context, view, url);
        }
    }

    @Override
    public void onPageFinished(Context context, WebView view, String url) {
        if (JsInterceptor.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(view, JsInterceptor.toLoadJs);
        }

        if (getStartupMessage() != null) {
            for (Message m : getStartupMessage()) {
                dispatchMessage(m);
            }
            setStartupMessage(null);
        }
    }

    void handlerReturnData(String url) {
        String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
        CallBackFunction f = responseCallbacks.get(functionName);
        String data = BridgeUtil.getDataFromReturnUrl(url);
        if (f != null) {
            f.onCallBack(data);
            responseCallbacks.remove(functionName);
            return;
        }
    }

    public void send(String data) {
        doSendInner(null, data, null);
    }

    public void send(String data, CallBackFunction responseCallback) {
        doSendInner(null, data, responseCallback);
    }

    public void send(String handlerName, String data, CallBackFunction callBack) {
        doSendInner(handlerName, data, callBack);
    }

    private BridgeHandler mBridgeHandler;

    public void registerHandler(BridgeHandler handler) {
        this.mBridgeHandler = handler;
    }

    private BridgeHandler getBridgeHandler() {
        if (null == mBridgeHandler) {
            mBridgeHandler = new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    if (null != data) {
                        function.onCallBack("default response data = " + data);
                    }
                }
            };
        }
        return mBridgeHandler;
    }

    private void doSendInner(String handlerName, String data, CallBackFunction responseCallback) {
        Message m = new Message();
        if (!TextUtils.isEmpty(handlerName)) {
            m.setHandlerName(handlerName);
        }

        if (!TextUtils.isEmpty(data)) {
            m.setData(data);
        }

        if (responseCallback != null) {
            String callbackString = String.format("JAVA_CB_%s_%s", ++uniqueId, SystemClock.currentThreadTimeMillis());
            responseCallbacks.put(callbackString, responseCallback);
            m.setCallbackId(callbackString);
        }
        queueMessage(m);
    }

    private void queueMessage(Message m) {
        if (startupMessage != null) {
            startupMessage.add(m);
        } else {
            dispatchMessage(m);
        }
    }

    void dispatchMessage(Message m) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }

        String messageJson = Message.toJson(m);
        if (null == messageJson) {
            return;
        }

        //escape special characters for json string
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        mWebView.loadUrl(javascriptCommand);
    }

    void flushMessageQueue() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }

        String jsUrl = BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA;
        mWebView.loadUrl(jsUrl);
        responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                // deserializeMessage
                List<Message> list = Message.toMessageList(data);
                if (null == list || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    Message m = list.get(i);
                    String responseId = m.getResponseId();
                    // 是否是response
                    if (!TextUtils.isEmpty(responseId)) {
                        CallBackFunction function = responseCallbacks.get(responseId);
                        String responseData = m.getResponseData();
                        function.onCallBack(responseData);
                        responseCallbacks.remove(responseId);
                    } else {
                        CallBackFunction responseFunction = null;
                        // if had callbackId
                        final String callbackId = m.getCallbackId();
                        if (!TextUtils.isEmpty(callbackId)) {
                            responseFunction = new CallBackFunction() {
                                @Override
                                public void onCallBack(String data) {
                                    Message responseMsg = new Message();
                                    responseMsg.setResponseId(callbackId);
                                    responseMsg.setResponseData(data);
                                    queueMessage(responseMsg);
                                }
                            };
                        } else {
                            responseFunction = new CallBackFunction() {
                                @Override
                                public void onCallBack(String data) {
                                    // do nothing
                                }
                            };
                        }

                        getBridgeHandler().handler(m.getData(), responseFunction);
                    }
                }
            }
        });
    }
}
