package com.uml;

import android.os.Bundle;
import android.view.View;

import com.uml.aggregation.AggregationActivity;
import com.uml.assocation.AssocationActivity;
import com.uml.composition.CompositionActivity;
import com.uml.dependency.DependencyActivity;
import com.uml.inheritance.PhoneWindow;
import com.uml.realization.RealizationActivity;
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
                RealizationActivity.launcher(MainActivity.this);
            }
        });

        addButton("association - 引用(关联)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssocationActivity.launcher(MainActivity.this);
            }
        });

        addButton("aggregation - 聚合", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AggregationActivity.launcher(MainActivity.this);
            }
        });

        addButton("inheritance - 继承", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhoneWindow();
            }
        });

        addButton("dependency - 依赖", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DependencyActivity.launcher(MainActivity.this);
            }
        });
    }
}
