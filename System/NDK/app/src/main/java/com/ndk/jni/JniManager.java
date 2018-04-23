package com.ndk.jni;

/**
 * Jni管理类
 *
 * @author yline 2018/4/23 -- 18:42
 * @version 1.0.0
 */
public class JniManager {
    static {
        System.loadLibrary("native-lib");

        System.loadLibrary("native-log");
    }

    /**
     * 获取Jni
     *
     * @return C层的数据
     */
    public native String stringFromJNI();

    /**
     * 使用C层，打印日志
     *
     * @param msg 打印内容
     * @return 返回内容
     */
    public native String logByJni(String msg);
}
