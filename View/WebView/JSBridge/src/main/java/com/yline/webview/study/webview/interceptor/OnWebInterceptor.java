package com.yline.webview.study.webview.interceptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.annotation.NonNull;

/**
 * WebView Activity所有回调
 *
 * @author yline 2020/3/23 -- 18:52
 */
public abstract class OnWebInterceptor {
    /* ---------------------------- WebViewClient 相关 --------------------------- */
    public boolean shouldOverrideUrlLoading(Context context, WebView view, WebResourceRequest request) {
        return false;
    }

    public void onPageFinished(Context context, WebView view, String url) {
    }

    public void onReceivedSslError(Context context, WebView view, final SslErrorHandler handler, SslError error) {
    }

    /* ---------------------------- WebChromeClient 相关 --------------------------- */
    public void onProgressChanged(Context context, WebView view, int newProgress) {
    }

    public void onReceivedTitle(Context context, WebView view, String title) {
    }

    public boolean onJsPrompt(Context context, WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    public boolean onShowFileChooser(Context context, WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    /* ---------------------------- Activity相关 --------------------------- */
    public void onCreate(Context context, Bundle savedInstanceState) {
    }

    public void onRequestPermissionsResult(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
    }

    public void onDestroy(Context context) {
    }
}
