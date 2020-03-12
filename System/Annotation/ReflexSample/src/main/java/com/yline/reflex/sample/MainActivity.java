package com.yline.reflex.sample;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.lang.reflect.Method;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("调用某个类的方法", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doInfoClass();
            }
        });
    }

    private void doInfoClass() {
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
}
