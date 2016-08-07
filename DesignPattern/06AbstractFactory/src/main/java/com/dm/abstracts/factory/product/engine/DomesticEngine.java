package com.dm.abstracts.factory.product.engine;

import com.dm.abstracts.factory.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class DomesticEngine implements IEngine
{
    
    @Override
    public void engine()
    {
        LogFileUtil.v(MainApplication.TAG, "国产发动机");
    }
    
}
