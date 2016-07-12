package com.dm.template;

import android.os.Bundle;
import android.view.View;

import com.dm.template.abstracts.AbstractComputer;
import com.dm.template.concrete.CoderComputer;
import com.dm.template.concrete.MilitaryComputer;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_coder).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                AbstractComputer coderComputer = new CoderComputer();
                coderComputer.startUp();
            }
        });
        
        findViewById(R.id.btn_military).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                AbstractComputer militaryComputer = new MilitaryComputer();
                militaryComputer.startUp();
            }
        });
    }
    
}
