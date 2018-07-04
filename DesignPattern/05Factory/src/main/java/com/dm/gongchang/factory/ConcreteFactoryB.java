package com.dm.gongchang.factory;

import com.dm.gongchang.product.ConcreteProductB;
import com.dm.gongchang.product.Product;

public class ConcreteFactoryB extends Factory {
    @Override
    public Product createProduct() {
        return new ConcreteProductB();
    }

}
