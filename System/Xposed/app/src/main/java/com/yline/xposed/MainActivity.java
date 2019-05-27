package com.yline.xposed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
                Log.v("xxx-", "str = " + str);
                mTextView.setText(str);
            }
        });
    }

    private String getXposedString() {
        return "native _ android";
    }
}
