package com.yline.xposed;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author yline 2019/6/10 -- 18:01
 */
public class XposedHookUtil implements IXposedHookLoadPackage {
    private static final String className = "com.yline.xposed.MainActivity";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // 实现拦截
        Class clazz = loadPackageParam.classLoader.loadClass(className);
        XposedHelpers.findAndHookMethod(clazz, "getXposedString", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                return "广告被拦截了";
            }
        });
    }
}
