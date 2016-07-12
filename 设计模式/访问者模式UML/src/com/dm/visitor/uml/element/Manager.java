package com.dm.visitor.uml.element;

import java.util.Random;

import com.dm.visitor.uml.visitor.Visitor;


public class Manager extends Staff
{
    private int products;
    
    public Manager(String name)
    {
        super(name);
        this.products = new Random().nextInt(10);
    }
    
    @Override
    public void accept(Visitor visitor)
    {
        visitor.visit(this);
    }
    
    /** 一年内做的产品数量 */
    public int getProducts()
    {
        return products;
    }
}
