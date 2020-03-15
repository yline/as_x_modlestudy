package com.yline.runtime.xutils.lib.view.event;

import android.view.View;


import com.yline.runtime.xutils.lib.utils.CheckUtils;
import com.yline.runtime.xutils.lib.utils.LogIoc;
import com.yline.runtime.xutils.lib.view.ViewFinderCompat;
import com.yline.runtime.xutils.lib.view.annotation.Event;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashSet;

/**
 * Event 实现
 */
public class EventManager {
    private final static long CLICK_TIME_SPAN = 400; // ms
    private final static HashSet<String> AVOID_QUICK_EVENT_SET = new HashSet<>(2);

    static {
        AVOID_QUICK_EVENT_SET.add("onClick");
        AVOID_QUICK_EVENT_SET.add("onItemClick");
    }

    private final static DoubleKeyValueMap<Integer, Class<?>, Object> mCache = new DoubleKeyValueMap<Integer, Class<?>, Object>();

    /**
     * Event 实现
     *
     * @param object 处理对象
     * @param compat view处理
     */
    public static void init(Object object, ViewFinderCompat compat) {
        if (null == object || null == compat) {
            return;
        }

        initDfs(object, object.getClass(), compat);
    }

    private static void initDfs(Object object, Class<?> clazz, ViewFinderCompat compat) {
        if (CheckUtils.isIgnore(clazz)) {
            return;
        }

        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                // 静态方法不处理
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                handleEvent(object, method, compat);
            }
        }

        initDfs(object, clazz.getSuperclass(), compat);
    }

    /**
     * 1，调用set方法，设置点击事件
     * 2，用户手动触发点击事件，触发DynamicHandler的invoke方法
     * 3，invoke方法中，调用注解的api
     *
     * @param object 注解所在的对象
     * @param method 注解的方法
     * @param compat 注解作用的view
     */
    private static void handleEvent(Object object, Method method, ViewFinderCompat compat) {
        Event event = method.getAnnotation(Event.class);
        if (null != event) {
            try {
                // id参数
                int[] values = event.value();   // id
                int[] parentIds = event.parentId(); // pid

                String setListenerName = event.setter(); // setOnClickListener
                Class<?> listenerType = event.type(); // View.OnClickListener.class

                // 循环所有id，生成ViewInfo并添加代理反射
                for (int i = 0; i < values.length; i++) {
                    int value = values[i];
                    if (value > 0) {
                        method.setAccessible(true);

                        int pid = i < parentIds.length ? parentIds[i] : 0;
                        View targetView = compat.findViewById(value, pid);
                        if (null != targetView) {
                            // 监听事件所有的相关量
                            Object listener = getRealListener(object, method, value, listenerType);

                            // 调用setOnClickListener方法，设置进去
                            Method setListenerMethod = targetView.getClass().getMethod(setListenerName, listenerType);
                            setListenerMethod.invoke(targetView, listener);
                        }

                        LogIoc.v("handle event ok!! method = " + method.getName());
                    }
                }
            } catch (Throwable ex) {
                LogIoc.e(ex.getMessage(), ex);
            }
        }
    }

    private static Object getRealListener(Object targetObject, Method targetMethod, int id, Class<?> listenerType) {
        Object listener = mCache.get(id, listenerType);
        LogIoc.v("targetObject = " + targetObject + ", targetMethod = " + targetMethod +
                ", listener = " + listener);

        // 新的就重新创建
        if (null == listener) {
            return createNewListener(targetObject, targetMethod, id, listenerType);
        }

        // 已有值，则考虑选择复用
        DynamicHandler dynamicHandler = (DynamicHandler) Proxy.getInvocationHandler(listener);

        // 对象已经改变，则重新创建
        if (!targetObject.equals(dynamicHandler.getTargetObject())) {
            return createNewListener(targetObject, targetMethod, id, listenerType);
        }

        dynamicHandler.setTargetMethod(targetMethod);
        return listener;
    }

    private static Object createNewListener(Object targetObject, Method targetMethod, int id, Class<?> listenerType) {
        DynamicHandler dynamicHandler = new DynamicHandler(targetObject, targetMethod);
        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, dynamicHandler);
        mCache.put(id, listenerType, listener);
        return listener;
    }

    private static class DynamicHandler implements InvocationHandler {
        private final WeakReference<Object> mObjectReference;
        private WeakReference<Method> mMethodReference;

        private static long mLastClickTime;

        private DynamicHandler(Object object, Method method) {
            mObjectReference = new WeakReference<>(object);
            mMethodReference = new WeakReference<>(method);
        }

        public Object getTargetObject() {
            return mObjectReference.get();
        }

        public void setTargetMethod(Method method) {
            if (null != method) {
                mMethodReference = new WeakReference<>(method);
            }
        }

        /**
         * 代理方法
         *
         * @param proxy  DynamicHandler 自身
         * @param method 操作调用的方法，此处为: View.OnClickListener.onClick(View view) 方法
         * @param args   操作调用的方法的参数，此处为：View.OnClickListener.onClick(View view) 参数
         * @return 系统调用method 之后，可能的返回参数
         * @throws Throwable 异常
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (null == method || "toString".equals(method.getName())) {
                return DynamicHandler.class.getSimpleName();
            }

            Object targetObject = mObjectReference.get();
            Method targetMethod = mMethodReference.get();

            LogIoc.v("Dynamic invoke start, targetObject = " + targetObject + ", targetMethod = " + targetMethod);
            if (null != targetObject && null != targetMethod) {
                // 快速响应「点击」过滤
                String eventMethodName = method.getName();
                if (AVOID_QUICK_EVENT_SET.contains(eventMethodName)) {
                    if (System.currentTimeMillis() - mLastClickTime < CLICK_TIME_SPAN) {
                        LogIoc.v("onClick cancelled: " + (System.currentTimeMillis() - mLastClickTime));
                        return null;
                    }
                    mLastClickTime = System.currentTimeMillis();
                }

                // 调用注解的方法
                try {
                    LogIoc.v("targetMethod = " + targetMethod.getName() + ", targetObject = " + targetObject);
                    return targetMethod.invoke(targetObject, args);
                } catch (Throwable ex) {
                    throw new RuntimeException("invoke method error:" +
                            targetObject.getClass().getName() + "#" + method.getName(), ex);
                }
            }
            return null;
        }
    }
}
