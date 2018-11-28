package com.yline.custom;

import android.os.Bundle;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.custom.circle.CircleActivity;
import com.yline.custom.circle.HttpLoadingDialog;
import com.yline.custom.horizontal.HorizontalActivity;
import com.yline.custom.system.SystemActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("System", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SystemActivity.launch(MainActivity.this);
			}
		});
		
		addButton("Horizontal", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HorizontalActivity.launch(MainActivity.this);
			}
		});
		
		addButton("Circle", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CircleActivity.launch(MainActivity.this);
			}
		});
		
		addButton("CircleDialog", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final HttpLoadingDialog loadingDialog = new HttpLoadingDialog();
				loadingDialog.show(MainActivity.this);
				
				SDKManager.getHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						loadingDialog.dismiss();
					}
				}, 10500);
			}
		});
	}
}
