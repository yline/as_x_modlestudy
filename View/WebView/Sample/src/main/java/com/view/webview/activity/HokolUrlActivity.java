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

public class HokolUrlActivity extends BaseAppCompatActivity
{
	private WebView firstWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hokol_url);

		String firstUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_1?news_id=2";
		initFirstWebView(firstUrl);
	}

	private void initFirstWebView(String httpUrl)
	{
		firstWebView = (WebView) findViewById(R.id.web_view_first);

		WebSettings webSettings = firstWebView.getSettings();
		webSettings.setDomStorageEnabled(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		firstWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		firstWebView.setWebChromeClient(new WebChromeClient()
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
		firstWebView.loadUrl(httpUrl);
	}
	
	public static void actionStart(Context context)
	{
	   context.startActivity(new Intent(context, HokolUrlActivity.class));
	}
}
