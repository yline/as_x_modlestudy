package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nohttp.R;

public class DownloadActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, DownloadActivity.class));
	}
}
