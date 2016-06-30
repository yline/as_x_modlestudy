package com.lock.activity;

import com.lock.object.R;
import com.lock.object.SynchronizedLock;
import com.yline.log.LogFileUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    
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
                LogFileUtil.v("MainActivity", "onClick");
                new SynchronizedLock().testLockSingle(1000, "Single");
                new SynchronizedLock().testLockWait(1000, "Wait");
                synchronized (MainApplication.lock)
                {
                    try
                    {
                        LogFileUtil.v(TAG, "synchronized in wait before");
                        MainApplication.lock.wait();
                        LogFileUtil.v(TAG, "synchronized in wait before");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.v(TAG, "synchronized in wait InterruptedException");
                    }
                    
                    new SynchronizedLock().testLockNotifyAll(1000, "NotifyAll");
                    new SynchronizedLock().testLockWaitAndNotifyAll(1000, "Wait&NotifyAll");
                }
                LogFileUtil.v(TAG, "run");
                LogFileUtil.v(TAG, "run");
                LogFileUtil.v(TAG, "run");
                LogFileUtil.v(TAG, "run");
                LogFileUtil.v(TAG, "run");
            }
        });
    }
    
}
