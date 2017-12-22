package com.coordinator.floatingaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.coordinator.R;
import com.yline.base.BaseAppCompatActivity;

/**
 * FloatActionBar
 *
 * @author yline 2017/12/22 -- 13:35
 * @version 1.0.0
 */
public class FloatingActionActivity extends BaseAppCompatActivity {

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, FloatingActionActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_action);

        findViewById(R.id.float_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "floating_action", Snackbar.LENGTH_LONG).setAction("cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 这里的单击事件代表点击消除Action后的响应事件
                    }
                }).show();
            }
        });
    }

}
