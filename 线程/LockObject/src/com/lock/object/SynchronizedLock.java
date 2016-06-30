package com.lock.object;

import com.lock.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * wait(): 导致当前线程等待,直到其它线程调用该同步监视器的notify()方法或者notifyAll()方法来唤醒该线程.
 * 该方法可带  倒计时功能
 * 
 * notify(): 唤醒在此同步监视器上等待的单个线程。如果所有线程都在此同步监视器上等待,则会选择唤醒其中一个线程,选择是任意性的.
 * 只有当前线程放弃对该同步监视器的锁定后(使用wait()方法),才可以执行会唤醒的线程.
 * 
 * notifyAll(): 唤醒在此同步监视器上等待的所有线程. 只有当前线程放弃对该同步监视器的锁定后才可以执行被唤醒的线程.
 * 
 * 上述代码执行步骤分析:
 * 前部代码块代号:"A"; 内部代码块代号"B",后部代码块"C"
 * 
 * @author YLine
 * 2016年6月26日 下午11:04:13
 */
public class SynchronizedLock
{
    private static final String TAG = "SynchronizedLock";
    
    public SynchronizedLock()
    {
    }
    
    public void testLockSingle(final int time, final String addTag)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                String tag = TAG + addTag;
                LogFileUtil.v(tag, "synchronized out before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in thread before");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in thread after");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in InterruptedException");
                    }
                }
                
                LogFileUtil.v(tag, "synchronized out after");
                try
                {
                    LogFileUtil.v(tag, "synchronized out thread before");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "synchronized out thread after");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "synchronized out InterruptedException");
                }
            }
        }).start();
    }
    
    public void testLockWait(final int time, final String addTag)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                String tag = TAG + addTag;
                LogFileUtil.v(tag, "synchronized out before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in wait before");
                        MainApplication.lock.wait();
                        LogFileUtil.v(tag, "synchronized in wait after");
                    }
                    catch (InterruptedException e1)
                    {
                        LogFileUtil.e(tag, "synchronized in wait InterruptedException");
                    }
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in thread before");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in thread after");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in InterruptedException");
                    }
                }
                
                LogFileUtil.v(tag, "synchronized out after");
                try
                {
                    LogFileUtil.v(tag, "synchronized out thread before");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "synchronized out thread after");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "synchronized out InterruptedException");
                }
            }
        }).start();
    }
    
    public void testLockNotifyAll(final int time, final String addTag)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                String tag = TAG + addTag;
                LogFileUtil.v(tag, "synchronized out before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in thread before");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in thread after");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in InterruptedException");
                    }
                }
                
                LogFileUtil.v(tag, "synchronized out after");
                try
                {
                    LogFileUtil.v(tag, "synchronized out thread before");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "synchronized out thread after");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "synchronized out InterruptedException");
                }
                
                LogFileUtil.v(tag, "synchronized out notify before");
                MainApplication.lock.notify();
                LogFileUtil.v(tag, "synchronized out notify after");
            }
        }).start();
    }
    
    public void testLockWaitAndNotifyAll(final int time, final String addTag)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                String tag = TAG + addTag;
                LogFileUtil.v(tag, "synchronized out before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in wait before");
                        MainApplication.lock.wait();
                        LogFileUtil.v(tag, "synchronized in wait after");
                    }
                    catch (InterruptedException e1)
                    {
                        LogFileUtil.e(tag, "synchronized in wait InterruptedException");
                    }
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in thread before");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in thread after");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in InterruptedException");
                    }
                }
                
                LogFileUtil.v(tag, "synchronized out after");
                try
                {
                    LogFileUtil.v(tag, "synchronized out thread before");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "synchronized out thread after");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "synchronized out InterruptedException");
                }
                
                LogFileUtil.v(tag, "synchronized out notify before");
                MainApplication.lock.notify();
                LogFileUtil.v(tag, "synchronized out notify after");
            }
        }).start();
    }
}
