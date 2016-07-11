package com.dm.abstracts.factory;

import com.dm.abstracts.factory.product.brake.IBrake;
import com.dm.abstracts.factory.product.engine.IEngine;
import com.dm.abstracts.factory.product.tire.ITire;

public abstract class CarFactory
{
    /**
     * 生产轮胎
     */
    public abstract ITire createTire();
    
    /**
     * 生产发动机
     */
    public abstract IEngine createEngine();
    
    /**
     * 生产制动系统
     */
    public abstract IBrake createBrake();
}
