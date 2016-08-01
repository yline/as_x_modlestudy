package com.tools.activity;

import com.tools.R;
import com.tools.SystemSkipTool;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity
{
    
    private SystemSkipTool mSystemSkipTool;
    
    private static final int ALBUM_PICK = 1;
    
    private static final int AUDIO_PICK = 2;
    
    private static final int FILE_CHOOSE = 3;
    
    private static final int SETTING_WIFI = 4;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mSystemSkipTool = new SystemSkipTool();
        
        findViewById(R.id.btn_brower_web).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "btn_brower_web");
                
                mSystemSkipTool.openBrower(MainActivity.this, "www.baidu.com");
            }
        });
        
        findViewById(R.id.btn_album_pick).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "btn_album_pick");
                
                mSystemSkipTool.openAlbum(MainActivity.this, ALBUM_PICK);
            }
        });
        
        findViewById(R.id.btn_audio_pick).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "btn_audio_pick");
                
                mSystemSkipTool.openAudio(MainActivity.this, AUDIO_PICK);
            }
        });
        
        findViewById(R.id.btn_file_choose).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "btn_file_choose");
                
                mSystemSkipTool.openFile(MainActivity.this, FILE_CHOOSE);
            }
        });
        
        findViewById(R.id.btn_setting_wifi).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "btn_setting_wifi");
                mSystemSkipTool.openSetting(MainActivity.this, SETTING_WIFI);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        LogFileUtil.v(MainApplication.TAG, "onActivityResult data is null ? -> " + (data == null));
        if (null != data)
        {
            switch (requestCode)
            {
                case ALBUM_PICK:
                    LogFileUtil.v(MainApplication.TAG, "ALBUM_PICK -> " + data.getExtras());
                    break;
                case AUDIO_PICK:
                    LogFileUtil.v(MainApplication.TAG, "AUDIO_PICK -> " + data.getExtras());
                    break;
                case FILE_CHOOSE:
                    LogFileUtil.v(MainApplication.TAG, "FILE_CHOOSE -> " + data.getExtras());
                    break;
                case SETTING_WIFI:
                    LogFileUtil.v(MainApplication.TAG, "SETTING_WIFI -> " + data.getExtras());
                    break;
                default:
                    LogFileUtil.e(MainApplication.TAG, "onActivityResult requestCode exception");
                    break;
            }
        }
    }
    
}
