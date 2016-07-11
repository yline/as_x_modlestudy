package com.dm.iterator;

import android.os.Bundle;
import android.view.View;

import com.dm.iterator.leader.Boss;
import com.dm.iterator.leader.Director;
import com.dm.iterator.leader.GroupLeader;
import com.dm.iterator.leader.Manager;
import com.dm.iterator.pattern.R;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_iterator).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                GroupLeader groupLeader = new GroupLeader();
                Director director = new Director();
                Manager manager = new Manager();
                Boss boss = new Boss();
                
                // 设置上一级领导处理者对象
                groupLeader.setNextHandler(director);
                director.setNextHandler(manager);
                manager.setNextHandler(boss);
                
                groupLeader.handleRequest(1100);
            }
        });
    }
    
}
