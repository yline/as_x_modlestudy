package com.yline.webview.study.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.yline.base.BaseActivity;
import com.yline.utils.LogUtil;
import com.yline.webview.study.R;
import com.yline.webview.study.jsbridge.BridgeHandler;
import com.yline.webview.study.webview.js.JsInterceptor;
import com.yline.webview.study.jsbridge.CallBackFunction;

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

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    static class Location {
        String address;
    }

    static class User {
        String name;
        Location location;
        String testStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webview_content);

        mJsInterceptor = new JsInterceptor(webView);

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

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }
        });

        webView.loadUrl("file:///android_asset/demo.html");
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
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
}
