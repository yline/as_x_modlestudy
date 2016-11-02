package com.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.sensors.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.math.BigDecimal;

/**
 * Created by yline on 2016/11/1.
 */
public class SensorHelper
{
	private SensorManager sensorManager;

	private SensorEventListener orientationOldListener;

	private SensorEventListener orientationNewListener;

	private SensorEventListener accelerometerListener;

	private SensorEventListener lightListener;

	public SensorHelper(Context context)
	{
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}

	/**
	 * 老方式;实现方向传感器
	 * @param listener
	 */
	public void registerOrientationOldListener(SensorEventListener listener)
	{
		this.orientationOldListener = listener;
		// 方向传感器
		Sensor orientationOldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(listener, orientationOldSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * 处理;老方式;方向传感器
	 * @param event
	 * @return
	 */
	public float dealWithOrientationOld(SensorEvent event)
	{
		float afterAngle = BigDecimal.valueOf(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		LogFileUtil.v("dealWithAccelerometer afterAngle : " + afterAngle);
		return afterAngle;
	}

	/**
	 * 新方式;实现方向(地磁)传感器
	 * @param listener
	 */
	public void registerOrientationNewListener(SensorEventListener listener)
	{
		this.orientationNewListener = listener;
		// 地磁传感器
		Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);    // 地磁传感器
		sensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * 处理;方向(地磁)传感器;单纯的不等于指南针的角度
	 * @param event
	 * @return
	 */
	public float dealWithOrientationNew(SensorEvent event)
	{
		/**
		 * values[0] 记录着手机围绕Z轴的旋转角度
		 * values[1] 记录着手机围绕X轴的旋转角度
		 * values[2] 记录着手机围绕Y轴的旋转角度
		 */
		float[] magneticValues = new float[3];

		// 判断当前是加速度传感器还是地磁传感器
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		{
			// 注意赋值时要调用clone()方法
			magneticValues = event.values.clone();
		}

		float afterAngle = magneticValues[0];
		LogFileUtil.v("dealWithAccelerometer afterAngle : " + afterAngle);
		return afterAngle;
	}

	/**
	 * 加速度传感器
	 * @param listener
	 */
	public void registerAccelerometerListener(SensorEventListener listener)
	{
		this.accelerometerListener = listener;
		// 加速度传感器
		Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 加速度传感器; 一直有值,只是要超过一定值
	 * @param event
	 */
	public float dealWithAccelerometer(SensorEvent event)
	{
		// 加速度可能会是负值，所以要取它们的绝对值
		float xValue = Math.abs(event.values[0]);
		float yValue = Math.abs(event.values[1]);
		float zValue = Math.abs(event.values[2]);
		if (xValue > 15 || yValue > 15 || zValue > 15)
		{  // 加速度 为 15 m/s^2
			// 认为用户摇动了手机，触发摇一摇逻辑
			MainApplication.toast("摇一摇");
		}

		float value = Math.max(Math.max(xValue, yValue), zValue);
		LogFileUtil.v("dealWithAccelerometer value : " + value);
		return value;
	}

	/**
	 * 光传感器
	 * @param listener
	 */
	public void registerLightListener(SensorEventListener listener)
	{
		this.lightListener = listener;
		// 光传感器
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		// 注册监听事件
		sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 光传感器
	 * @param event
	 * @return
	 */
	public float dealWithLight(SensorEvent event)
	{
		// values数组中第一个下标的值就是当前的光照强度	// 最大值 10000.0
		float value = event.values[0];
		LogFileUtil.v("dealWithLight Current light level is" + value + " lx");
		return value;
	}

	/**
	 * 注销已经注册的监听器
	 */
	public void unRegisterListener()
	{
		if (null != accelerometerListener)
		{
			sensorManager.unregisterListener(accelerometerListener);
		}

		if (null != orientationOldListener)
		{
			sensorManager.unregisterListener(orientationOldListener);
		}

		if (null != orientationNewListener)
		{
			sensorManager.unregisterListener(orientationNewListener);
		}

		if (null != lightListener)
		{
			sensorManager.unregisterListener(lightListener);
		}
	}
}
