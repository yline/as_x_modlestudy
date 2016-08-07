package com.udp.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.data.network.udp.instance.R;
import com.udp.demo.helper.UDPUser;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class MainActivity extends BaseActivity
{
	private UDPUser mUDPUser;

	private EditText mEtServerIp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mUDPUser = new UDPUser();
		
		mEtServerIp = (EditText) findViewById(R.id.et_server_ip);
		mEtServerIp.setText(getLocalIPAddress());

		findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_send");

				final String content = "客户端发送的内容0";
				final String host = mEtServerIp.getText().toString().trim();
				// String host = "255.255.255.255";
				final int port = 1001;

				// mUDPUser.sendMessage(content, host, port);
				new Thread(new Runnable()
				{

					@Override
					public void run()
					{
						send(content.getBytes(), host, port);
					}
				}).start();

			}
		});

		int aPort = 2002;
		int timeout = 0;
		mUDPUser.receiverMessage(aPort, timeout);
	}

	private DatagramSocket mSendDatagramSocket;

	private DatagramPacket mSendDatagramPacket;

	/**
	 * @param data 发送的数据
	 * @param host 服务端host
	 * @param port 服务端端口
	 * @return
	 */
	public boolean send(byte[] data, String host, int port)
	{
		try
		{
			if (null == mSendDatagramSocket)
			{
				mSendDatagramSocket = new DatagramSocket();
			}

			InetAddress inetAddress = InetAddress.getByName(host);

			if (null == mSendDatagramPacket)
			{
				mSendDatagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
			}

			LogFileUtil.v(MainApplication.TAG, "UDPHelper -> before send mSendDatagramSocket = " + mSendDatagramSocket + ",mSendDatagramPacket = " + mSendDatagramPacket + ",data = " + new String(data) + ",inetAddress = " + inetAddress + ",port = " + port);
			mSendDatagramSocket.send(mSendDatagramPacket);

			return true;
		}
		catch (SocketException e)
		{
			LogFileUtil.e(MainApplication.TAG, "UDPHelper -> send SocketException", e);
		}
		catch (UnknownHostException e)
		{
			LogFileUtil.e(MainApplication.TAG, "UDPHelper -> send UnknownHostException", e);
		}
		catch (IOException e)
		{
			LogFileUtil.e(MainApplication.TAG, "UDPHelper -> send IOException", e);
		}

		return false;
	}

	//获取本地IP函数
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
