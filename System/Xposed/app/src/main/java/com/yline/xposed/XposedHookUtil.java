package com.yline.xposed;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 调用时机：dex被系统加载的时候
 * <p>
 * 参考：https://blog.csdn.net/coder_pig/article/details/80031285
 * <p>
 * XC_LoadPackage.LoadPackageParam --> 包含与正在加载的应用程序的有关信息
 * XposedHelpers.findAndHookMethod --> hook到的对应的类以及方法
 * MethodHookParam methodHookParam --> 包含与调用方法有关的信息
 * <p>
 * 具体使用，和反射基本类似
 *
 * @author yline 2019/6/10 -- 18:01
 */
public class XposedHookUtil implements IXposedHookLoadPackage {
    private static final String className = "com.yline.xposed.MainActivity";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.v("xxx-", "handleLoadPackage"); // LogUtils还未初始化

        if (loadPackageParam.packageName.equals("com.yline.xposed")) { // 当前APP
            // 实现拦截
            final Class clazz = loadPackageParam.classLoader.loadClass(className); // 类
            XposedHelpers.findAndHookMethod(clazz, "getXposedString", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    // methodHookParam.thisObject 调用该方法的对象实例
                    return "广告被拦截了";
                }
            });
        } else { // 更多案例
            final Class clazz = loadPackageParam.classLoader.loadClass(className); // 类
            XposedHelpers.findAndHookMethod(clazz, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    Field field = clazz.getDeclaredField("mTextView");
                    field.setAccessible(true);
                    XposedBridge.log("xposed valid");

                    TextView tv = (TextView) field.get(param.thisObject);
                    tv.setText("find And Hook");
                }
            });
        }
    }
}
