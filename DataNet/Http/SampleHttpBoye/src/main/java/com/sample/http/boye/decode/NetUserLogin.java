package com.sample.http.boye.decode;

import com.google.gson.Gson;
import com.sample.http.boye.bean.UserLoginBean;
import com.sample.http.boye.manager.NetManager;
import com.sample.http.boye.obtain.NetPublicObtain;

/**
 * 用户登录接口
 */
public class NetUserLogin extends NetPublicObtain
{
	public static final String TYPE_VALUE = "BY_User_login";

	public static final String API_VER_VALUE = NetPublicObtain.API_VER_VALUE; // 这是默认的，后期会修改的

	// 业务参数	必须

	/** 登录用户名，可以是手机号 */
	public static final String USERNAME = "username";

	/** 密码 */
	public static final String PASSWORD = "password";

	@Override
	protected void success(String data)
	{
		super.success(data);
		Gson gson = new Gson();
		UserLoginBean bean = gson.fromJson(data, UserLoginBean.class);
		// 这一步是完美的缓存的地方....
		NetManager.getInstance().setUserLoginSuccess(bean);
	}

	@Override
	protected void errorNet(String ex)
	{
		super.errorNet(ex);
		NetManager.getInstance().setUserLoginNetError(ex);
	}

	@Override
	protected void errorParams(String ex)
	{
		super.errorParams(ex);
		NetManager.getInstance().setUserLoginParamsError(ex);
	}
}
