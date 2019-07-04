package com.yline.surface;

import android.os.Bundle;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.surface.surface.SurfaceViewTemplate;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurfaceViewTemplate view = findViewById(R.id.main_surface);
                view.reset();
            }
        });
    }
}
