package com.dm.strategy.impl;

import com.dm.strategy.Strategy;

public class TaxiStrategy implements Strategy
{
    
    @Override
    public int calculatePrice(int km)
    {
        return km * 2;
    }
    
}
