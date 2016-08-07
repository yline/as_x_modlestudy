package com.dm.decorator.component;

import com.dm.decorator.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class Boy extends Person
{
    
    @Override
    public void dressed()
    {
        LogFileUtil.v(MainApplication.TAG, "Boy -> dressed -> 内衣内裤");
    }
    
}
