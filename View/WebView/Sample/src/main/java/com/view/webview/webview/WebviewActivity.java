package com.view.webview.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.view.webview.R;
import com.view.webview.webview.interceptor.OnWebInterceptor;
import com.view.webview.webview.interceptor.basic.BasicInterceptor;
import com.view.webview.webview.interceptor.file.FileChooserInterceptor;
import com.view.webview.webview.interceptor.js.JSInterceptor;
import com.yline.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用Chain将所有功能，转移到 一个个的 Interceptor中
 * 1，BasicInterceptor : 基础功能都在那里
 * 2，FileChooserInterceptor: 文件选择器适配
 * 3，JSInterceptor: 与js交互逻辑，api可以通过接口，转接到WebViewActivity中实现
 *
 * @author yline 2020/3/24 -- 10:50
 */
public class WebviewActivity extends BaseActivity implements JSInterceptor.OnJSBridge {
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

    private WebviewChain mChain;
    private BasicInterceptor mBasicInterceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String title = getIntent().getStringExtra(TITLE);
        String url = getIntent().getStringExtra(URL);

        WebView webView = findViewById(R.id.webview_web);
        View containerView = findViewById(R.id.webview_container);

        // 所有功能的实现，都在这里
        List<OnWebInterceptor> interceptorList = new ArrayList<>();

        // 所有功能的具体实现，基本都在拦截器中
        // 杂七杂八的，比较众多
        mBasicInterceptor = new BasicInterceptor(containerView, title);
        interceptorList.add(mBasicInterceptor);

        // 负责文件选择器
        interceptorList.add(new FileChooserInterceptor(REQUEST_CODE_PERMISSION, REQUEST_CODE_CAMERA_FILE, REQUEST_CODE_ALBUM_FILE, REQUEST_CODE_ALL_FILE));

        // 负责JS适配
        interceptorList.add(new JSInterceptor(this));

        mChain = new WebviewChain(this, webView, interceptorList);
        mChain.onCreate(this, savedInstanceState);

        String realUrl = toRealUrl(url);
        webView.loadUrl(realUrl);
        // mWebView.loadUrl("file:///android_asset/bbtuanJsBridgeTest.html");
    }

    @Override
    public void hideTitle() {
        if (null != mBasicInterceptor) {
            mBasicInterceptor.setTitleBarVisible(false);
        }
    }

    @Override
    public void back() {
        if (null != mBasicInterceptor) {
            mBasicInterceptor.back(WebviewActivity.this);
        }
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mChain.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mChain.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mChain.onDestroy(this);
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
