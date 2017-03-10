package com.sample.http.boye.activity;

import com.yline.application.BaseApplication;

import org.xutils.x;

/**
 * 继承之后,自己调用方法开启对应的功能
 */
public class MainApplication extends BaseApplication
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		x.Ext.init(this);
		x.Ext.setDebug(true);
	}
}
