package com.yline.webview.study.webview.interceptor.js;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.webkit.WebView;

import com.yline.utils.LogUtil;
import com.yline.webview.study.jsbridge.BridgeHandler;
import com.yline.webview.study.jsbridge.JsBridgeUtil;
import com.yline.webview.study.jsbridge.CallBackFunction;
import com.yline.webview.study.jsbridge.Message;
import com.yline.webview.study.webview.interceptor.OnWebInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交互原理：
 *
 * java调用js:
 * java：javascript:WebViewJavascriptBridge._handleMessageFromNative('{\"data\":\"hello\"}');
 * js: 在相应的文件中，实现对应的方法 + 参数
 *
 * js调用java: 【本文的方法，不如 onJsPrompt() 方法】，
 * js : messagingIframe.src = 'yy://' + QUEUE_HAS_MESSAGE;
 * java: shouldOverrideUrlLoading 方法，url作为参数
 * 缺陷：经过测试，刚刚加载完成，return的内容不能触发 shouldOverrideUrlLoading 方法
 *
 * 值得参考的是：利用{string-callback} 的映射关系，实现java调用js，js的回调
 *
 * @author yline 2020/3/25 -- 14:19
 */
@SuppressLint("SetJavaScriptEnabled")
public class JsInterceptor extends OnWebInterceptor { // implements WebViewJavascriptBridge
    private Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();

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

        LogUtil.v("url = " + url);
        if (url.startsWith(JsBridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据adb adb
            handlerReturnData(url);
            return true;
        } else if (url.startsWith(JsBridgeUtil.YY_OVERRIDE_SCHEMA)) { // send调用会执行
            flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(context, view, url);
        }
    }

    @Override
    public void onPageFinished(Context context, WebView view, String url) {
        JsBridgeUtil.loadLocalJsForWebview(mWebView);
        handleStartupMessage();
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

    public void registerHandler(BridgeHandler handler) {
        this.mBridgeHandler = handler;
    }

    private void handlerReturnData(String url) {
        // url 中带有数据
        // 案例：[{"responseId":"JAVA_CB_4_811","responseData":{"Javascript Responds":"测试中文!"}}]
        String functionName = JsBridgeUtil.getFunctionFromReturnUrl(url); // 这是key
        CallBackFunction function = responseCallbacks.get(functionName);
        String data = JsBridgeUtil.getDataFromReturnUrl(url);
        if (function != null) {
            function.onCallBack(data);
            responseCallbacks.remove(functionName);
        }
    }

    private void flushMessageQueue() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }

        final String jsUrl = "javascript:WebViewJavascriptBridge._fetchQueue();";
        mWebView.loadUrl(jsUrl);

        String callbackId = JsBridgeUtil.parseFunctionName(jsUrl);
        LogUtil.v("callbackId = " + callbackId);
        responseCallbacks.put(callbackId, new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                LogUtil.v("data = " + data);
                // deserializeMessage
                List<Message> list = Message.toMessageList(data);
                if (null == list || list.isEmpty()) {
                    return;
                }

                // 多个消息，这才是 分发js回来的内容
                for (int i = 0; i < list.size(); i++) {
                    Message message = list.get(i);
                    String responseId = message.getResponseId();
                    // 是否是response
                    if (!TextUtils.isEmpty(responseId)) {
                        CallBackFunction function = responseCallbacks.get(responseId);
                        String responseData = message.getResponseData();
                        function.onCallBack(responseData);
                        responseCallbacks.remove(responseId);
                    } else {
                        CallBackFunction responseFunction = null;
                        // if had callbackId
                        final String callbackId = message.getCallbackId();
                        if (!TextUtils.isEmpty(callbackId)) {
                            responseFunction = new CallBackFunction() {
                                @Override
                                public void onCallBack(String data) {
                                    Message responseMsg = new Message();
                                    responseMsg.setResponseId(callbackId);
                                    responseMsg.setResponseData(data);
                                    queueMessageSafely(responseMsg);
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

                        getBridgeHandler().handler(message.getData(), responseFunction);
                    }
                }
            }
        });
    }

    private BridgeHandler mBridgeHandler;

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

    /* ----------------------------- 处理将信息发送给js的事务 ------------------------- */
    private List<Message> mStartupMessageList = new ArrayList<>();

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

        queueMessageSafely(m);
    }

    private void handleStartupMessage() {
        if (null == mStartupMessageList) {
            return;
        }

        for (Message message : mStartupMessageList) {
            queueMessage(message);
        }
        mStartupMessageList = null;
    }

    private void queueMessageSafely(Message m) {
        if (null != mStartupMessageList) {
            mStartupMessageList.add(m);
        } else {
            queueMessage(m);
        }
    }

    private void queueMessage(Message m) {
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

        final String jsCommandFormat = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
        final String jsCommand = String.format(jsCommandFormat, messageJson);

        LogUtil.v("messageJson = " + jsCommand);
        mWebView.loadUrl(jsCommand);
    }
}
