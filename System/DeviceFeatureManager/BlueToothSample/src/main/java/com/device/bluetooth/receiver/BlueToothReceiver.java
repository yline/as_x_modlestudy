package com.device.bluetooth.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.yline.base.BaseReceiver;
import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/11/4.
 */
public class BlueToothReceiver extends BaseReceiver
{
	private static final String TAG = BlueToothReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		String action = intent.getAction();
		LogFileUtil.v(TAG, "action = " + action);

		if (null != action)
		{
			// 蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播
			if (action.equals(BluetoothDevice.ACTION_FOUND))
			{
				if (null != bluetoothBondCallback)
				{
					// 获取查找到的蓝牙设备
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					bluetoothBondCallback.scanFinish(device);
				}
			}
			// 蓝牙状态值发生改变
			else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
			{

			}
			//  蓝牙扫描状态(SCAN_MODE)发生改变
			else if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED))
			{

			}
			// 蓝牙扫描过程开始
			else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
			{
				if (null != bluetoothBondCallback)
				{
					bluetoothBondCallback.scanStart();
				}
			}
			// 蓝牙扫描过程结束
			else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
			{
				if (null != bluetoothBondCallback)
				{
					bluetoothBondCallback.scanFinish(null);
				}
			}
			// 蓝牙设备Name发生改变
			else if (action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED))
			{

			}
			// 请求用户选择是否使该蓝牙能被扫描
			else if (action.equals(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
			{

			}
			// 请求用户选择是否打开蓝牙
			else if (action.equals(BluetoothAdapter.ACTION_REQUEST_ENABLE))
			{
				
			}
			// 蓝牙配对状态改变
			else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				switch (device.getBondState())
				{
					case BluetoothDevice.BOND_BONDING: // 正在配对
						if (null != bluetoothBondCallback)
						{
							bluetoothBondCallback.bonding();
						}
						break;
					case BluetoothDevice.BOND_BONDED: // 配对结束
						if (null != bluetoothBondCallback)
						{
							bluetoothBondCallback.bondFinish(device, true);
						}
						break;
					case BluetoothDevice.BOND_NONE: // 取消配对/未配对
						if (null != bluetoothBondCallback)
						{
							bluetoothBondCallback.bondFinish(device, false);
						}
						break;
				}
			}
			else
			{
				LogFileUtil.v(TAG, "action is not belong to we have know");
			}
		}
	}

	private static BluetoothBondCallback bluetoothBondCallback;

	public static void setBluetoothScanCallback(BluetoothBondCallback callback)
	{
		BlueToothReceiver.bluetoothBondCallback = callback;
	}

	public interface BluetoothBondCallback
	{
		void scanStart();

		/** null,则说明扫描结束 */
		void scanFinish(BluetoothDevice device);

		void bonding();

		void bondFinish(BluetoothDevice device, boolean result);
	}
}
