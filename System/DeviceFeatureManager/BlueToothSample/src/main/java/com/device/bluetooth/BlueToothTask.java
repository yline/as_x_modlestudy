package com.device.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.yline.log.LogFileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 完成,Bluetooth的,信息传输;
 * 单次传输信息,即:
 * 发送方:连接一次 -> 链接成功 -> 发送信息 -> 发送完毕 -> 关掉Socket
 * 接收方:一直监听 -> 监听到Socket -> 接收到信息 -> 接收完毕 -> 继续监听
 * <p/>
 * 一下可参考,但主要是伪代码(可参考的伪代码)
 * @author yline 2016/11/8 --> 18:53
 * @version 1.0.0
 */
public class BlueToothTask
{
	private static final String TAG = BlueToothTask.class.getSimpleName();

	private final String ACCEPT_DEVICE_NAME;

	private BluetoothAdapter bluetoothAdapter = BlueToothHelper.getInstance().getBluetoothAdapter();

	private final static UUID MY_UUID = UUID.fromString("73A06ACC-C5B6-4af5-AAB0-AA9C14F7DAC4");

	public BlueToothTask()
	{
		if (null != bluetoothAdapter)
		{
			ACCEPT_DEVICE_NAME = bluetoothAdapter.getName();
		}
		else
		{
			ACCEPT_DEVICE_NAME = "";
		}
	}

	private class AcceptRunnable implements Runnable
	{
		private BluetoothServerSocket serverSocket;

		private BluetoothSocket socket;

		public AcceptRunnable()
		{
			try
			{
				serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(ACCEPT_DEVICE_NAME, MY_UUID);
			}
			catch (IOException e)
			{
				LogFileUtil.e(TAG, "AcceptRunnable construct IOException", e);
			}
		}

		@Override
		public void run()
		{
			if (null != serverSocket)
			{
				try
				{
					socket = serverSocket.accept();
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "AcceptRunnable run IOException", e);
				}
			}
		}

		public void cancel()
		{
			if (null != socket)
			{
				try
				{
					socket.close();
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "AcceptRunnable cancel IOException", e);
				}
			}
		}
	}

	private class ConnectRunnable implements Runnable
	{
		private BluetoothSocket socket;

		private BluetoothDevice device;

		public ConnectRunnable(BluetoothDevice device)
		{
			this.device = device;
		}

		@Override
		public void run()
		{
			if (null != device)
			{
				try
				{
					socket = device.createRfcommSocketToServiceRecord(MY_UUID);
					if (null != socket)
					{
						socket.connect();
					}
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "ConnectRunnable run IOException", e);
				}
			}
		}

		public void cancel()
		{
			if (null != socket)
			{
				try
				{
					socket.close();
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "ConnectRunnable cancel IOException", e);
				}
			}
		}
	}

	private boolean sendMessage(BluetoothSocket socket, byte[] buffer)
	{
		if (null != socket)
		{
			try
			{
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(buffer);

				return true;
			}
			catch (IOException e)
			{
				LogFileUtil.e(TAG, "sendMessage IOException", e);
			}
		}
		return false;
	}

	private byte[] readMessage(BluetoothSocket socket)
	{
		byte[] result = null;
		if (null != socket)
		{
			byte[] buffer = new byte[4096];
			int length;
			try
			{
				InputStream inputStream = socket.getInputStream();
				length = inputStream.read(buffer);  // length 长度， buffer 内容

				System.arraycopy(buffer, 0, result, 0, length);
			}
			catch (IOException e)
			{
				LogFileUtil.e(TAG, "readMessage IOException", e);
			}
		}
		return result;
	}
}
