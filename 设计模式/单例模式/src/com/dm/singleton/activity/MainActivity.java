package com.dm.singleton.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dm.singleton.R;
import com.dm.singleton.dcl.SingletonDCL;
import com.dm.singleton.enums.SingletonEnum;
import com.dm.singleton.lazy.SingletonLazy;
import com.dm.singleton.statics.SingletonStatic;
import com.yline.log.LogFileUtil;

public class MainActivity extends Activity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
    }
    
    private void initView()
    {
        findViewById(R.id.btn_lazy).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "initView -> btn_lazy");
                SingletonLazy.getInstance().doSome();
            }
        });
        
        findViewById(R.id.btn_dcl).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "initView -> btn_dcl");
                SingletonDCL.getInstance().doSome();
            }
        });
        
        findViewById(R.id.btn_static).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "initView -> btn_static");
                SingletonStatic.getInstance().doSome();
            }
        });
        
        findViewById(R.id.btn_enum).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "initView -> btn_enum");
                SingletonEnum.INSTANCE.doSome();
            }
        });
        
        findViewById(R.id.btn_manager).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "initView -> btn_static");
                LogFileUtil.v(MainApplication.TAG, "该项,直接看代码,不进行测试");
            }
        });
    }
}
