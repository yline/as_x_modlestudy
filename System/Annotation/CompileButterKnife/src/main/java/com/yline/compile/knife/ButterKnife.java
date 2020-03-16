package com.yline.compile.knife;

import android.app.Activity;
import android.view.View;

import com.yline.compile.knife.lib.view.Unbinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class ButterKnife {
    private static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS = new LinkedHashMap<>();

    public static Unbinder bind(Activity target) {
        View sourceView = target.getWindow().getDecorView();
        return bind(target, sourceView);
    }

    public static Unbinder bind(Object target, View source) {
        Class<?> targetClass = target.getClass();
        Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(targetClass);
        if (null == constructor) {
            return Unbinder.EMPTY;
        }

        try {
            return constructor.newInstance(target, source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> clazz) {
        Constructor<? extends Unbinder> bindingCtor = BINDINGS.get(clazz);
        if (bindingCtor != null || BINDINGS.containsKey(clazz)) {
            return bindingCtor;
        }

        String clsName = clazz.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.") || clsName.startsWith("androidx.")) {
            return null;
        }

        try {
            Class<?> bindingClass = clazz.getClassLoader().loadClass(clsName + "_ViewBinding");
            //noinspection unchecked
            bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(clazz, View.class);
        } catch (ClassNotFoundException e) {
            bindingCtor = findBindingConstructorForClass(clazz.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
        BINDINGS.put(clazz, bindingCtor);
        return bindingCtor;
    }
}
