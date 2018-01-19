package com.view.vscroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.view.vscroll.view.VerticalScrollTextView;

public class MainActivity extends AppCompatActivity {
    private VerticalScrollTextView mVScrollTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVScrollTextView = findViewById(R.id.main_vertical_scroll_textview);
        mVScrollTextView.startAnimation();
    }

    @Override
    protected void onDestroy() {
        mVScrollTextView.stopAnimation();
        super.onDestroy();
    }
}
