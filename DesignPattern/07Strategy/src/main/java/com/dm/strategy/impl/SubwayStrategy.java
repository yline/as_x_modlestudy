package com.dm.strategy.impl;

import com.dm.strategy.Strategy;

public class SubwayStrategy implements Strategy
{
    
    /**
     * 6公里(含)内3元
     *  6 ~ 12(含) = 4元
     * 12 ~ 22(含) = 5元
     * 22 ~ 32(含) = 6元
     * 
     */
    @Override
    public int calculatePrice(int km)
    {
        if (km <= 6)
        {
            return 3;
        }
        else if (km > 6 && km <= 12)
        {
            return 4;
        }
        else if (km > 12 && km <= 22)
        {
            return 5;
        }
        else if (km > 22 && km <= 32)
        {
            return 6;
        }
        else
        {
            return 7; // 其它距离(不可能发生)
        }
    }
    
}
