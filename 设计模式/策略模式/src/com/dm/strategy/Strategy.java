package com.dm.strategy;

public interface Strategy
{
    /**
     * 按距离来计算价格
     * 
     * @param km	公里
     * @return	价格
     */
    int calculatePrice(int km);
}
