package com.http.activity;

import com.http.R;
import com.http.connection.get.util.GetConnectionUtil;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity
{
    private EditText mEtInputIp, mEtInputStr;
    
    private Button mBtnGetConnectionSubmit;
    
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
        mEtInputStr = (EditText)findViewById(R.id.et_input_end);
        mBtnGetConnectionSubmit = (Button)findViewById(R.id.btn_get_connection);
    }
    
    private void initData()
    {
        mBtnGetConnectionSubmit.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String ip = mEtInputIp.getText().toString().trim();
                String str = mEtInputStr.getText().toString().trim();
                LogFileUtil.v(MainApplication.TAG, "ip = " + ip + ",str = " + str);
                GetConnectionUtil.doLocal(ip, str, new GetConnectionUtil.GetConnectionCallback()
                {
                    
                    @Override
                    public void onSuccess(String result)
                    {
                        
                    }
                    
                    @Override
                    public void onError(Exception e)
                    {
                        
                    }
                });
            }
        });
    }
    
}
