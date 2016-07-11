package com.aidl.activity;

import com.aidl.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_aidl).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_aidl");
                LogFileUtil.i(MainApplication.TAG, "aidl 免测试,原因:需要2个工程");
            }
        });
    }
    
}
