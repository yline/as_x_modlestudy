package com.yline.webview.study.webview.interceptor.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.yline.webview.study.webview.interceptor.OnWebInterceptor;

public class FileInterceptor extends OnWebInterceptor {
    private static final int REQUEST_CODE = 1000;
    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    public boolean onShowFileChooser(Context context, WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        mFilePathCallback = filePathCallback;
        pickFile(context);
        return true;
    }

    private void pickFile(Context context) {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        ((Activity) context).startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        super.onActivityResult(context, requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null == mFilePathCallback) {
                return;
            }

            if (resultCode != Activity.RESULT_OK) {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
                return;
            }

            Uri[] results = null;
            Uri uri = data.getData();
            if (null != uri) {
                results = new Uri[]{uri};
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }
}
