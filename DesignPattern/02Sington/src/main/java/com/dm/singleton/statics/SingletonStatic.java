package com.dm.singleton.statics;

import com.yline.utils.LogUtil;

import java.io.Serializable;

/**
 * 推荐使用的单例模式
 */
public class SingletonStatic implements Serializable {
    private static final long serialVersionUID = 2819686490699367375L;

    private SingletonStatic() {
        // 防止反射创建多个对象
        if (null != getInstance()) {
            throw new RuntimeException("不允许创建多个实例");
        }
    }

    public static SingletonStatic getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * 静态内部类
     */
    private static class SingletonHolder {
        private static final SingletonStatic sInstance = new SingletonStatic();
    }

    public void doSome() {
        LogUtil.v("SingletonStatic -> doSome");
    }

    // 防止序列化创建多个对象,这个方法是关键
    private Object readResolve() {
        return getInstance();
    }
}
