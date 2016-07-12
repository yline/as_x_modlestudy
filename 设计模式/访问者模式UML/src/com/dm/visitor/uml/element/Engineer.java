package com.dm.visitor.uml.element;

import java.util.Random;

import com.dm.visitor.uml.visitor.Visitor;


public class Engineer extends Staff
{
    
    public Engineer(String name)
    {
        super(name);
    }
    
    @Override
    public void accept(Visitor visitor)
    {
        visitor.visit(this);
    }
    
    /** 一年内做的代码数量 */
    public int getCodeLines()
    {
        return new Random().nextInt(10 * 10000);
    }
    
}
