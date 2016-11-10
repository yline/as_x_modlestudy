package com.udp.demo.helper;

import android.os.Handler;
import android.os.Message;

import com.udp.demo.activity.MainApplication;
import com.yline.application.task.PriorityRunnable;
import com.yline.log.LogFileUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * UDP Socket帮助类
 * @author yline 2016/11/10 --> 22:12
 * @version 1.0.0
 */
public class UDPHelper
{
	private static final String TAG = UDPHelper.class.getSimpleName();

	private static final boolean DEBUG = true;

	private static final int SEND_CODE_FINISH = 0;

	private SendRunnable sendRunnable;

	private static final int RECEIVER_CODE_START = 10;

	private static final int RECEIVER_CODE_FINISH = 11;

	private static boolean isReceiving = false;

	private OnUDPReceiverCallback callback;

	private ReceiverRunnable receiverRunnable;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (DEBUG)
			{
				LogFileUtil.v(TAG, "msg what = " + msg.what);
			}

			switch (msg.what)
			{
				case SEND_CODE_FINISH:
					if (DEBUG)
					{
						LogFileUtil.v(TAG, "msg arg1 = " + msg.arg1);
					}
					break;
				case RECEIVER_CODE_START:
					if (null != callback)
					{
						if (msg.arg1 == RECEIVER_CODE_START)
						{
							callback.onStart(true);
						}
						else
						{
							callback.onStart(false);
						}
					}
					break;
				case RECEIVER_CODE_FINISH:
					if (null != callback)
					{
						if (null == msg.obj)
						{
							callback.onFinish(null);
						}
						else
						{
							callback.onFinish((byte[]) msg.obj);
						}
					}
					break;
			}
		}
	};

	private UDPHelper()
	{
	}

	public static UDPHelper getInstance()
	{
		return UDPHelperHolder.sInstance;
	}

	public static class UDPHelperHolder
	{
		private static UDPHelper sInstance = new UDPHelper();
	}

	/**
	 * 发送数据,若之前未设置过host,port则会报错
	 * @param data
	 * @return
	 */
	public boolean sendMessage(String data)
	{
		if (null != sendRunnable)
		{
			sendRunnable.setData(data);
			MainApplication.start(sendRunnable, null);
			return true;
		}
		return false;
	}

	/**
	 * @param data 发送的数据
	 * @param host 服务端host
	 * @param port 服务端端口
	 */
	public void sendMessage(String data, String host, int port)
	{
		if (null == sendRunnable)
		{
			sendRunnable = new SendRunnable(host, port);
		}
		sendRunnable.setData(data).setHost(host).setPort(port);
		MainApplication.start(sendRunnable, null);
	}


	private class SendRunnable implements Runnable
	{
		private DatagramSocket sendDatagramSocket;

		private DatagramPacket sendDatagramPacket;

		/** 发送的数据 */
		private byte[] data;

		/** 服务端host */
		private String host;

		/** 服务端端口 */
		private int port;

		SendRunnable()
		{
		}

		SendRunnable(String host, int port)
		{
			this.host = host;
			this.port = port;
		}

		public SendRunnable setData(String data)
		{
			if (null != data)
			{
				this.data = data.getBytes();
			}
			else
			{
				this.data = new byte[0];
			}
			return this;
		}

		public SendRunnable setHost(String host)
		{
			if (null != host)
			{
				this.host = host;
			}
			return this;
		}

		public SendRunnable setPort(int port)
		{
			if (-1 != port)
			{
				this.port = port;
			}
			return this;
		}

		private InetAddress getInetAddress() throws UnknownHostException
		{
			return InetAddress.getByName(host);
		}

		private byte[] getSendData()
		{
			if (null == data)
			{
				data = new byte[0];
			}
			return data;
		}

		private int getPort()
		{
			return port;
		}

		@Override
		public void run()
		{
			try
			{
				if (null == sendDatagramSocket)
				{
					sendDatagramSocket = new DatagramSocket();
				}

				if (null == sendDatagramPacket)
				{
					sendDatagramPacket = new DatagramPacket(getSendData(), getSendData().length, getInetAddress(), getPort());
				}
				else
				{
					sendDatagramPacket.setData(getSendData());
					sendDatagramPacket.setAddress(getInetAddress());
					sendDatagramPacket.setPort(getPort());
				}

				if (DEBUG)
				{
					LogFileUtil.v(TAG, "DatagramSocket -> " + "LocalPort = " + sendDatagramSocket.getLocalPort() + ",Port = " + sendDatagramSocket.getPort()
							+ ",Timeout = " + sendDatagramSocket.getSoTimeout());
					LogFileUtil.v(TAG, "DatagramPacket -> " + new String(sendDatagramPacket.getData()) + ",HostAddress = " + sendDatagramPacket.getAddress().getHostAddress()
							+ ",Port = " + sendDatagramPacket.getPort());
				}

				sendDatagramSocket.send(sendDatagramPacket);
				handler.obtainMessage(SEND_CODE_FINISH, SEND_CODE_FINISH, -1).sendToTarget();
			}
			catch (SocketException e)
			{
				LogFileUtil.e(TAG, "SendRunnable -> send SocketException", e);
				handler.obtainMessage(SEND_CODE_FINISH, (SEND_CODE_FINISH - 1), -1).sendToTarget();
			}
			catch (UnknownHostException e)
			{
				LogFileUtil.e(TAG, "SendRunnable -> send UnknownHostException", e);
				handler.obtainMessage(SEND_CODE_FINISH, (SEND_CODE_FINISH - 1), -1).sendToTarget();
			}
			catch (IOException e)
			{
				LogFileUtil.e(TAG, "SendRunnable -> send IOException", e);
				handler.obtainMessage(SEND_CODE_FINISH, (SEND_CODE_FINISH - 1), -1).sendToTarget();
			}
		}
	}

	/**
	 * @param callback
	 * @return 若之前从未设定过端口, 则可能开启接收失败
	 */
	public boolean startListener(OnUDPReceiverCallback callback)
	{
		if (null != receiverRunnable)
		{
			MainApplication.start(receiverRunnable, PriorityRunnable.Priority.UI_LOW);
			this.callback = callback;
			callback.onStart(false);
			return true;
		}
		return false;
	}

	/**
	 * @param aPort 本地接收用的端口
	 */
	public void startListener(int aPort, OnUDPReceiverCallback callback)
	{
		if (null == receiverRunnable)
		{
			receiverRunnable = new ReceiverRunnable();
		}
		receiverRunnable.setAPort(aPort);
		this.callback = callback;
		MainApplication.start(receiverRunnable, PriorityRunnable.Priority.UI_NORMAL);
	}

	/**
	 * @param aPort   本地接收用的端口
	 * @param timeout 超时时间
	 */
	public void startListener(int aPort, int timeout, OnUDPReceiverCallback callback)
	{
		if (null == receiverRunnable)
		{
			receiverRunnable = new ReceiverRunnable();
		}
		receiverRunnable.setAPort(aPort).setSoTimeout(timeout);
		this.callback = callback;
		MainApplication.start(receiverRunnable, PriorityRunnable.Priority.UI_NORMAL);
	}


	/**
	 * 它的执行是要插队的,而且一次只能接收到一次
	 */
	public class ReceiverRunnable implements Runnable
	{
		private DatagramSocket receiverDatagramSocket;

		private DatagramPacket receiverDatagramPacket;

		/** 本地接收用的端口 */
		private int aPort;

		/** 超时时间,默认为永不超时 */
		private int timeout = 0;

		public ReceiverRunnable setAPort(int aPort)
		{
			this.aPort = aPort;
			return this;
		}

		/**
		 * @param timeout the timeout in milliseconds or 0 for no timeout.
		 */
		public ReceiverRunnable setSoTimeout(int timeout)
		{
			this.timeout = Math.abs(timeout);
			return this;
		}

		@Override
		public void run()
		{
			if (!isReceiving)
			{
				isReceiving = true;
				handler.obtainMessage(RECEIVER_CODE_START, RECEIVER_CODE_START, -1).sendToTarget();

				try
				{
					if (null == receiverDatagramSocket)
					{
						receiverDatagramSocket = new DatagramSocket(aPort);
					}
					else if (receiverDatagramSocket.getLocalPort() != aPort)
					{
						// 若端口变化了,则重新新建
						receiverDatagramSocket = new DatagramSocket(aPort);
					}

					byte data[] = new byte[2048];
					if (null == receiverDatagramPacket)
					{
						receiverDatagramPacket = new DatagramPacket(data, data.length);
					}

					if (DEBUG)
					{
						LogFileUtil.v(TAG, " DatagramSocket -> " + "LocalPort = "
								+ receiverDatagramSocket.getLocalPort() + ",Port = " + receiverDatagramSocket.getPort()
								+ ",Timeout = " + receiverDatagramSocket.getSoTimeout());
						LogFileUtil.v(TAG, " DatagramPacket -> " + "Port = " + receiverDatagramPacket.getPort()
								+ ",data = " + new String(receiverDatagramPacket.getData()));
					}
					
					receiverDatagramSocket.receive(receiverDatagramPacket);
					receiverDatagramSocket.setSoTimeout(timeout);

					/** 除去1024无用的数据 */
					byte[] result = new byte[receiverDatagramPacket.getLength()];
					System.arraycopy(receiverDatagramPacket.getData(), 0, result, 0, receiverDatagramPacket.getLength());

					handler.obtainMessage(RECEIVER_CODE_FINISH, result).sendToTarget();
				}
				catch (SocketException e)
				{
					LogFileUtil.e(TAG, "ReceiverRunnable -> receiver SocketException", e);
					handler.obtainMessage(RECEIVER_CODE_FINISH, null).sendToTarget();
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "ReceiverRunnable -> receiver IOException", e);
					handler.obtainMessage(RECEIVER_CODE_FINISH, null).sendToTarget();
				}
				finally
				{
					isReceiving = false;
				}
			}
			else
			{
				if (DEBUG)
				{
					LogFileUtil.v(TAG, "ReceiverRunnable isReceiving");
				}

				handler.obtainMessage(RECEIVER_CODE_START, (RECEIVER_CODE_START - 1), -1).sendToTarget();
			}
		}
	}

	public interface OnUDPReceiverCallback
	{
		/**
		 * 这一次监听是否 开始成功
		 * @param isStartSuccess
		 */
		void onStart(boolean isStartSuccess);

		/**
		 * 监听结果
		 * @param result
		 */
		void onFinish(byte[] result);
	}
}
