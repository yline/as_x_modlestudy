package com.view.wheel.area;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.view.wheel.R;
import com.view.wheel.area.view.WheelAreaPicker;
import com.yline.base.BaseAppCompatActivity;

/**
 * 地区
 *
 * @author yline 2018/3/5 -- 15:31
 * @version 1.0.0
 */
public class AreaActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, AreaActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        WheelAreaPicker areaPicker = findViewById(R.id.area_wheel_picker);
    }
}
