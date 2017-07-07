package com.view.webview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.view.webview.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class BaiduUrlActivity extends BaseAppCompatActivity
{
	private WebView secondWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidu_url);

		String secondUrl = "https://www.baidu.com";
		initSecondWebView(secondUrl);
	}

	private void initSecondWebView(final String httpUrl)
	{
		secondWebView = (WebView) findViewById(R.id.web_view_second);

		WebSettings webSettings = secondWebView.getSettings();
		webSettings.setDomStorageEnabled(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		secondWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		secondWebView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				super.onReceivedTitle(view, title);
				LogFileUtil.v("First title = " + title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				LogFileUtil.v("First newProgress = " + newProgress);
			}
		});
		secondWebView.loadUrl(httpUrl);
	}
	
	public static void actionStart(Context context)
	{
	   context.startActivity(new Intent(context, BaiduUrlActivity.class));
	}
}
