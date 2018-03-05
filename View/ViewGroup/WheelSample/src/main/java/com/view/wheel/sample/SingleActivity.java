package com.view.wheel.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.view.wheel.R;
import com.wheel.lib.WheelPicker;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;

/**
 * 单个轮播器
 *
 * @author yline 2018/3/5 -- 14:58
 * @version 1.0.0
 */
public class SingleActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SingleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        WheelPicker wheelPicker = findViewById(R.id.single_wheel_picker);
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                SDKManager.toast("select:" + String.valueOf(data));
            }
        });
    }
}
