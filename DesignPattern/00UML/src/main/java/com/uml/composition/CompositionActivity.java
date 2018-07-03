package com.uml.composition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uml.composition.a.ViewRootImpl;
import com.uml.composition.b.WindowManagerGlobal;
import com.uml.composition.c.Session;
import com.yline.test.BaseTestActivity;

/**
 * 组合
 *
 * @author yline 2018/6/28 -- 15:53
 * @version 1.0.0
 */
public class CompositionActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, CompositionActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        // 全局参数，ViewRootImpl使用
        // ViewRootImpl + ViewRootImpl.W
        // PhoneView + PhoneView.DecorView
        addButton("StyleA - 构造函数创建", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewRootImpl();
            }
        });

        // 全局参数，默认 同时构造
        // WindowManagerGlobal + ViewRootImpl(列表)
        // WindowManagerService + WindowState(列表)
        addButton("StyleB - 全局参数创建", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WindowManagerGlobal();
            }
        });

        // 全局参数，函数控制构造
        // Session + SurfaceSession
        // WindowManagerService + Session
        addButton("StyleC - 调用函数创建", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Session();
            }
        });
    }
}
