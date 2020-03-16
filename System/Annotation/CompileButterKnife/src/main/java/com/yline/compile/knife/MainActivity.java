package com.yline.compile.knife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.yline.compile.knife.lib.view.annotation.BindView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_tv)
    private TextView mMainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 调用，实现注解的初始化
        ButterKnife.bind(this);
    }
}
