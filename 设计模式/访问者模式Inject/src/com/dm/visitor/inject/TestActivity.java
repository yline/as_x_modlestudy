package com.dm.visitor.inject;

import org.simple.injector.anno.ViewInjector;

import android.os.Bundle;
import android.widget.TextView;

import com.yline.base.BaseActivity;

public class TestActivity extends BaseActivity
{
    
    @ViewInjector(R.id.my_tv)
    protected TextView mTextView;
    
    @ViewInjector(R.id.my_tv2)
    protected TextView mTextView2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTextView.setText("文字1");
        mTextView2.setText("文字2");
    }
    
}
