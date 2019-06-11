package com.yline.xposed;

import android.util.Log;

import com.yline.utils.LogUtil;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 参考：https://blog.csdn.net/coder_pig/article/details/80031285
 *
 * XC_LoadPackage.LoadPackageParam --> 包含与正在加载的应用程序的有关信息
 * XposedHelpers.findAndHookMethod --> hook到的对应的类以及方法
 * MethodHookParam methodHookParam --> 包含与调用方法有关的信息
 *
 * 具体使用，和反射基本类似
 *
 * @author yline 2019/6/10 -- 18:01
 */
public class XposedHookUtil implements IXposedHookLoadPackage {
    private static final String className = "com.yline.xposed.MainActivity";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        LogUtil.v("handleLoadPackage");

        // 实现拦截
        Class clazz = loadPackageParam.classLoader.loadClass(className); // 累

        Field field = clazz.getField("getXposedString"); // 方法
        field.setAccessible(true); // 私有的，则需要先设置权限

        XposedHelpers.findAndHookMethod(clazz, "getXposedString", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {

                // methodHookParam.thisObject 调用该方法的对象实例

                return "广告被拦截了";
            }
        });

    }
}
