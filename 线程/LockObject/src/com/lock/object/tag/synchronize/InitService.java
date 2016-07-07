package com.lock.object.tag.synchronize;

import com.lock.object.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class InitService extends Service
{
    private static final String TAG = "InitService";
    
    /** 标签,是否加载完成了.so文件 */
    private static boolean IS_SO_LOADED = false;
    
    /** 标签,是否加载完成了SDK */
    private static boolean IS_SDK_INIT = false;
    
    public static boolean is_so_load()
    {
        return IS_SO_LOADED;
    }
    
    public static boolean is_sdk_init()
    {
        return IS_SDK_INIT;
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        LogFileUtil.v(TAG, "onCreate -> before Load.So");
        new Thread(new LoadSoRunnable(2000)).start();
        LogFileUtil.v(TAG, "onCreate -> after Load.So");
        
        LogFileUtil.v(TAG, "onCreate -> before InitSDK");
        new Thread(new InitSDKRunnable(1000)).start();
        LogFileUtil.v(TAG, "onCreate -> after InitSDK");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        workAfterInit(500);
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    private class LoadSoRunnable implements Runnable
    {
        private int time;
        
        public LoadSoRunnable(int time)
        {
            this.time = time;
        }
        
        @Override
        public void run()
        {
            LogFileUtil.v(TAG, "LoadSoRunnable -> run start");
            synchronized (MainApplication.initLock)
            {
                LogFileUtil.v(TAG, "LoadSoRunnable -> lock in");
                try
                {
                    LogFileUtil.v(TAG, "LoadSoRunnable -> lock in -> before sleep");
                    Thread.sleep(time);
                    LogFileUtil.v(TAG, "LoadSoRunnable -> lock in -> after sleep");
                    
                    IS_SO_LOADED = true;
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.v(TAG, "LoadSoRunnable -> lock in -> InterruptedException", e);
                }
                finally
                {
                    LogFileUtil.v(TAG, "LoadSoRunnable -> lock in -> before notifyAll,IS_SO_LOADED = " + IS_SO_LOADED);
                    MainApplication.initLock.notifyAll();
                    LogFileUtil.v(TAG, "LoadSoRunnable -> lock in -> after notifyAll");
                }
            }
            LogFileUtil.v(TAG, "LoadSoRunnable -> run end");
        }
    }
    
    private class InitSDKRunnable implements Runnable
    {
        private int time;
        
        public InitSDKRunnable(int time)
        {
            this.time = time;
        }
        
        @Override
        public void run()
        {
            LogFileUtil.v(TAG, "InitSDKRunnable -> run start");
            synchronized (MainApplication.initLock)
            {
                LogFileUtil.v(TAG, "InitSDKRunnable -> lock in");
                // 循环等待
                while (!IS_SO_LOADED)
                {
                    try
                    {
                        LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> before wait");
                        MainApplication.initLock.wait();
                        LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> after wait");
                    }
                    catch (InterruptedException e)
                    {
                        LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> InterruptedException", e);
                    }
                }
                
                LogFileUtil.v(TAG, "InitSDKRunnable -> lock in");
                try
                {
                    LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> before sleep");
                    Thread.sleep(time);
                    LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> after sleep");
                    
                    IS_SDK_INIT = true;
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> InterruptedException", e);
                }
                
                LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> before notifyAll,IS_SDK_INIT = " + IS_SDK_INIT);
                MainApplication.initLock.notifyAll();
                LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> after notifyAll");
            }
            LogFileUtil.v(TAG, "InitSDKRunnable -> run end");
        }
    }
    
    private void workAfterInit(int time)
    {
        LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> start");
        synchronized (MainApplication.initLock)
        {
            LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in");
            // 循环等待
            while (!IS_SO_LOADED && !IS_SDK_INIT)
            {
                try
                {
                    LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in -> before wait");
                    MainApplication.initLock.wait();
                    LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in -> after wait");
                }
                catch (InterruptedException e)
                {
                    LogFileUtil.v(TAG, "InitSDKRunnable -> lock in -> InterruptedException", e);
                }
            }
            
            try
            {
                LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in -> before sleep");
                Thread.sleep(time);
                LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in ->after sleep");
            }
            catch (InterruptedException e)
            {
                LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> sleep InterruptedException");
            }
            
            LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in -> before notifyAll");
            MainApplication.initLock.notifyAll();
            LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> lock in -> after notifyAll");
        }
        LogFileUtil.v(TAG, "onStartCommand -> workAfterInit -> end");
    }
}
