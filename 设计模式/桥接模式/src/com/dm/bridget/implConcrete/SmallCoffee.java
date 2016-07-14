package com.dm.bridget.implConcrete;

import com.dm.bridget.activity.MainApplication;
import com.dm.bridget.addBridge.CoffeeAdditives;
import com.yline.log.LogFileUtil;

public class SmallCoffee extends Coffee
{
    
    public SmallCoffee(CoffeeAdditives impl)
    {
        super(impl);
    }
    
    @Override
    public void makeCoffee()
    {
        LogFileUtil.v(MainApplication.TAG, "SmallCoffee -> 小杯的" + impl.getClass().getSimpleName() + "咖啡");
    }
    
}
