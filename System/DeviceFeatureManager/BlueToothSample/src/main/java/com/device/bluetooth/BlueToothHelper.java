package com.device.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

import com.device.bluetooth.receiver.BlueToothReceiver;
import com.yline.log.LogFileUtil;

import java.util.Set;

/**
 * Created by yline on 2016/11/4.
 * 已配对设备、可配对设备
 */
public class BlueToothHelper
{
	public static final String TAG = "BlueToothHelper";

	private BluetoothAdapter bluetoothAdapter;

	private BlueToothHelper()
	{
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public static BlueToothHelper getInstance()
	{
		return HelpHolder.sInstance;
	}

	private static class HelpHolder
	{
		private static BlueToothHelper sInstance = new BlueToothHelper();
	}

	public BluetoothAdapter getBluetoothAdapter()
	{
		return bluetoothAdapter;
	}

	/**
	 * 打印当前蓝牙信息
	 * 包含:自己的name、device;其他的name、device
	 */
	public void logBlueToothInfo()
	{
		if (null != bluetoothAdapter)
		{
			String name = bluetoothAdapter.getName();
			String address = bluetoothAdapter.getAddress();
			LogFileUtil.v(TAG, "mine device name = " + name + ",address = " + address);

			Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("bonded devices -> ");
			if (null != bondedDevices)
			{
				strBuffer.append("length = " + bondedDevices.size());
				for (BluetoothDevice device : bondedDevices)
				{
					strBuffer.append(",name = " + device.getName());
					strBuffer.append(",address = " + device.getAddress());
				}
			}
			else
			{
				strBuffer.append("null!");
			}
			LogFileUtil.v(TAG, strBuffer.toString());
		}
	}

	/**
	 * 判断手机中蓝牙是否存在
	 * @return
	 */
	public boolean isExist()
	{
		if (null != bluetoothAdapter)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断 蓝牙 是否打开
	 * @return
	 */
	public boolean isEnable()
	{
		if (null != bluetoothAdapter)
		{
			if (bluetoothAdapter.isEnabled())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 强制打开蓝牙(要求用户开启应用权限)
	 * @return
	 */
	public boolean enable()
	{
		if (null != bluetoothAdapter)
		{
			return bluetoothAdapter.enable();
		}
		return false;
	}

	/**
	 * 打开蓝牙(弹出对话框,请求用户打开)
	 * @param activity
	 * @param requestCode
	 */
	public void enable(Activity activity, int requestCode)
	{
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		activity.startActivityForResult(enableIntent, requestCode);
	}

	/**
	 * 跳转到系统设置页面
	 * @param context
	 */
	public void openBluetoothSetting(Context context)
	{
		context.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
	}

	/**
	 * 强制关闭蓝牙
	 * @return
	 */
	public boolean disable()
	{
		if (null != bluetoothAdapter)
		{
			return bluetoothAdapter.disable();
		}
		return false;
	}

	/**
	 * 是否在扫描蓝牙
	 * @return
	 */
	public boolean isScan()
	{
		if (null != bluetoothAdapter)
		{
			if (bluetoothAdapter.isDiscovering())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 开始扫描
	 * @return
	 */
	public boolean scanStart(BlueToothReceiver.BluetoothBondCallback callback)
	{
		boolean result = false;
		if (null != bluetoothAdapter)
		{
			result = bluetoothAdapter.startDiscovery();
			if (result)
			{
				BlueToothReceiver.setBluetoothScanCallback(callback);
			}
			return result;
		}
		return result;
	}

	/**
	 * 取消扫描;并不是立即生效
	 * @return
	 */
	public boolean scanCancel()
	{
		if (null != bluetoothAdapter)
		{
			return bluetoothAdapter.cancelDiscovery();
		}
		return false;
	}

	/**
	 * 发送消息
	 */
	public void sendMessage()
	{
		// TO DO
	}

	/**
	 * 接收消息
	 */
	public void registerReceiverMessage()
	{
		// TO DO
	}

	/**
	 * 地址信息,转化成设备信息
	 * @param address
	 * @return
	 */
	public BluetoothDevice address2Device(String address)
	{
		if (null != bluetoothAdapter)
		{
			return bluetoothAdapter.getRemoteDevice(address);
		}
		return null;
	}

	/**
	 * 重置 限制时间
	 * 若参数等于0,则设置为不可见
	 * @param context
	 * @param time    时间,单位s
	 */
	public void resetLimitTime(Context context, int time)
	{
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
		context.startActivity(discoverableIntent);
	}


	public void registerReceiver(Context context, BroadcastReceiver receiver)
	{
		IntentFilter intentFilter = new IntentFilter();

		// 蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		// 配对请求开始
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		// 蓝牙状态值发生改变
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 蓝牙扫描状态(SCAN_MODE)发生改变
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		// 蓝牙扫描过程开始
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		// 蓝牙扫描过程结束
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		// 蓝牙设备Name发生改变
		intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
		// 请求用户选择是否使该蓝牙能被扫描
		intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		// 请求用户选择是否打开蓝牙
		intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);

		context.registerReceiver(receiver, intentFilter);
	}
}
