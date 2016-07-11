package com.dm.singleton.lazy;

import com.dm.singleton.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 优点:只有被使用时,才会被实例化
 * 缺点:getInstance,每次调用都会进行同步,造成不必要的开销
 */
public class SingletonLazy
{
    private static SingletonLazy instance;
    
    private SingletonLazy()
    {
    }
    
    public static synchronized SingletonLazy getInstance()
    {
        if (null == instance)
        {
            LogFileUtil.i(MainApplication.TAG, "SingletonLazy -> getInstance, new");
            instance = new SingletonLazy();
        }
        return instance;
    }
    
    public void doSome()
    {
        LogFileUtil.i(MainApplication.TAG, "SingletonLazy -> doSome");
    }
}
