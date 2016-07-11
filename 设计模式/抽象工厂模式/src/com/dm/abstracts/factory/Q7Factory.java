package com.dm.abstracts.factory;

import com.dm.abstracts.factory.product.brake.IBrake;
import com.dm.abstracts.factory.product.brake.SeniorBrake;
import com.dm.abstracts.factory.product.engine.IEngine;
import com.dm.abstracts.factory.product.engine.ImportEngine;
import com.dm.abstracts.factory.product.tire.ITire;
import com.dm.abstracts.factory.product.tire.SUVTire;

public class Q7Factory extends CarFactory
{
    
    @Override
    public ITire createTire()
    {
        return new SUVTire();
    }
    
    @Override
    public IEngine createEngine()
    {
        return new ImportEngine();
    }
    
    @Override
    public IBrake createBrake()
    {
        return new SeniorBrake();
    }
    
}
