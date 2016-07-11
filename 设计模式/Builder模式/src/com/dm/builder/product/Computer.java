package com.dm.builder.product;

// 计算机抽象类,即 Product角色
public abstract class Computer
{
    protected String mBoard;
    
    protected String mDisplay;
    
    protected String mOS;
    
    protected Computer()
    {
    }
    
    // 设置CPU核心数
    public void setBoard(String board)
    {
        this.mBoard = board;
    }
    
    // 设置内存
    public void setDisplay(String display)
    {
        this.mDisplay = display;
    }
    
    // 设置操作系统
    public abstract void setOS();
    
    @Override
    public String toString()
    {
        return "Computer [mBoard=" + mBoard + ", mDisplay=" + mDisplay + ", mOS=" + mOS + "]";
    }
}
