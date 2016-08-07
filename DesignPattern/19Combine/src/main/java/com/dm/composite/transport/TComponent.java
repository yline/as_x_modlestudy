package com.dm.composite.transport;

public abstract class TComponent
{
    protected String name; // 节名点
    
    public TComponent(String name)
    {
        this.name = name;
    }
    
    /**
     * 具体的逻辑方法由子类实现
     */
    public abstract void doSomething();
    
    /**
     * 添加 子节点
     * @param child
     */
    public abstract void addChild(TComponent child);
    
    /**
     * 移除 子节点
     * @param child
     */
    public abstract void removeChild(TComponent child);
    
    /**
     * 获取 字节点
     * @param index 序号(0 开头)
     */
    public abstract TComponent getChild(int index);
}
