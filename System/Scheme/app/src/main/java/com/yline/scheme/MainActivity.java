package com.yline.scheme;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		final String a1 = "https://blog.csdn.net/janice0529/article/details/49082859?locationNum=1&fps=1";
		addButton(a1, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.schemePrint(Uri.parse(a1));
			}
		});
		
		final String a2 = "https://www.cnblogs.com/android-zcq/p/5882012.html";
		addButton(a2, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.schemePrint(Uri.parse(a2));
			}
		});
	}
}
