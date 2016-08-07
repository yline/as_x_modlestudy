package com.dm.proxy.uml.proxy.dynamic;

import com.dm.proxy.uml.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler
{
	private Object targetObj; // 被代理的类引用

	public DynamicProxy(Object targetObj)
	{
		this.targetObj = targetObj;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		LogFileUtil.v(MainApplication.TAG, "DynamicProxy -> invoke before");
		Object result = method.invoke(targetObj, args);
		LogFileUtil.v(MainApplication.TAG, "DynamicProxy -> invoke after");
		return result;
	}

}
