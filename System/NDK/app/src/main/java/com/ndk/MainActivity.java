package com.ndk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ndk.jni.JniManager;

public class MainActivity extends AppCompatActivity {
    private JniManager mJniManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJniManager = new JniManager();

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(mJniManager.stringFromJNI() + "\n" + mJniManager.logByJni("Android->JNI"));
    }
}
