package com.dm.abstracts.factory.product.tire;

import com.dm.abstracts.factory.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class SUVTire implements ITire
{
    
    @Override
    public void tire()
    {
        LogFileUtil.v(MainApplication.TAG, "越野轮胎");
    }
    
}
