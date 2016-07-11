package com.dm.gongchang.factory;

import com.dm.gongchang.product.ConcreteProductA;
import com.dm.gongchang.product.Product;

public class ConcreteFactoryA extends Factory
{
    
    @Override
    public Product createProduct()
    {
        return new ConcreteProductA();
    }
    
}
