package com.dm.composite.safe;

import java.util.ArrayList;
import java.util.List;

import com.dm.composite.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 这个相当于一个容器,存储每一个节点的内容
 * @author f21
 * @date 2016-3-4
 */
public class SComposite extends SComponent
{
    
    public SComposite(String name)
    {
        super(name);
    }
    
    private List<SComponent> sComponents = new ArrayList<SComponent>();
    
    @Override
    public void doSomething()
    {
        LogFileUtil.v(MainApplication.TAG, "SComposite -> dosomething name = " + name);
        if (null != sComponents)
        {
            for (SComponent sComponent : sComponents)
            {
                sComponent.doSomething();
            }
        }
    }
    
    /**
     * 添加 子节点
     * @param component
     */
    public void addChild(SComponent child)
    {
        sComponents.add(child);
    }
    
    /**
     * 移除 子节点
     * @param child
     */
    public void removeChild(SComponent child)
    {
        sComponents.remove(child);
    }
    
    /**
     * 获取 某一个子节点
     * @param index 子节点的序号(0 开始)
     */
    public SComponent getChild(int index)
    {
        return sComponents.get(index);
    }
}
