package com.dm.observer.uml.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.observer.uml.R;
import com.dm.observer.uml.observable.DevTechFrontier;
import com.dm.observer.uml.observer.Coder;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_observer).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                DevTechFrontier devTechFrontier = new DevTechFrontier();
                
                Coder mrSimple = new Coder("mr,simple");
                Coder coder1 = new Coder("coder1");
                Coder coder2 = new Coder("coder2");
                Coder coder3 = new Coder("coder3");
                
                // 注册
                devTechFrontier.addObserver(mrSimple);
                devTechFrontier.addObserver(coder1);
                devTechFrontier.addObserver(coder2);
                devTechFrontier.addObserver(coder3);
                
                // 发布消息
                devTechFrontier.postNewPublication("新一期的技术前线周报 发布了");
            }
        });
    }
    
}
