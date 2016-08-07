package com.dm.bridget.implConcrete;

import com.dm.bridget.activity.MainApplication;
import com.dm.bridget.addBridge.CoffeeAdditives;
import com.yline.log.LogFileUtil;

public class LargeCoffee extends Coffee
{
    
    public LargeCoffee(CoffeeAdditives impl)
    {
        super(impl);
    }
    
    @Override
    public void makeCoffee()
    {
        LogFileUtil.v(MainApplication.TAG, "LargeCoffee -> 大杯的" + impl.getClass().getSimpleName() + "咖啡");
    }
    
}
