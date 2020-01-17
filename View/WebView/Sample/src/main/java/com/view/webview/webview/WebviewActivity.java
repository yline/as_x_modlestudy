package com.view.webview.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.view.webview.webview.plugin.FileChooserPlugin;
import com.view.webview.webview.plugin.WebviewXmlPlugin;
import com.yline.base.BaseActivity;

import org.json.JSONObject;

/**
 * 1，普通配置WebView
 * 2，分离UI，解决UI层，导致问题复杂化的问题  // WebviewXmlPlugin
 * 3，解决，H5，选择文件api，Android系统不支持的问题  // FileChooserPlugin
 * 4，
 *
 * // todo study: https://juejin.im/post/5a94f9d15188257a63113a74
 *
 * @author yline 2020/1/17 -- 15:01
 */
public class WebviewActivity extends BaseActivity {
    private static final int REQUEST_CODE_PERMISSION = 1;

    private static final int REQUEST_CODE_CAMERA_FILE = 1001;
    private static final int REQUEST_CODE_ALBUM_FILE = 1000;
    private static final int REQUEST_CODE_ALL_FILE = 1002;

    private static final String TITLE = "webview_title";
    private static final String URL = "webview_url";

    public static void launch(Context context, String title, String url) {
        if (url == null || url.trim().length() == 0) {
            return;
        }

        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, WebviewActivity.class);
            intent.putExtra(TITLE, title);
            intent.putExtra(URL, url);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private WebviewXmlPlugin xmlPlugin;
    private FileChooserPlugin mFileChooserPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xmlPlugin = WebviewXmlPlugin.create(this);

        initViewClick();

        xmlPlugin.getWebView().setWebViewClient(mWebViewClient);
        xmlPlugin.getWebView().setWebChromeClient(mWebChromeClient);
        WebviewManager.decorateSetting(this, xmlPlugin.getWebView());

        initData();

        mFileChooserPlugin = FileChooserPlugin.create(REQUEST_CODE_PERMISSION,
                REQUEST_CODE_CAMERA_FILE, REQUEST_CODE_ALBUM_FILE, REQUEST_CODE_ALL_FILE);
    }

    private void initViewClick() {
        xmlPlugin.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        xmlPlugin.setOnCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void back() {
        if (!xmlPlugin.back()) {
            xmlPlugin.release();
            finish();
        }
    }

    private void initData() {
        String title = getIntent().getStringExtra(TITLE);
        String url = getIntent().getStringExtra(URL);

        xmlPlugin.setTitleText(title);

        String realUrl = toRealUrl(url);
        xmlPlugin.getWebView().loadUrl(realUrl);
    }

    private static final int JS_CODE = 1000;

    //不会产生内存泄漏问题
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
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
                            xmlPlugin.setTitleBarVisible(false);
                            break;
                        case "back":
                            back();
                            break;
                        case "close":
                            finish();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            int progress = newProgress * 5 / 4;
            if (progress < 100 && progress > 0) {
                xmlPlugin.setProgress(true, progress);
            } else {
                xmlPlugin.setProgress(false, 100);

                xmlPlugin.evaluateJavascript(WebviewActivity.this);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            xmlPlugin.setTitleTextWithCheck(title);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (!TextUtils.isEmpty(message) && message.startsWith(JSBridge.HEAD)) {
                Message msg = new Message();
                msg.what = JS_CODE;
                msg.obj = message.substring(JSBridge.HEAD.length());
                mHandler.sendMessage(msg);
                result.confirm();
                return true;
            } else {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mFileChooserPlugin.showFileChooser(WebviewActivity.this, filePathCallback);
            return true;
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = view.getUrl();
            if (!(url.startsWith("http:") || url.startsWith("https:"))) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            xmlPlugin.setTitleTextWithCheck(view.getTitle());

            xmlPlugin.evaluateJavascript(WebviewActivity.this);
            // mCloseView.setVisibility(mWebView.canGoBack() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            try {
                Uri uri = Uri.parse(view.getUrl());
                if (uri.getHost().contains("kjtpay.com")) {
                    handler.proceed();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            new AlertDialog.Builder(WebviewActivity.this).setTitle("证书错误")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("信任", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (handler != null) {
                        handler.proceed();
                    }
                }
            }).create().show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mFileChooserPlugin.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFileChooserPlugin.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        xmlPlugin.release();

        super.onDestroy();
    }

    //对快捷通的页面，添加参数
    private static String toRealUrl(String url) {
        String realUrl = url;
        Uri uri = Uri.parse(url);
        if (!TextUtils.isEmpty(url) && uri.getHost() != null && uri.getHost().contains("kjtpay.com")) {
            String anchor = null;
            if (url.contains("#")) {
                int index = url.indexOf("#");
                anchor = url.substring(index);
                realUrl = url.substring(0, index);
            }

            String params = String.format("isShowHeader=1&deviceId=%s&version=%s&deviceType=android",
                    "deviceId", "1.0.0");

            if (url.contains("?")) {
                realUrl = realUrl + "&" + params;
            } else {
                realUrl = realUrl + "?" + params;
            }

            if (!TextUtils.isEmpty(anchor)) {
                realUrl += anchor;
            }
        }

        return realUrl;
    }
}
