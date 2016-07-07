package com.lock.object.tag.synchronize;

import com.lock.object.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.content.Intent;

/**
 * 多线程同步,初始化,并留给其他程序一个判断函数
 * 实现效果:
 * 
 * 先执行完Service中LoadSoRunnable -> InitSDKRunnable -> workAfterInit
 * 然后执行主函数:startWork【这个和workAfterInit,可能关系互换】
 * @author YLine
 *
 * 2016年7月7日 上午1:29:31
 */
public class InitConfig
{
    public InitConfig(Context context)
    {
        startWork(500);
        context.startService(new Intent(context, InitService.class));
    }
    
    private void startWork(final int time)
    {
        LogFileUtil.v(MainApplication.TAG, "startWork -> start");
        
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                LogFileUtil.v(MainApplication.TAG, "startWork -> start");
                synchronized (MainApplication.initLock)
                {
                    LogFileUtil.v(MainApplication.TAG, "startWork -> lock in");
                    // 循环等待
                    while (!InitService.is_so_load() && !InitService.is_sdk_init())
                    {
                        try
                        {
                            LogFileUtil.v(MainApplication.TAG, "startWork -> lock in -> before wait");
                            MainApplication.initLock.wait();
                            LogFileUtil.v(MainApplication.TAG, "startWork -> lock in -> after wait");
                        }
                        catch (InterruptedException e)
                        {
                            LogFileUtil.v(MainApplication.TAG, "startWork -> lock in -> InterruptedException", e);
                        }
                    }
                    
                    try
                    {
                        LogFileUtil.v(MainApplication.TAG, "startWork -> lock in -> before sleep");
                        Thread.sleep(time);
                        LogFileUtil.v(MainApplication.TAG, "startWork -> lock in ->after sleep");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.v(MainApplication.TAG, "startWork -> sleep InterruptedException");
                    }
                    
                    LogFileUtil.v(MainApplication.TAG, "startWork -> lock in -> before notifyAll");
                    MainApplication.initLock.notifyAll();
                    LogFileUtil.v(MainApplication.TAG, "startWork -> lock in -> after notifyAll");
                }
            }
        }).start();
        
        LogFileUtil.v(MainApplication.TAG, "startWork -> end");
    }
}
