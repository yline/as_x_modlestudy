package com.uml;

import android.os.Bundle;
import android.view.View;

import com.uml.composition.CompositionActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("composition - 组合", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompositionActivity.launcher(MainActivity.this);
            }
        });

        addButton("realization - 实现", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton("association - 引用(关联)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton("aggregation - 聚合", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton("inheritance - 继承", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton("dependency - 依赖", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
