package com.dm.adapter.composite;

public class CVoltAdapter implements CVolt5
{
    private CVolt220 mVolt220;
    
    public CVoltAdapter(CVolt220 adaptee)
    {
        this.mVolt220 = adaptee;
    }
    
    @Override
    public int getVolt5()
    {
        return (int)(getVolt220() * 1.0 / 220 * 5);
    }
    
    public int getVolt220()
    {
        return mVolt220.getVolt220();
    }
    
}
