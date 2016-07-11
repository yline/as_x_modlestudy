package com.dm.gongchang.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.gongchang.R;
import com.dm.gongchang.product.ConcreteProductA;
import com.dm.gongchang.product.ConcreteProductB;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_factory_normal).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                com.dm.gongchang.factory.Factory factoryA = new com.dm.gongchang.factory.ConcreteFactoryA();
                com.dm.gongchang.product.Product productA = factoryA.createProduct();
                productA.method();
                
                com.dm.gongchang.factory.Factory factoryB = new com.dm.gongchang.factory.ConcreteFactoryA();
                com.dm.gongchang.product.Product productB = factoryB.createProduct();
                productB.method();
            }
        });
        
        findViewById(R.id.btn_factory_reflact).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                com.dm.gongchang.factory.reflact.Factory factoryA =
                    new com.dm.gongchang.factory.reflact.ConcreteFactory();
                com.dm.gongchang.product.Product productA = factoryA.createProduct(ConcreteProductA.class);
                productA.method();
                
                com.dm.gongchang.factory.reflact.Factory factoryB =
                    new com.dm.gongchang.factory.reflact.ConcreteFactory();
                com.dm.gongchang.product.Product productB = factoryB.createProduct(ConcreteProductB.class);
                productB.method();
            }
        });
    }
}
