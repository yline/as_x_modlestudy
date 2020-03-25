package com.yline.webview.study.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.yline.base.BaseActivity;
import com.yline.utils.LogUtil;
import com.yline.webview.study.R;
import com.yline.webview.study.jsbridge.BridgeHandler;
import com.yline.webview.study.webview.interceptor.file.FileInterceptor;
import com.yline.webview.study.webview.interceptor.OnWebInterceptor;
import com.yline.webview.study.webview.interceptor.js.JsInterceptor;
import com.yline.webview.study.jsbridge.CallBackFunction;

import java.util.ArrayList;
import java.util.List;

public class WebviewActivity extends BaseActivity {
    public static void launch(Context context, String httpUrl) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, WebviewActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private JsInterceptor mJsInterceptor;

    static class Location {
        String address;
    }

    static class User {
        String name;
        Location location;
        String testStr;
    }

    private WebviewChain mWebviewChain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webview_content);

        List<OnWebInterceptor> interceptorList = new ArrayList<>();
        mJsInterceptor = new JsInterceptor(webView);
        interceptorList.add(mJsInterceptor);
        interceptorList.add(new FileInterceptor());

        mWebviewChain = new WebviewChain(this, webView, interceptorList);
        mWebviewChain.onCreate(this, savedInstanceState);

        findViewById(R.id.webview_java_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJsInterceptor.send("hello");
            }
        });
        findViewById(R.id.webview_java_send2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJsInterceptor.send("hello", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        LogUtil.v("hello 2 back");
                    }
                });
            }
        });
        findViewById(R.id.webview_java_str).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJsInterceptor.send("functionInJs", "data from Java", new CallBackFunction() {

                    @Override
                    public void onCallBack(String data) {
                        LogUtil.v("response data from js " + data);
                    }
                });
            }
        });
        findViewById(R.id.webview_java_json).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                Location location = new Location();
                location.address = "SDU";
                user.location = location;
                user.name = "大头鬼";

                mJsInterceptor.send("functionInJs", new Gson().toJson(user), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {

                    }
                });
            }
        });

        mJsInterceptor.registerHandler(new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                // 这里可以通过data 分发不同的情况
                LogUtil.v("data = " + data);

                if (null != function) {
                    function.onCallBack("submitFromWeb exe, response data 中文 from Java");
                }
            }
        });

        webView.loadUrl("file:///android_asset/demo.html");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWebviewChain.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mWebviewChain.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mWebviewChain.onDestroy(this);
        super.onDestroy();
    }
}
