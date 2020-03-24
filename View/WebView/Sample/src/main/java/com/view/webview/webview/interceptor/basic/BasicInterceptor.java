package com.view.webview.webview.interceptor.basic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.view.webview.R;
import com.view.webview.webview.interceptor.OnWebInterceptor;

public class BasicInterceptor extends OnWebInterceptor {
    private View mContainerView;

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mTitleView;

    private String mTitle;

    public BasicInterceptor(View containerView, String title) {
        this.mContainerView = containerView;
        this.mTitle = title;
    }

    public void setTitleBarVisible(boolean visible) {
        mContainerView.findViewById(R.id.webview_title_bar).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 返回的api
     */
    public void back(Context context) {
        if (null != mWebView && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            release();

            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    private void release() {
        if (null != mWebView) {
            try {
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Context context, Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);

        initView(context);
    }

    private void initView(final Context context) {
        mProgressBar = mContainerView.findViewById(R.id.webview_progress);
        mProgressBar.setMax(100);

        mWebView = mContainerView.findViewById(R.id.webview_web);
        mTitleView = mContainerView.findViewById(R.id.webview_title);

        mContainerView.findViewById(R.id.webview_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(context);
            }
        });
        mContainerView.findViewById(R.id.webview_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });

        mTitleView.setText(mTitle);
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);

        release();
    }

    @Override
    public boolean shouldOverrideUrlLoading(Context context, WebView view, WebResourceRequest request) {
        String url = view.getUrl();
        if (!(url.startsWith("http:") || url.startsWith("https:"))) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (context instanceof Activity) {
                    context.startActivity(intent);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.shouldOverrideUrlLoading(context, view, request);
    }

    @Override
    public void onPageFinished(Context context, WebView view, String url) {
        super.onPageFinished(context, view, url);

        String titleText = view.getTitle();
        String oldText = mTitleView.getText().toString().trim();
        if (TextUtils.isEmpty(oldText) && !TextUtils.isEmpty(titleText)) {
            mTitleView.setText(titleText);
        }
    }

    @Override
    public void onReceivedSslError(Context context, WebView view, final SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(context, view, handler, error);

        try {
            Uri uri = Uri.parse(view.getUrl());
            if (uri.getHost().contains("kjtpay.com")) {
                handler.proceed();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new AlertDialog.Builder(context).setTitle("证书错误")
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

    @Override
    public void onProgressChanged(Context context, WebView view, int newProgress) {
        super.onProgressChanged(context, view, newProgress);
        int progress = newProgress * 5 / 4;
        if (progress < 100 && progress > 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(progress);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.setProgress(100);
        }
    }

    @Override
    public void onReceivedTitle(Context context, WebView view, String title) {
        super.onReceivedTitle(context, view, title);

        String oldText = mTitleView.getText().toString().trim();
        if (TextUtils.isEmpty(oldText) && !TextUtils.isEmpty(title)) {
            mTitleView.setText(title);
        }
    }
}
