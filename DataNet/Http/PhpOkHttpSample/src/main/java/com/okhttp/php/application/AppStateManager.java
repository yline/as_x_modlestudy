package com.okhttp.php.application;

import com.yline.application.SDKManager;
import com.yline.utils.SPUtil;

/**
 * App状态
 *
 * @author yline 2017/6/25 -- 11:58
 * @version 1.0.0
 */
public class AppStateManager
{
	public static final String DefaultServerIp = "192.168.2.107";

	public static final int DefaultServerPort = 80;
	
	private static final String FileName = "AppStateFile";

	private static final String KeyServerIp = "AppStateServerIp";

	private static final String KeyServerPort = "AppStateServerPort";

	private AppStateManager()
	{
	}

	public static AppStateManager getInstance()
	{
		return AppStateHolder.sInstance;
	}

	private static class AppStateHolder
	{
		private static AppStateManager sInstance = new AppStateManager();
	}

	public void setServerIp(String inputIp, int port)
	{
		SPUtil.put(SDKManager.getApplication(), KeyServerIp, inputIp, FileName);
		SPUtil.put(SDKManager.getApplication(), KeyServerPort, port, FileName);
	}

	public String getServerIp()
	{
		Object object = SPUtil.get(SDKManager.getApplication(), KeyServerIp, DefaultServerIp, FileName);
		if (null == object)
		{
			return DefaultServerIp;
		}
		return (String) object;
	}

	public int getServerPort()
	{
		Object object = SPUtil.get(SDKManager.getApplication(), KeyServerPort, DefaultServerPort, FileName);
		if (null == object)
		{
			return DefaultServerPort;
		}
		return (int) object;
	}
}
