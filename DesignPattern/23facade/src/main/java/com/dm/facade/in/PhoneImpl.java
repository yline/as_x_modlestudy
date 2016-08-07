package com.dm.facade.in;

import com.dm.facade.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class PhoneImpl implements Phone
{
    
    @Override
    public void dail()
    {
        LogFileUtil.v(MainApplication.TAG, "PhoneImpl -> dail");
    }
    
    @Override
    public void hangup()
    {
        LogFileUtil.v(MainApplication.TAG, "PhoneImpl -> hangup");
    }
}
