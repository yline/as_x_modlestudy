package com.view.wm;

import android.os.Bundle;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.LogUtil;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatService.registerCircleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("xxx-点悬浮框");
                IApplication.toast("点击悬浮框");
            }
        });
    }
}
