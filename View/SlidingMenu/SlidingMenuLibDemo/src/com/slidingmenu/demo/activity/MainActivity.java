package com.slidingmenu.demo.activity;

import com.slidingmenu.demo.activity.bynew.NewActivity;
import com.slidingmenu.demo.activity.esliding.ESlidingActivity;
import com.slidingmenu.demo.activity.eslidingfragment.ESlidingFragmentActivity;
import com.view.slidingmenu.demo.R;
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
        
        findViewById(R.id.btn_bynew).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_bynew");
                NewActivity.actionStart(MainActivity.this);
            }
        });
        
        findViewById(R.id.btn_byextends_sliding).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_byextends_sliding");
                ESlidingActivity.actionStart(MainActivity.this);
            }
        });
        
        findViewById(R.id.btn_byextends_fragment).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_byextends_fragment");
                ESlidingFragmentActivity.actionStart(MainActivity.this);
            }
        });
    }
    
}
