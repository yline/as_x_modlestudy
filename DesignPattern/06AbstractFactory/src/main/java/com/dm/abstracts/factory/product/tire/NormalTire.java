package com.dm.abstracts.factory.product.tire;

import com.dm.abstracts.factory.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class NormalTire implements ITire
{
    
    @Override
    public void tire()
    {
        LogFileUtil.v(MainApplication.TAG, "普通轮胎");
    }
    
}
