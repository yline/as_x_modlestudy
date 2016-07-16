package com.utils.activity;

import com.utils.AppUtil;
import com.utils.DensityUtil;
import com.utils.KeyBoardUtil;
import com.utils.MD5Util;
import com.utils.R;
import com.utils.ScreenUtil;
import com.utils.TimeStampUtil;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_app_util).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_app_util");
                
                String appName = AppUtil.getAppName(MainApplication.getApplication());
                String versionName = AppUtil.getVersionName(MainApplication.getApplication());
                LogFileUtil.i(MainApplication.TAG, "appName = " + appName + ",versionName = " + versionName);
            }
        });
        
        findViewById(R.id.btn_density_util).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_density_util");
                
                int dp = DensityUtil.dp2px(MainApplication.getApplication(), 100);
                int sp = DensityUtil.sp2px(MainApplication.getApplication(), 100);
                float pxd = DensityUtil.px2dp(MainApplication.getApplication(), 100);
                float pxs = DensityUtil.px2sp(MainApplication.getApplication(), 100);
                LogFileUtil.i(MainApplication.TAG, "dp = " + dp + ",sp = " + sp + ",pxd = " + pxd + ",pxs = " + pxs);
            }
        });
        
        // 软键盘
        final EditText etKeyBoard = (EditText)findViewById(R.id.et_keyboard_util);
        findViewById(R.id.btn_keyboard_open).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_keyboard_open");
                
                KeyBoardUtil.openKeybord(MainActivity.this, etKeyBoard);
                
            }
        });
        findViewById(R.id.btn_keyboard_close).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_keyboard_close");
                
                KeyBoardUtil.closeKeybord(MainActivity.this, etKeyBoard);
            }
        });
        
        findViewById(R.id.btn_md5_util).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_md5_util");
                
                byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
                String hexByBytes = MD5Util.toHexString(bytes);
                String md5ByString = MD5Util.md5(hexByBytes);
                LogFileUtil.v(MainApplication.TAG, "hexByBytes = " + hexByBytes + ",md5ByString = " + md5ByString);
            }
        });
        
        findViewById(R.id.btn_screen_util).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_screen_util");
                
                int screenWidth = ScreenUtil.getScreenWidth(MainApplication.getApplication());
                int screenHeight = ScreenUtil.getScreenHeight(MainApplication.getApplication());
                int statusheight = ScreenUtil.getStatusHeight(MainApplication.getApplication());
                LogFileUtil.v(MainApplication.TAG,
                    "screenWidth = " + screenWidth + ",screenHeight = " + screenHeight + ",statusheight = "
                        + statusheight);
                
                Bitmap bitmap1 = ScreenUtil.snapShotWithStatusBar(MainActivity.this);
                LogFileUtil.v(MainApplication.TAG,
                    "getWidth1 = " + bitmap1.getWidth() + ",getHeight1 = " + bitmap1.getHeight());
                
                Bitmap bitmap2 = ScreenUtil.snapShotWithoutStatusBar(MainActivity.this);
                LogFileUtil.v(MainApplication.TAG,
                    "getWidth2 = " + bitmap2.getWidth() + ",getHeight2 = " + bitmap2.getHeight());
            }
        });
        
        findViewById(R.id.btn_timestamp_util).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_timestamp_util");
                
                long currentStamp = TimeStampUtil.getCurrentStamp();
                String timeStandard = TimeStampUtil.getTimeStandard(currentStamp);
                LogFileUtil.v(MainApplication.TAG,
                    "currentStamp = " + currentStamp + ",timeStandard = " + timeStandard);
                
                int year = TimeStampUtil.getYear(currentStamp);
                int month = TimeStampUtil.getMonth(currentStamp);
                int day = TimeStampUtil.getDay(currentStamp);
                int hour = TimeStampUtil.getHour(currentStamp);
                int minute = TimeStampUtil.getMinute(currentStamp);
                int second = TimeStampUtil.getSecond(currentStamp);
                int dayOfWeekEnglish = TimeStampUtil.getDayOfWeekEnglish(currentStamp);
                int dayOfWeek = TimeStampUtil.getDayOfWeek(currentStamp);
                LogFileUtil.v(MainApplication.TAG,
                    "year = " + year + ",month = " + month + "day = " + day + ",hour = " + hour + ",minute = " + minute
                        + ",second = " + second + ",dayOfWeekEnglish = " + dayOfWeekEnglish + ",dayOfWeek = "
                        + dayOfWeek);
                
                long diffStamp = TimeStampUtil.getDiffStamp(currentStamp);
                boolean isStampTimeOut = TimeStampUtil.isStampTimeOut(currentStamp, 20);
                LogFileUtil.v(MainApplication.TAG, "diffStamp = " + diffStamp + ",isStampTimeOut = " + isStampTimeOut);
            }
        });
    }
    
}
