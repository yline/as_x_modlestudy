package com.dm.abstracts.factory.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.abstracts.factory.CarFactory;
import com.dm.abstracts.factory.Q3Factory;
import com.dm.abstracts.factory.Q7Factory;
import com.dm.abstracts.factory.R;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_Q3).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                CarFactory q3Factory = new Q3Factory();
                q3Factory.createTire().tire();
                q3Factory.createEngine().engine();
                q3Factory.createBrake().brake();
            }
        });
        
        findViewById(R.id.btn_Q7).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                CarFactory q7Factory = new Q7Factory();
                q7Factory.createTire().tire();
                q7Factory.createEngine().engine();
                q7Factory.createBrake().brake();
            }
        });
        
    }
    
}
