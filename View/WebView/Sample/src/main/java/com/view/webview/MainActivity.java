package com.view.webview;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private WebView firstWebView;

	private WebView secondWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String firstUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_1?news_id=38";
		initFirstWebView(firstUrl);

		String secondUrl = "https://www.baidu.com";
		initSecondWebView(secondUrl);
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
}
