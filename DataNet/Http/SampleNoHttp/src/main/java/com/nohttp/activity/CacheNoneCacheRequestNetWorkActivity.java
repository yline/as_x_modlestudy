package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nohttp.R;

public class CacheNoneCacheRequestNetWorkActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache_none_cache_request_net_work);
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, CacheNoneCacheRequestNetWorkActivity.class));
	}
}
