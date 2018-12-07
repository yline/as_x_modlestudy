package com.yline.scheme.third.export;

import android.os.Bundle;
import android.view.View;

import com.yline.scheme.third.util.Utils;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

public class ExportActivity extends BaseTestActivity {
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		if (null != getIntent()) {
			Utils.schemePrint(getIntent().getData());
		} else {
			LogUtil.v("intent is null");
		}
	}
}
