package com.dm.singleton.lazy;

import com.yline.utils.LogUtil;

/**
 * 优点:只有被使用时,才会被实例化
 * 缺点:getInstance,每次调用都会进行同步,造成不必要的开销
 */
public class SingletonLazy {
    private static SingletonLazy instance;

    private SingletonLazy() {
    }

    public static synchronized SingletonLazy getInstance() {
        if (null == instance) {
            LogUtil.v("SingletonLazy -> getInstance, new");
            instance = new SingletonLazy();
        }
        return instance;
    }

    public void doSome() {
        LogUtil.v("SingletonLazy -> doSome");
    }
}
