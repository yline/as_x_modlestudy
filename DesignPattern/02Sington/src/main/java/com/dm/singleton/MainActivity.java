package com.dm.singleton;

import android.os.Bundle;
import android.view.View;

import com.dm.singleton.dcl.SingletonDCL;
import com.dm.singleton.enums.SingletonEnum;
import com.dm.singleton.lazy.SingletonLazy;
import com.dm.singleton.statics.SingletonStatic;
import com.yline.test.BaseTestActivity;
import com.yline.utils.FileUtil;
import com.yline.utils.LogUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("懒汉模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("initView -> btn_lazy");
                SingletonLazy.getInstance().doSome();
            }
        });

        addButton("double check lock模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("initView -> btn_dcl");
                SingletonDCL.getInstance().doSome();

                breakByReflect(SingletonDCL.class);
                breakByStream(SingletonDCL.getInstance(), "SingletonDCL");
            }
        });

        addButton("静态内部类模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("initView -> btn_static");
                SingletonStatic.getInstance().doSome();

                breakByReflect(SingletonStatic.class);
                breakByStream(SingletonStatic.getInstance(), "SingletonStatic");
            }
        });

        addButton("枚举单例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("initView -> btn_enum");
                SingletonEnum.INSTANCE.doSome();
            }
        });

        addButton("使用容器实现单例模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("initView -> btn_static");
                LogUtil.v("该项,直接看代码,不进行测试");
            }
        });
    }

    /**
     * 通过反射的方式，破坏单例
     */
    private static void breakByReflect(Class<?> clazz) {
        try {
            Constructor c = clazz.getDeclaredConstructor();
            c.setAccessible(true);

            Object o1 = c.newInstance();
            Object o2 = c.newInstance();

            // 是否重复创建，可以通过让它失败，不允许破坏
            LogUtil.v("is double created = " + (o1 != o2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过序列化的方式，破坏单例
     */
    private static void breakByStream(Object object, String fileName) {
        String filePath = FileUtil.getFileTop("yline-" + fileName).getAbsolutePath();

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();

            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object copyObject = ois.readObject();
            ois.close();

            // 是否重复创建，可以通过让它失败，不允许破坏
            LogUtil.v("is double created = " + (object != copyObject));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
