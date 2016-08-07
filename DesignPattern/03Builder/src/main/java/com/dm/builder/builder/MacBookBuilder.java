package com.dm.builder.builder;

import com.dm.builder.product.Computer;
import com.dm.builder.product.MacBook;

/**
 * 负责构建Computer
 */
public class MacBookBuilder extends Builder
{
    private Computer mComputer;
    
    public MacBookBuilder()
    {
        mComputer = new MacBook();
    }
    
    @Override
    public void buildBoard(String board)
    {
        mComputer.setBoard(board);
    }
    
    @Override
    public void buildDisplay(String display)
    {
        mComputer.setDisplay(display);
    }
    
    @Override
    public void buildOS()
    {
        mComputer.setOS();
    }
    
    @Override
    public Computer create()
    {
        return mComputer;
    }
}
