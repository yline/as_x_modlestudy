package com.yline.view.annual;

import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity {
	private String[] axisXValues = {"01-01", "01-02", "01-03", "01-04", "01-05", "01-05", "01-06"}; // X轴 标注
	private final float[] yValues = {3.782f, 3.100f, 2.900f, 3.150f, 2.950f, 3.500f, 3.783f};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AnnualProfitView annualProfitView = findViewById(R.id.main_annual_profit);
		annualProfitView.setData(yValues, axisXValues);
	}
}
