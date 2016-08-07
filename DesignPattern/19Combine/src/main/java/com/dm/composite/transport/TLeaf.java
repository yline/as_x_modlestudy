package com.dm.composite.transport;

import com.dm.composite.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 这个是具体的节点
 * @author f21
 * @date 2016-3-4
 */
public class TLeaf extends TComponent
{
    
    public TLeaf(String name)
    {
        super(name);
    }
    
    @Override
    public void doSomething()
    {
        LogFileUtil.v(MainApplication.TAG, "TLeaf -> dosomething name = " + name);
    }
    
    @Override
    public void addChild(TComponent child)
    {
        throw new UnsupportedOperationException("叶子节点没有子节点");
    }
    
    @Override
    public void removeChild(TComponent child)
    {
        throw new UnsupportedOperationException("叶子节点没有子节点");
    }
    
    @Override
    public TComponent getChild(int index)
    {
        throw new UnsupportedOperationException("叶子节点没有子节点");
    }
    
}
