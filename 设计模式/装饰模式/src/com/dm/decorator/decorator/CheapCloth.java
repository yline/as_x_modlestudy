package com.dm.decorator.decorator;

import com.dm.decorator.activity.MainApplication;
import com.dm.decorator.component.Person;
import com.yline.log.LogFileUtil;

/**
 * PersonCloth 的子类
 */
public class CheapCloth extends PersonCloth
{
    
    public CheapCloth(Person person)
    {
        super(person);
    }
    
    @Override
    public void dressed()
    {
        super.dressed();
        dressShort();
    }
    
    private void dressShort()
    {
        LogFileUtil.v(MainApplication.TAG, "CheapCloth -> dressed");
    }
}
