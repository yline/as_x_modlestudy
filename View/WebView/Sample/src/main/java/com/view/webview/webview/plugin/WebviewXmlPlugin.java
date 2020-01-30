package com.view.webview.webview.plugin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.view.webview.R;
import com.view.webview.webview.JSBridge;

public class WebviewXmlPlugin {
    public static WebviewXmlPlugin create(Activity activity) {
        activity.setContentView(R.layout.activity_webview);
        View containerView = activity.findViewById(R.id.webview_container);
        return new WebviewXmlPlugin(containerView);
    }

    private View mContainerView;

    private ProgressBar mProgressBar;
    private TextView mTitleView;
    private WebView mWebView;

    private WebviewXmlPlugin(View containerView) {
        mContainerView = containerView;
        initView();
    }

    private void initView() {
        mProgressBar = mContainerView.findViewById(R.id.webview_progress);
        mProgressBar.setMax(100);

        mWebView = mContainerView.findViewById(R.id.webview_web);
        mTitleView = mContainerView.findViewById(R.id.webview_title);
    }

    public void setOnCloseClickListener(View.OnClickListener listener) {
        mContainerView.findViewById(R.id.webview_close).setOnClickListener(listener);
    }

    public void setOnBackClickListener(View.OnClickListener listener) {
        mContainerView.findViewById(R.id.webview_back).setOnClickListener(listener);
    }

    public void setTitleBarVisible(boolean visible) {
        mContainerView.findViewById(R.id.webview_title_bar).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitleText(CharSequence titleText) {
        mTitleView.setText(titleText);
    }

    public void setTitleTextWithCheck(CharSequence titleText) {
        String oldText = mTitleView.getText().toString().trim();
        if (TextUtils.isEmpty(oldText) && !TextUtils.isEmpty(titleText)) {
            mTitleView.setText(titleText);
        }
    }

    public void setProgress(boolean visible, int progress) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        mProgressBar.setProgress(progress);
    }

    public boolean back() {
        if (null != mWebView && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public void release() {
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

    public void evaluateJavascript(Context context) {
        if (null != mWebView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(JSBridge.loadInitJS(context.getApplicationContext()), null);
            }
        }
    }

    public WebView getWebView() {
        return mWebView;
    }
}
