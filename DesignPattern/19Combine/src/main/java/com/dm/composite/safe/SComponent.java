package com.dm.composite.safe;

public abstract class SComponent
{
    protected String name; // 节名点
    
    public SComponent(String name)
    {
        this.name = name;
    }
    
    /**
     * 具体的逻辑方法由子类实现
     */
    public abstract void doSomething();
}
