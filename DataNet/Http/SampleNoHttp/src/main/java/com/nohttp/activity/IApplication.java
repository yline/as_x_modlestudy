package com.nohttp.activity;

import com.nohttp.okhttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import com.yline.application.BaseApplication;

public class IApplication extends BaseApplication
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		NoHttp.Config config = new NoHttp.Config();
		config.setConnectTimeout(30 * 1000) // 设置全局连接超时时间，单位毫秒，默认10s。
				.setReadTimeout(30 * 1000) // 设置全局服务器响应超时时间，单位毫秒，默认10s。
				// 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
				.setCacheStore(new DBCacheStore(this).setEnable(true)) // 如果不使用缓存，设置setEnable(false)禁用。
				// 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
				.setCookieStore(new DBCookieStore(this).setEnable(true)) // 如果不维护cookie，设置false禁用。
				.setNetworkExecutor(new OkHttpNetworkExecutor()); // 配置网络层，默认使用URLConnection，如果想用OkHttp：OkHttpNetworkExecutor。

		NoHttp.initialize(this, new NoHttp.Config());
	}
}
