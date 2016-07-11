package com.dm.state.tv;

import com.dm.state.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class PowerOnState implements IState
{
    
    @Override
    public void nextChannel()
    {
        LogFileUtil.v(MainApplication.TAG, "nextChannel");
    }
    
    @Override
    public void prevChannel()
    {
        LogFileUtil.v(MainApplication.TAG, "prevChannel");
    }
    
    @Override
    public void turnUp()
    {
        LogFileUtil.v(MainApplication.TAG, "turnUp");
    }
    
    @Override
    public void turnDowm()
    {
        LogFileUtil.v(MainApplication.TAG, "turnDowm");
    }

}
