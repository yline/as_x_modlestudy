package com.dm.facade.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.facade.R;
import com.dm.facade.out.MobilePhone;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_facade).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                MobilePhone mobilePhone = new MobilePhone();
                mobilePhone.takePicture();
                mobilePhone.videoChat();
            }
        });
    }
    
}
