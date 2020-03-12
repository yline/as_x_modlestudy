package com.yline.reflex.sample.construct;

import com.yline.utils.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflexUtils {
    /**
     * 查询所有方法的内容
     */
    public static void queryMethodInfo() {
        try {
            Class clazz = Class.forName("com.yline.reflex.sample.construct.ViewFindHelper");

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                String returnType = method.getReturnType().getSimpleName();
                String methodName = method.getName();

                String tag;
                if (Modifier.PUBLIC == method.getModifiers()) {
                    tag = "public";
                } else if (Modifier.PROTECTED == method.getModifiers()) {
                    tag = "protected";
                } else {
                    tag = "private";
                }

                LogUtil.v(tag + " " + returnType + " " + methodName);
            }
        } catch (Exception e) {
            LogUtil.e("", e);
        }
    }

    /**
     * 查询所有成员变量信息
     */
    public static void queryFiledInfo() {
        try {
            Class clazz = Class.forName("com.yline.reflex.sample.construct.ViewFindHelper");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String tag;
                if (Modifier.PUBLIC == field.getModifiers()) {
                    tag = "public";
                } else if (Modifier.PROTECTED == field.getModifiers()) {
                    tag = "protected";
                } else {
                    tag = "private";
                }

                String type = field.getType().getSimpleName();
                String methodName = field.getName();

                LogUtil.v(tag + " " + type + " " + methodName);
            }
        } catch (Exception e) {
            LogUtil.e("", e);
        }
    }

    /**
     * 查询所有构造方法信息
     */
    public static void queryConstructInfo() {
        try {
            Class clazz = Class.forName("com.yline.reflex.sample.construct.ViewFindHelper");

            Constructor[] constructors = clazz.getConstructors();
            for (Constructor construct : constructors) {
                String name = construct.getName();

                String tag;
                if (Modifier.PUBLIC == construct.getModifiers()) {
                    tag = "public";
                } else if (Modifier.PROTECTED == construct.getModifiers()) {
                    tag = "protected";
                } else {
                    tag = "private";
                }
                StringBuilder sBuilder = new StringBuilder(tag);
                sBuilder.append(" ");
                sBuilder.append(name);

                Class[] classParamTypes = construct.getParameterTypes();
                if (classParamTypes.length == 0) {
                    sBuilder.append("()");
                } else {
                    sBuilder.append('(');
                    for (Class param : classParamTypes) {
                        String simpleName = param.getSimpleName();
                        sBuilder.append(simpleName);
                        sBuilder.append(',');
                    }
                    sBuilder.deleteCharAt(sBuilder.length() - 1); // 删除最后一个逗号

                    sBuilder.append(')');
                }

                LogUtil.v(sBuilder.toString());
            }
        } catch (Exception e) {
            LogUtil.e("", e);
        }
    }

    /**
     * 创建一个类
     */
    public static void doNewInstance() {
        try {
            Class clazz = Class.forName("com.yline.reflex.sample.construct.ViewFindHelper");
            Object obj = clazz.newInstance();
            LogUtil.v("obj = " + obj);
        } catch (Exception e) {
            LogUtil.e("", e);
        }
    }

    /**
     * 调用类方法
     */
    public static void executeMethod() {
        Object obj = null;
        Class clazz = null;
        try {
            // 获取类相关
            clazz = Class.forName("com.yline.reflex.sample.construct.ViewFindHelper");
            obj = clazz.newInstance();
        } catch (Exception ex) {
            LogUtil.e("", ex);
            return;
        }

        try {
            // 获取对象相关; 注意:private方法和protected方法依旧还是支持其权限要求;否则调用不到
            Method methodA = clazz.getDeclaredMethod("testPrivate");
            methodA.setAccessible(true);
            Object objectA = methodA.invoke(obj);
            LogUtil.v("" + objectA);
        } catch (Exception ex) {
            LogUtil.e("", ex);
        }

        try {
            Method methodB = clazz.getDeclaredMethod("testProtect", String.class);
            Object objectB = methodB.invoke(obj, "Jay Zhou");
            LogUtil.v("" + objectB);
        } catch (Exception ex) {
            LogUtil.e("", ex);
        }

        try {
            Method methodC = clazz.getDeclaredMethod("testPublic");
            Object objectC = methodC.invoke(obj);
            LogUtil.v("" + objectC);
        } catch (Exception ex) {
            LogUtil.e("", ex);
        }
    }

    /**
     * 实现，对成员变量，赋值
     */
    public static void setField() {
        Object obj = null;
        Class clazz = null;
        try {
            // 获取类相关
            clazz = Class.forName("com.yline.reflex.sample.construct.ViewFindHelper");
            obj = clazz.newInstance();
        } catch (Exception ex) {
            LogUtil.e("", ex);
            return;
        }

        try {
            LogUtil.v("obj = " + obj);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (fieldName.equals("mSingleString")) {
                    field.setAccessible(true);
                    field.set(obj, "Jay Zhou");
                    continue;
                }

                if (fieldName.equals("mSingleInt")) {
                    field.setAccessible(true);
                    field.set(obj, 100);
                    continue;
                }

                if (fieldName.equals("mSingleLong")) {
                    field.set(obj, Long.MAX_VALUE);
                }
            }
            LogUtil.v("obj = " + obj);
        } catch (Exception ex) {
            LogUtil.e("", ex);
        }
    }
}
