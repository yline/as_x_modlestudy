package com.lock.object;

import com.lock.object.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 调用代码:
 * new SynchronizedLockSimple().testSimple(3000,"1");
 * new SynchronizedLockSimple().testSimple(500,"2");
 * 
 * 上述代码执行步骤分析:
 * 前部代码块代号:"A"; 内部代码块代号"B",后部代码块"C"
 * 
 * 日志:
 * 并发状态
 * Simple1 -> synchronized out before           1A
 * Simple2 -> synchronized out before           2A
 * 串行状态// 这里会阻塞作用开启
 * Simple1 -> synchronized in                   1B
 * Simple1 -> synchronized in thread before     1B
 * Simple1 -> synchronized in thread after      1B
 * 并发状态
 * Simple2 -> synchronized in                   2B
 * Simple1 -> synchronized out after            1C
 * Simple2 -> synchronized in thread before     2B
 * Simple1 -> synchronized out thread before    1C
 * Simple2 -> synchronized in thread after      2B
 * Simple2 -> synchronized out after            2C
 * Simple2 -> synchronized out thread before    2C
 * Simple2 -> synchronized out thread after     2C
 * Simple1 -> synchronized out thread after     1C
 * 
 * 总结:
 * 1,synchronized会阻塞 内部代码块以及后部代码块
 * 2,synchronized能够保证同一时刻最多只有一个线程执行该段代码
 * 3,并不一定先创建线程的代码就一定是先拿到锁...具有一定的随机性
 * 
 * @author YLine
 * 2016年6月26日 下午9:10:29
 */
public class SynchronizedLockSimple
{
    private static final String TAG = "SynchronizedLockSimple"; // tag
    
    public SynchronizedLockSimple()
    {
    }
    
    public void testSimple(final int time, final String addTag)
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
}
