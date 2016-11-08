package com.device.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.yline.log.LogFileUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 专注于蓝牙配对
 * @author yline 2016/11/8 --> 8:03
 * @version 1.0.0
 */
public class ClsUtils
{
	private static final String TAG = ClsUtils.class.getSimpleName();

	/**
	 * 开始  与设备配对
	 * 参考源码：platform/packages/apps/Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 * @param btClass  BluetoothDevice.getclass()
	 * @param btDevice BluetoothDevice
	 * @return 不作处理
	 * @throws Exception 不作处理
	 */
	static public boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception
	{
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * 解除  与设备配对
	 * 参考源码：platform/packages/apps/Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 * @param btClass  BluetoothDevice.getclass()
	 * @param btDevice BluetoothDevice
	 * @return 不作处理
	 * @throws Exception 不作处理
	 */
	static public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception
	{
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * 设置Pin,匹配关键字
	 * @param btClass  BluetoothDevice.getclass()
	 * @param btDevice BluetoothDevice
	 * @param str      匹配 的 字符串
	 * @return 不作处理
	 * @throws Exception 不作处理
	 */
	static public boolean setPin(Class<?> btClass, BluetoothDevice btDevice, String str) throws Exception
	{
		try
		{
			Method removeBondMethod = btClass.getDeclaredMethod("setPin",
					new Class[]{byte[].class});
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
					new Object[]{str.getBytes()});
			Log.e("returnValue", "" + returnValue);
		}
		catch (SecurityException e)
		{
			LogFileUtil.e(TAG, "setPin SecurityException", e);
		}
		catch (IllegalArgumentException e)
		{
			LogFileUtil.e(TAG, "setPin IllegalArgumentException", e);
		}
		catch (Exception e)
		{
			LogFileUtil.e(TAG, "setPin Exception", e);
		}
		return true;
	}

	/**
	 * 取消用户输入
	 * @param btClass BluetoothDevice.getclass()
	 * @param device  BluetoothDevice
	 * @return 不作处理
	 * @throws Exception 不作处理
	 */
	static public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception
	{
		Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
		// cancelBondProcess()
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	/**
	 * 取消配对
	 * @param btClass BluetoothDevice.getclass()
	 * @param device  BluetoothDevice
	 * @return 不作处理
	 * @throws Exception 不作处理
	 */
	static public boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception
	{
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	/**
	 * 用反射机制枚举BluetoothDevice的所有方法和常量
	 * @param clsShow BluetoothDevice.getclass()
	 */
	static public void printAllInform(Class clsShow)
	{
		try
		{
			// 取得所有方法
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++)
			{
				Log.e("method name", hideMethod[i].getName() + ";and the i is:" + i);
			}

			// 取得所有常量
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++)
			{
				Log.e("Field name", allFields[i].getName());
			}
		}
		catch (SecurityException e)
		{
			LogFileUtil.e(TAG, "setPin SecurityException", e);
		}
		catch (IllegalArgumentException e)
		{
			LogFileUtil.e(TAG, "setPin IllegalArgumentException", e);
		}
		catch (Exception e)
		{
			LogFileUtil.e(TAG, "setPin Exception", e);
		}
	}
}
