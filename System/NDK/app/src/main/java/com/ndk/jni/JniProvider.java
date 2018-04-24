package com.ndk.jni;

import com.ndk.IApplication;
import com.yline.utils.LogUtil;

/**
 * C调用Java
 *
 * @author yline 2018/4/24 -- 11:19
 * @version 1.0.0
 */
public class JniProvider {
    public String testProvider() {
        IApplication.toast("testProvider");
        LogUtil.v("testProvider");
        return "Android->testProvider";
    }

    public static String testStaticProvider() {
        IApplication.toast("testStaticProvider");
        LogUtil.v("testStaticProvider");
        return "Android->testStaticProvider";
    }
}
