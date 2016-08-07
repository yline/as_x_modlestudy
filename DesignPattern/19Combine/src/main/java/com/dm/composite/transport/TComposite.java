package com.dm.composite.transport;

import java.util.ArrayList;
import java.util.List;

import com.dm.composite.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 这个相当于一个容器,存储每一个节点的内容
 * @author f21
 * @date 2016-3-4
 */
public class TComposite extends TComponent
{
    
    public TComposite(String name)
    {
        super(name);
    }
    
    private List<TComponent> tComponents = new ArrayList<TComponent>();
    
    @Override
    public void doSomething()
    {
        LogFileUtil.v(MainApplication.TAG, "TComposite -> dosomething name = " + name);
        if (null != tComponents)
        {
            for (TComponent tComponent : tComponents)
            {
                tComponent.doSomething();
            }
        }
    }
    
    /**
     * 添加 子节点
     * @param component
     */
    @Override
    public void addChild(TComponent child)
    {
        tComponents.add(child);
    }
    
    /**
     * 移除 子节点
     * @param child
     */
    @Override
    public void removeChild(TComponent child)
    {
        tComponents.remove(child);
    }
    
    /**
     * 获取 某一个子节点
     * @param index 子节点的序号(0 开始)
     */
    @Override
    public TComponent getChild(int index)
    {
        return tComponents.get(index);
    }
}
