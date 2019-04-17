package com.system.app.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * wifi管理 todo 测试
 *
 * @author yline 2019/4/17 -- 18:13
 */
public class WifiActivity extends BaseTestActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, WifiActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
		/*findViewById(R.id.btn_wifi_test).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new WifiHelper().testWifi(MainActivity.this);
			}
		});*/
    }
}
