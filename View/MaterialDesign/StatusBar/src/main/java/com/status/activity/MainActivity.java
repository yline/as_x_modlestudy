package com.status.activity;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("Translucent 全透明", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TranslucentActivity.launch(MainActivity.this, true);
			}
		});
		
		addButton("Translucent 半透明", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TranslucentActivity.launch(MainActivity.this, false);
			}
		});
		
		addButton("Translucent Image", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageActivity.launch(MainActivity.this);
			}
		});
		
		addButton("Translucent DrawerLayout", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DrawerActivity.launch(MainActivity.this);
			}
		});
		
		addButton("Color", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorActivity.launch(MainActivity.this);
			}
		});
		
		addButton("Color SwipeBack", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SwipeBackActivity.launch(MainActivity.this);
			}
		});
		
		addButton("Translucent&Color Drawer", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DrawerActivity.launch(MainActivity.this);
			}
		});
	}
}
