package com.lock.object;

import com.lock.object.activity.MainApplication;
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
 * 调用语句:
 * new SynchronizedLock().testLockNotifyAll(9000, "3");
 * new SynchronizedLock().testLockWait(2000, "2");
 * new SynchronizedLock().testLockSingle(1000, "1");
 * new SynchronizedLock().testLockWaitAndNotifyAll(1000, "4");
 * 
 * 结果,"4"最后的代码不会被执行;
 * 原因:它在等待着,并且没有被notify
 * 
 * 总结:
 * 1,wait和notify不是万能的,它要求在Synchronized内部调用才能保证不出错;即:它要求在具有控制权的线程中执行对象的wait或notify方法.
 * 否则报错:java.lang.IllegalMonitorStateException:object not locked by thread before notify()
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
                LogFileUtil.v(tag, "out synchronized before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in & before sleep");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in & after sleep");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in & InterruptedException");
                    }
                }
                
                LogFileUtil.v(tag, "out synchronized");
                try
                {
                    LogFileUtil.v(tag, "out synchronized & before sleep");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "out synchronized & after sleep");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "out synchronized & InterruptedException");
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
                LogFileUtil.v(tag, "out synchronized before");
                
                synchronized (MainApplication.lock)
                {
                    
                    LogFileUtil.v(tag, "synchronized in");
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in & before wait");
                        MainApplication.lock.wait();
                        LogFileUtil.v(tag, "synchronized in & after wait");
                    }
                    catch (InterruptedException e1)
                    {
                        LogFileUtil.e(tag, "synchronized in wait & InterruptedException");
                    }
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in & before sleep");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in & after sleep");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in & InterruptedException");
                    }
                }
                
                LogFileUtil.v(tag, "out synchronized");
                try
                {
                    LogFileUtil.v(tag, "out synchronized & before sleep");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "out synchronized & after sleep");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "out synchronized & InterruptedException");
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
                LogFileUtil.v(tag, "out synchronized before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in & before sleep");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in & after sleep");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in & InterruptedException");
                    }
                    
                    LogFileUtil.v(tag, "synchronized in & before notify");
                    MainApplication.lock.notify();
                    LogFileUtil.v(tag, "synchronized in & after notify");
                }
                
                LogFileUtil.v(tag, "out synchronized after");
                try
                {
                    LogFileUtil.v(tag, "out synchronized & before sleep");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "out synchronized & after sleep");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "out synchronized & InterruptedException");
                }
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
                LogFileUtil.v(tag, "out synchronized before");
                
                synchronized (MainApplication.lock)
                {
                    LogFileUtil.v(tag, "synchronized in");
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in & before wait");
                        MainApplication.lock.wait();
                        LogFileUtil.v(tag, "synchronized in & after wait");
                    }
                    catch (InterruptedException e1)
                    {
                        LogFileUtil.e(tag, "synchronized in wait & InterruptedException");
                    }
                    
                    try
                    {
                        LogFileUtil.v(tag, "synchronized in & before sleep");
                        Thread.sleep(time);
                        LogFileUtil.v(tag, "synchronized in & after sleep");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.e(tag, "synchronized in & InterruptedException");
                    }
                    
                    LogFileUtil.v(tag, "synchronized in & before notify");
                    MainApplication.lock.notify();
                    LogFileUtil.v(tag, "synchronized in & after notify");
                }
                
                LogFileUtil.v(tag, "out synchronized after");
                try
                {
                    LogFileUtil.v(tag, "out synchronized & before sleep");
                    Thread.sleep(time);
                    LogFileUtil.v(tag, "out synchronized & after sleep");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.e(tag, "out synchronized & InterruptedException");
                }
                
            }
        }).start();
    }
}
