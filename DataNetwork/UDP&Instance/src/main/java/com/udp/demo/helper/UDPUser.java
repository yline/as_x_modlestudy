package com.udp.demo.helper;

import com.udp.demo.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 不能使用单例模式,因为两个方法会异步被阻塞
 * @author YLine 2016/8/7 --> 19:31
 * @version 1.0.0
 */
public class UDPUser
{
	private UDPHelper udpHelper = UDPHelper.getInstance();

	public void sendMessage()
	{
		LogFileUtil.v(MainApplication.TAG, "LocalIPAddress = " + getLocalIPAddress());
		
		final String content = "我在测试" + System.currentTimeMillis(); // 发送的数据
		final String host = "255.255.255.255"; // 服务端host
		final int port = 1001; // 服务端端口
		udpHelper.sendMessage(content, host, port);
	}

	public void receiverMessage()
	{
		final int aPort = 2002;
		udpHelper.startListener(aPort, new UDPHelper.OnUDPReceiverCallback()
		{
			@Override
			public void onStart(boolean isStartSuccess)
			{
				LogFileUtil.v(MainApplication.TAG, "receiverMessage onStart isStartSuccess = " + isStartSuccess);
			}

			@Override
			public void onFinish(byte[] result)
			{
				LogFileUtil.v(MainApplication.TAG, "receiverMessage onFinish result = " + Arrays.toString(result));
			}
		});
	}

	/**
	 * 获取本地IP函数
	 * @return
	 */
	private String getLocalIPAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements(); )
			{
				NetworkInterface intf = mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); )
				{
					InetAddress inetAddress = enumIPAddr.nextElement();
					//如果不是回环地址
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
					{
						//直接返回本地IP地址
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			LogFileUtil.e(MainApplication.TAG, "SocketException", ex);
		}
		return null;
	}
}
