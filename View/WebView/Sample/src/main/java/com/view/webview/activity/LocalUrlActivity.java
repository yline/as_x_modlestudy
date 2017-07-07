package com.view.webview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.view.webview.R;
import com.yline.base.BaseAppCompatActivity;

public class LocalUrlActivity extends BaseAppCompatActivity
{
	private static final String LocalUrlHead = "file:///android_asset/";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_url);

		WebView webView = (WebView) findViewById(R.id.web_view_local);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(LocalUrlHead + "protocol/protocol_charge.html");
	}

	public static void actionStart(Context context)
	{
	   context.startActivity(new Intent(context, LocalUrlActivity.class));
	}
}
