package com.dm.singleton.dcl;

import com.yline.utils.LogUtil;

import java.io.Serializable;

/**
 * 优点:大部分场景使用
 * 缺点:并发场景较为复杂,或者低于JDK1.6版本下使用,出错概率较大
 */
public class SingletonDCL implements Serializable {
    private static final long serialVersionUID = 5885253168551397152L;

    /**
     * volatile 的 作用为防止 指令重排
     */
    private volatile static SingletonDCL instance = null;

    private SingletonDCL() {
        // 防止反射创建多个对象
        if (null != instance) {
            throw new RuntimeException("不允许创建多个实例");
        }
    }

    public static SingletonDCL getInstance() {
        if (null == instance) {
            synchronized (SingletonDCL.class) {
                if (null == instance) {
                    LogUtil.v("SingletonDCL -> getInstance, new");
                    instance = new SingletonDCL();
                }
            }
        }
        return instance;
    }

    public void doSome() {
        LogUtil.v("SingletonDCL -> doSome");
    }

    // 防止序列化创建多个对象,这个方法是关键
    private Object readResolve() {
        return getInstance();
    }
}
