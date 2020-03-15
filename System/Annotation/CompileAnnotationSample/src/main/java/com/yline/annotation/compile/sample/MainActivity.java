package com.yline.annotation.compile.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.yline.annotation.compile.CompileAnnotation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test_a();
    }

    @CompileAnnotation
    public void test_a() {
        Log.v("xxx-", "test_a");
    }
}
