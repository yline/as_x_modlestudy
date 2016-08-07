package com.dm.state.power;

import com.dm.state.activity.MainApplication;
import com.dm.state.tv.PowerOffState;
import com.dm.state.tv.PowerOnState;
import com.dm.state.tv.IState;
import com.yline.log.LogFileUtil;

public class TvController implements IController
{
    private IState mTvState;
    
    @Override
    public void powerOn()
    {
        this.mTvState = new PowerOnState();
        LogFileUtil.v(MainApplication.TAG, "开机状态");
    }
    
    @Override
    public void powerOff()
    {
        this.mTvState = new PowerOffState();
        LogFileUtil.v(MainApplication.TAG, "关机状态");
    }
    
    /**
     * @return tvState, default is PowerOffState
     */
    public IState getTvStateInstance()
    {
        if (null == this.mTvState)
        {
            return new PowerOffState();
        }
        else
        {
            return mTvState;
        }
    }
    
    public void nextChannel()
    {
        mTvState.nextChannel();
    }
    
    public void prevChannal()
    {
        mTvState.prevChannel();
    }
    
    public void turnUp()
    {
        mTvState.turnUp();
    }
    
    public void turnDowm()
    {
        mTvState.turnDowm();
    }
}











