package com.yline.annotation.runtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yline.annotation.runtime.processor.RuntimeAnnotation;
import com.yline.annotation.runtime.processor.RuntimeAnnotationImpl;
import com.yline.utils.LogUtil;

public class MainActivity extends AppCompatActivity {
    @RuntimeAnnotation("Jay Zhou")
    private String mString;

    @RuntimeAnnotation()
    private int mInt;

    @RuntimeAnnotation("not match")
    private float mFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printFiled();
        RuntimeAnnotationImpl.init(this);
        printFiled();
    }

    private void printFiled() {
        LogUtil.v("int = " + mInt + ", mString = " + mString + ", mFloat = " + mFloat);
    }
}
