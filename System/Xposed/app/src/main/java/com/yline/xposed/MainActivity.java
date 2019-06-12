package com.yline.xposed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yline.utils.LogUtil;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.main_text);
        findViewById(R.id.main_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = getXposedString();
                LogUtil.v("str = " + str);
                mTextView.setText(str);
            }
        });

        LogUtil.v("" + getClass().getName());
    }

    public String getXposedString() {
        LogUtil.v("getXposedString");
        return "native _ android";
    }
}
