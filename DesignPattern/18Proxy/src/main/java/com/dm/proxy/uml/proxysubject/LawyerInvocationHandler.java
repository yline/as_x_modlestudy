package com.dm.proxy.uml.proxysubject;

import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态实现代理，没有直接接触到接口
 *
 * @author yline 2020-04-11 -- 12:57
 */
public class LawyerInvocationHandler implements InvocationHandler {
    public static Object newProxy(Object targetObj, Class<?> targetClazz) {
        return Proxy.newProxyInstance(targetClazz.getClassLoader(), new Class[]{targetClazz}, new LawyerInvocationHandler(targetObj));
    }

    private Object targetObj; // 被代理的类引用

    public LawyerInvocationHandler(Object targetObj) {
        this.targetObj = targetObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LogUtil.v("LawyerInvocationHandler -> invoke before");
        Object result = method.invoke(targetObj, args);
        LogUtil.v("LawyerInvocationHandler -> invoke after");
        return result;
    }

}
