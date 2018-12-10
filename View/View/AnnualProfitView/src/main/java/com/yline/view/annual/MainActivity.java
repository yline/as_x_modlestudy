package com.yline.view.annual;

import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.view.annual.model.RateModel;

import java.util.Arrays;

public class MainActivity extends BaseAppCompatActivity {
	private final float[] yValues = {3.782f, 3.100f, 2.900f, 3.150f, 2.950f, 3.500f, 3.783f};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AnnualProfitView annualProfitView = findViewById(R.id.main_annual_profit);
		annualProfitView.setData(Arrays.asList(new RateModel("1125", 3.782f),
				new RateModel("1125", 3.100f),
				new RateModel("1125", 2.900f),
				new RateModel("1125", 3.150f),
				new RateModel("1125", 2.950f),
				new RateModel("1125", 3.500f),
				new RateModel("1125", 3.783f)
		));
	}
}
