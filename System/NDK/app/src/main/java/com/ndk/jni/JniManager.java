package com.ndk.jni;

/**
 * Java调用C的方法
 * Jni管理类
 *
 * @author yline 2018/4/23 -- 18:42
 * @version 1.0.0
 */
public class JniManager {
    static {
        System.loadLibrary("native-manager");
    }

    private static JniManager mJniManager;

    public static JniManager getInstance() {
        if (null == mJniManager) {
            synchronized (JniManager.class) {
                if (null == mJniManager) {
                    mJniManager = new JniManager();
                }
            }
        }
        return mJniManager;
    }

    /**
     * 获取Jni 中定义的 String字符串
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

    /**
     * 加密
     *
     * @param msg  原始字符串
     * @param type 加密的方式
     * @return 加密后的数据
     */
    public native String encode(String msg, int type);

    /**
     * 解密
     *
     * @param msg  解密前的 字符串
     * @param type 解密的方式
     * @return 解密后的数据
     */
    public native String decode(String msg, int type);

    /**
     * 发生一次 Crash
     * @param msg 崩溃信息
     */
    public native void doCrash(String msg);
}
