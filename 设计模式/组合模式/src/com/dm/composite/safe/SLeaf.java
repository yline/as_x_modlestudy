package com.dm.composite.safe;

import com.dm.composite.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 这个是具体的节点
 * @author f21
 * @date 2016-3-4
 */
public class SLeaf extends SComponent
{
    
    public SLeaf(String name)
    {
        super(name);
    }
    
    @Override
    public void doSomething()
    {
        LogFileUtil.v(MainApplication.TAG, "SLeaf -> dosomething name = " + name);
    }
    
}
