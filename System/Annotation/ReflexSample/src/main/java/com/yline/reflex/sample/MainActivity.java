package com.yline.reflex.sample;

import android.os.Bundle;
import android.view.View;

import com.yline.reflex.sample.construct.ReflexUtils;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Class的调用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflexUtils.doNewInstance();
            }
        });

        addButton("获取方法信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflexUtils.queryMethodInfo();
            }
        });

        addButton("获取成员变量信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflexUtils.queryFiledInfo();
            }
        });

        addButton("获取构造函数信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflexUtils.queryConstructInfo();
            }
        });

        addButton("调用某个类的方法", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReflexUtils.executeMethod();
            }
        });

        addButton("给变量赋值", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflexUtils.setField();
            }
        });
    }

}
