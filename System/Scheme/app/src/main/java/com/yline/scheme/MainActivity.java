package com.yline.scheme;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("activity", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemeUtils.startActivity(MainActivity.this, "/query?search=fuck");
			}
		});
		
		addButton("service", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemeUtils.startService(MainActivity.this, "/query?search=fuck");
			}
		});
		
		addButton("receiver", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemeUtils.startReceiver(MainActivity.this, "/query?search=fuck");
			}
		});
	}
}
