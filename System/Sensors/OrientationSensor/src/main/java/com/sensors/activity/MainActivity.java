package com.sensors.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sensors.R;
import com.sensors.SensorHelper;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private SensorManager sensorManager;

	private SensorListener sensorListener;

	private SensorHelper sensorHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sensorHelper = new SensorHelper(MainActivity.this);

		// 指南针,老的方式
		final TextView tvOrientationOld = (TextView) findViewById(R.id.tv_orientation_old);
		findViewById(R.id.btn_orientation_old).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_orientation_old");
				sensorHelper.registerOrientationOldListener(new SensorEventListener()
				{
					@Override
					public void onSensorChanged(SensorEvent event)
					{
						float afterAngle = sensorHelper.dealWithOrientationOld(event);
						tvOrientationOld.setText("afterAngle : " + afterAngle);
					}

					@Override // 当传感器的精度发生变化时就会调用
					public void onAccuracyChanged(Sensor sensor, int accuracy)
					{

					}
				});
			}
		});

		// 指南针,新的方式
		final TextView tvOrientationNew = (TextView) findViewById(R.id.tv_orientation_new);
		findViewById(R.id.btn_orientation_new).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_orientation_new");
				sensorHelper.registerOrientationNewListener(new SensorEventListener()
				{

					@Override
					public void onSensorChanged(SensorEvent event)
					{
						double afterAngle = sensorHelper.dealWithOrientationNew(event);
						tvOrientationNew.setText("afterAngle : " + afterAngle);
					}

					@Override // 当传感器的精度发生变化时就会调用
					public void onAccuracyChanged(Sensor sensor, int accuracy)
					{

					}
				});
			}
		});

		// 加速度传感器
		final TextView tvAccelerometer = (TextView) findViewById(R.id.tv_accelerometer);
		findViewById(R.id.btn_accelerometer).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				LogFileUtil.v("btn_accelerometer");
				sensorHelper.registerAccelerometerListener(new SensorEventListener()
				{
					@Override
					public void onSensorChanged(SensorEvent event)
					{
						float value = sensorHelper.dealWithAccelerometer(event);
						tvAccelerometer.setText("value = " + value);
					}

					@Override
					public void onAccuracyChanged(Sensor sensor, int accuracy)
					{

					}
				});
			}
		});

		// 光传感器
		final TextView tvLight = (TextView) findViewById(R.id.tv_light);
		findViewById(R.id.btn_light).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_light");
				sensorHelper.registerLightListener(new SensorEventListener()
				{
					@Override
					public void onSensorChanged(SensorEvent event)
					{
						float value = sensorHelper.dealWithLight(event);
						tvLight.setText("value = " + value);
					}

					@Override
					public void onAccuracyChanged(Sensor sensor, int accuracy)
					{

					}
				});
			}
		});
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		sensorHelper.unRegisterListener();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
