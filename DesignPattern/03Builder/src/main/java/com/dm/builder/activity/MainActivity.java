package com.dm.builder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.design.pattern.builder.R;
import com.dm.builder.builder.Builder;
import com.dm.builder.builder.MacBookBuilder;
import com.dm.builder.product.Computer;
import com.yline.log.LogFileUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_buidler).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 构建器,包含产品信息
                Builder builder = new MacBookBuilder();

                Computer computer = builder.create("英特尔主板", "Retina 显示器");

                // 构建计算机,输出相关信息,builder.create()就是对象
                LogFileUtil.v(MainApplication.TAG, "Computer info : " + computer.toString());
            }
        });
    }
}
