package com.dm.abstracts.factory.product.engine;

import com.dm.abstracts.factory.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class ImportEngine implements IEngine
{
    
    @Override
    public void engine()
    {
        LogFileUtil.v(MainApplication.TAG, "进口发动机");
    }
    
}
