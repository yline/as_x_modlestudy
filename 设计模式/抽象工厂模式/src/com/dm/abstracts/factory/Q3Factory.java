package com.dm.abstracts.factory;

import com.dm.abstracts.factory.product.brake.IBrake;
import com.dm.abstracts.factory.product.brake.NormalBrake;
import com.dm.abstracts.factory.product.engine.DomesticEngine;
import com.dm.abstracts.factory.product.engine.IEngine;
import com.dm.abstracts.factory.product.tire.ITire;
import com.dm.abstracts.factory.product.tire.NormalTire;

public class Q3Factory extends CarFactory
{
    
    @Override
    public ITire createTire()
    {
        return new NormalTire();
    }
    
    @Override
    public IEngine createEngine()
    {
        return new DomesticEngine();
    }
    
    @Override
    public IBrake createBrake()
    {
        return new NormalBrake();
    }
    
}
