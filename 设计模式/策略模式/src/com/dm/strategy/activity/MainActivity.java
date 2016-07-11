package com.dm.strategy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dm.strategy.R;
import com.dm.strategy.impl.BusStrategy;
import com.yline.log.LogFileUtil;

public class MainActivity extends Activity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_caculate).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                TranfficCalculator calculator = new TranfficCalculator();
                calculator.setStrategy(new BusStrategy());
                int price = calculator.caculatePrice(16);
                
                LogFileUtil.v(MainApplication.TAG, "公交车16公里,价格 = " + price);
            }
        });
    }
    
}
