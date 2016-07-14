package com.dm.facade.in;

import com.dm.facade.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class CameraSamsung implements Camera
{
    
    @Override
    public void open()
    {
        LogFileUtil.v(MainApplication.TAG, "CameraSamsung -> open");
    }
    
    @Override
    public void takePicture()
    {
        LogFileUtil.v(MainApplication.TAG, "CameraSamsung -> takePicture");
    }
    
    @Override
    public void close()
    {
        LogFileUtil.v(MainApplication.TAG, "CameraSamsung -> close");
    }
    
}
