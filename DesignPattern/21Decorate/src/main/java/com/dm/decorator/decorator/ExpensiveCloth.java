package com.dm.decorator.decorator;

import com.dm.decorator.activity.MainApplication;
import com.dm.decorator.component.Person;
import com.yline.log.LogFileUtil;

/**
 * PersonCloth 的子类
 */
public class ExpensiveCloth extends PersonCloth
{
    
    public ExpensiveCloth(Person person)
    {
        super(person);
    }
    
    @Override
    public void dressed()
    {
        super.dressed();
        
        dressShirt();
        dressLeather();
        dressJean();
    }
    
    private void dressShirt()
    {
        LogFileUtil.v(MainApplication.TAG, "ExpensiveCloth -> dressShirt");
    }
    
    private void dressLeather()
    {
        LogFileUtil.v(MainApplication.TAG, "ExpensiveCloth -> dressLeather");
    }
    
    private void dressJean()
    {
        LogFileUtil.v(MainApplication.TAG, "ExpensiveCloth -> dressJean");
    }
}
