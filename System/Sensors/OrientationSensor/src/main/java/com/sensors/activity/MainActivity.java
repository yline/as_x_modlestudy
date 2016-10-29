package com.sensors.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.sensors.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

import java.math.BigDecimal;

public class MainActivity extends BaseAppCompatActivity
{
	private TextView tvTest;

	private SensorManager sensorManager;

	private SensorListener sensorListener;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvTest = (TextView) findViewById(R.id.tv_test);

		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		sensorListener = new SensorListener();
		sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
	}

	private class SensorListener implements SensorEventListener
	{
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			float angle = event.values[0];
			float afterAngle = BigDecimal.valueOf(angle).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

			LogFileUtil.v("angle : " + afterAngle);
			tvTest.setText("angle : " + afterAngle);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	}

	@Override
	protected void onDestroy()
	{
		sensorManager.unregisterListener(sensorListener);
		super.onDestroy();
	}
}
