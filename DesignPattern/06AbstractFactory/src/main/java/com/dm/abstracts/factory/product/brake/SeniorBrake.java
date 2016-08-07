package com.dm.abstracts.factory.product.brake;

import com.dm.abstracts.factory.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class SeniorBrake implements IBrake
{
    
    @Override
    public void brake()
    {
        LogFileUtil.v(MainApplication.TAG, "高级制动");
    }
    
}
