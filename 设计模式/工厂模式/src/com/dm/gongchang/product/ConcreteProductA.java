package com.dm.gongchang.product;

import com.dm.gongchang.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class ConcreteProductA extends Product
{
    
    @Override
    public void method()
    {
        LogFileUtil.v(MainApplication.TAG, "ConcreteProduct A");
    }
    
}
