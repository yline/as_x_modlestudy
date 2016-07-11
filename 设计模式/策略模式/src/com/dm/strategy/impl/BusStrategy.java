package com.dm.strategy.impl;

import com.dm.strategy.Strategy;

public class BusStrategy implements Strategy
{
    
    /**
     * 北京公交车：10公里以内 1元, 超过10公里,每加1元,多乘5公里
     *  0 ~ 10 = 1
     * 10 ~ 15 = 2
     * 15 ~ 20 = 3
     */
    @Override
    public int calculatePrice(int km)
    {
        int result = km / 5;
        return result - 1;
    }
    
}
