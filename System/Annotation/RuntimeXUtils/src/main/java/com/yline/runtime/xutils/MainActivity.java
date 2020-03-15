package com.yline.runtime.xutils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yline.runtime.xutils.lib.ViewInjectorManager;
import com.yline.runtime.xutils.lib.utils.LogIoc;
import com.yline.runtime.xutils.lib.view.annotation.ContentView;
import com.yline.runtime.xutils.lib.view.annotation.Event;
import com.yline.runtime.xutils.lib.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.main_tv)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewInjectorManager.getInstance().inject(this);
        mTextView.setText("Main After");
    }

    @Event(R.id.main_btn)
    private void onJump2Second(View view) {
        LogIoc.v("");
        SecondActivity.launch(this);
    }
}
