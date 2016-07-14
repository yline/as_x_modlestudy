package com.dm.adapter.extend;

public class EVoltAdapter extends EVolt220 implements EVolt5
{
    
    @Override
    public int getVolt5()
    {
        return getVolt220() / 220 * 5;
    }
    
}
