package com.lock.object.activity;

import com.lock.object.R;
import com.lock.object.SynchronizedLock;
import com.lock.object.SynchronizedLockSimple;
import com.lock.object.tag.synchronize.InitConfig;
import com.yline.log.LogFileUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick btn_one");
                new SynchronizedLockSimple().testSimple(3000, "1");
                new SynchronizedLockSimple().testSimple(500, "2");
            }
        });
        
        findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick btn_two");
                
                new SynchronizedLock().testLockNotifyAll(9000, "3");
                new SynchronizedLock().testLockWait(2000, "2");
                new SynchronizedLock().testLockSingle(1000, "1");
                new SynchronizedLock().testLockWaitAndNotifyAll(1000, "4");
            }
        });
        
        findViewById(R.id.btn_three).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick btn_three");
                new InitConfig(MainActivity.this);
            }
        });
    }
    
}
