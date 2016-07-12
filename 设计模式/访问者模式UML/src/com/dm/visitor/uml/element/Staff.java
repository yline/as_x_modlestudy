package com.dm.visitor.uml.element;

import java.util.Random;

import com.dm.visitor.uml.visitor.Visitor;


public abstract class Staff
{
    /**
     * 姓名
     */
    public String name;
    
    /**
     * 绩效管理	Key Performance Indicators
     */
    public int kpi;
    
    public Staff(String name)
    {
        this.name = name;
        kpi = new Random().nextInt(10);
    }
    
    public abstract void accept(Visitor visitor);
}
