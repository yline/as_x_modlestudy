package com.json.activity;

import com.json.R;
import com.json.gson.parse.WifiPolicyParse;
import com.yline.log.LogFileUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
    private static final String TAG = "json";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_gson).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(TAG, "onClick btn_gson");
                new WifiPolicyParse().test(WifiPolicyParse.JsonStr);
            }
        });
    }
    
}
