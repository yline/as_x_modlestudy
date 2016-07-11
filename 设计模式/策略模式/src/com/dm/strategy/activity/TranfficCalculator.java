package com.dm.strategy.activity;

import com.dm.strategy.Strategy;

public class TranfficCalculator
{
    private Strategy mCaculateStrategy;
    
    public int caculatePrice(int km)
    {
        return this.mCaculateStrategy.calculatePrice(km);
    }
    
    public void setStrategy(Strategy strategy)
    {
        this.mCaculateStrategy = strategy;
    }
}
