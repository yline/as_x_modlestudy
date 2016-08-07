package com.dm.bridget.implConcrete;

import com.dm.bridget.addBridge.CoffeeAdditives;

public abstract class Coffee
{
    protected CoffeeAdditives impl;
    
    public Coffee(CoffeeAdditives impl)
    {
        this.impl = impl;
    }
    
    /**
     * 咖啡的具体成分,由子类决定
     */
    public abstract void makeCoffee();
}
