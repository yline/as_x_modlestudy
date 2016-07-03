package com.https.activity;

import com.https.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity
{
    private static final String WEB_PROJECT_NAME = "WebHttps";
    
    private EditText mEtInputIp, mEtInputClass;
    
    private Button mBtnGetConnectionSubmit, mBtnPostConnectionSubmit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        initData();
    }
    
    private void initView()
    {
        mEtInputIp = (EditText)findViewById(R.id.et_input_ip);
        mEtInputClass = (EditText)findViewById(R.id.et_input_class);
        mBtnGetConnectionSubmit = (Button)findViewById(R.id.btn_get_connection);
        mBtnPostConnectionSubmit = (Button)findViewById(R.id.btn_post_connection);
    }
    
    private void initData()
    {
        mBtnGetConnectionSubmit.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                
            }
        });
        
        mBtnPostConnectionSubmit.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                
            }
        });
    }
}
