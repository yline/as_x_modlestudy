package com.dm.singleton.statics;

import com.dm.singleton.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 推荐使用的单例模式
 */
public class SingletonStatic
{
    private SingletonStatic()
    {
    }
    
    public static SingletonStatic getInstance()
    {
        return SingletonHolder.sInstance;
    }
    
    /**
     * 静态内部类
     */
    private static class SingletonHolder
    {
        private static final SingletonStatic sInstance = new SingletonStatic();
    }
    
    public void doSome()
    {
        LogFileUtil.i(MainApplication.TAG, "SingletonStatic -> doSome");
    }
}
