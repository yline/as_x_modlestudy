package com.system.app.activity;

import com.system.app.R;
import com.system.app.caller.CallHelper;
import com.system.app.contacter.ContacterHelper;
import com.system.app.msg.MsgHelper;
import com.system.app.wifi.WifiHelper;
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
        
        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(AppConstant.TAG_CLICK, "btn_call");
                new CallHelper().call(MainActivity.this, "563850");
            }
        });
        
        findViewById(R.id.btn_contacter_query).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(AppConstant.TAG_CLICK, "btn_contacter_query");
                new ContacterHelper().queryContacter(MainApplication.getApplication());
            }
        });
        
        findViewById(R.id.btn_contacter_insert).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(AppConstant.TAG_CLICK, "btn_contacter_insert");
                new ContacterHelper().insertContacter(MainApplication.getApplication());
            }
        });
        
        new MsgHelper().registerMsgobserver(MainApplication.getApplication());
        findViewById(R.id.btn_msg_send).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(AppConstant.TAG_CLICK, "btn_msg_send");
                new MsgHelper().sendMsg();
            }
        });
        
        findViewById(R.id.btn_msg_read).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(AppConstant.TAG_CLICK, "btn_msg_read");
                new MsgHelper().readMsgAll(MainActivity.this);
            }
        });
        
        findViewById(R.id.btn_wifi_test).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(AppConstant.TAG_CLICK, "btn_wifi_test");
                new WifiHelper().testWifi(MainActivity.this);
            }
        });
    }
    
}
