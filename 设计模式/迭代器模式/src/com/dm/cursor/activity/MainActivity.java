package com.dm.cursor.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.cursor.R;
import com.dm.cursor.aggregate.HuiCompany;
import com.dm.cursor.aggregate.ICompany;
import com.dm.cursor.aggregate.MinCompany;
import com.dm.cursor.iterator.IIterator;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_min).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                ICompany min = new MinCompany();
                check(min.iIterator());
            }
        });
        
        findViewById(R.id.btn_hui).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                ICompany hui = new HuiCompany();
                check(hui.iIterator());
            }
        });
    }
    
    private void check(IIterator iterator)
    {
        while (iterator.hasNext())
        {
            LogFileUtil.v(MainApplication.TAG, iterator.next().toString());
        }
    }
    
}
