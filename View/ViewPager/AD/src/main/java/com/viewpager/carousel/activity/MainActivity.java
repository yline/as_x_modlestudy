package com.viewpager.carousel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.view.viewpager.carousel.R;
import com.viewpager.carousel.widget.ADView;
import com.yline.application.BaseApplication;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.UrlConstant;
import com.yline.utils.LogUtil;
import com.yline.view.fresco.FrescoManager;
import com.yline.view.fresco.view.FrescoView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ADView adView = findViewById(R.id.main_ad);
		adView.setOnPageListener(new ADView.OnPageListener<String>() {
			@Override
			public void onPageClick(View v, String s, int position) {
				BaseApplication.toast("position = " + position);
			}
			
			@Override
			public void onPageInstance(FrescoView frescoView, String s) {
				LogUtil.v("s = " + s);
				FrescoManager.setImageUri(frescoView, s);
			}
		});
		List<String> dataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				dataList.add(UrlConstant.getJpg_960_640(i));
			} else {
				dataList.add(UrlConstant.getJpn_1920_1280(i));
			}
		}
		adView.setData(dataList);
	}
}
