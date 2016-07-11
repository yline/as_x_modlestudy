package com.dm.singleton.dcl;

import com.dm.singleton.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 优点:大部分场景使用
 * 缺点:并发场景较为复杂,或者低于JDK1.6版本下使用,出错概率较大
 */
public class SingletonDCL
{
    /** volatile 它修饰的变量不保留拷贝,直接访问主内存 */
    private volatile static SingletonDCL instance = null;
    
    private SingletonDCL()
    {
    }
    
    public static SingletonDCL getInstance()
    {
        if (null == instance)
        {
            synchronized (SingletonDCL.class)
            {
                if (null == instance)
                {
                    LogFileUtil.i(MainApplication.TAG, "SingletonDCL -> getInstance, new");
                    instance = new SingletonDCL();
                }
            }
        }
        return instance;
    }
    
    public void doSome()
    {
        LogFileUtil.i(MainApplication.TAG, "SingletonDCL -> doSome");
    }
}
