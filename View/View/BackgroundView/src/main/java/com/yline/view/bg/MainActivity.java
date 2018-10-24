package com.yline.view.bg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yline.base.BaseActivity;
import com.yline.view.bg.view.BankCardBgView;

public class MainActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BankCardBgView bankCardBgView = findViewById(R.id.main_bank_card);
		bankCardBgView.setBitmapResource(R.drawable.bank_1);
	}
}
