package com.view.waveview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.view.R;
import com.view.waveview.WaveOffsetView;
import com.view.waveview.WavePhaseView;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	private WaveOffsetView waveOffsetView;

	private WavePhaseView wavePhaseView;

	private Button btnSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		waveOffsetView = (WaveOffsetView) findViewById(R.id.view_wave);
		waveOffsetView.setWaveSpeed(3, 5);
		waveOffsetView.setWaveType(WaveOffsetView.WAVETYPE.DEGREE);
		waveOffsetView.setCycleNumber(2.0f);
		waveOffsetView.setWaveMaxHeight(50);

		wavePhaseView = (WavePhaseView) findViewById(R.id.view_wave_phase);
		wavePhaseView.setWaveSpeed(2.0f, 3.09f);
		wavePhaseView.setWaveColor(0x99090909);
		wavePhaseView.setCycleNumber(1.3f);
		wavePhaseView.setWaveMaxHeight(30);

		btnSwitch = (Button) findViewById(R.id.btn_switch);
		btnSwitch.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (waveOffsetView.getVisibility() == View.VISIBLE)
				{
					waveOffsetView.setVisibility(View.GONE);
					wavePhaseView.setVisibility(View.VISIBLE);
					btnSwitch.setText("切换图片\nnow - WavePhaseView");
				}
				else
				{
					waveOffsetView.setVisibility(View.VISIBLE);
					wavePhaseView.setVisibility(View.GONE);
					btnSwitch.setText("切换图片\nnow - waveOffsetView");
				}
			}
		});
	}
}
