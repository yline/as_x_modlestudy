package com.uml.assocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uml.assocation.a.IWindow;
import com.uml.assocation.a.WindowState;
import com.uml.assocation.b.WindowManagerImpl;
import com.uml.assocation.c.WindowManagerGlobal;
import com.yline.test.BaseTestActivity;

public class AssocationActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, AssocationActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("StyleA - 构造函数传入", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WindowState(new IWindow() {
                    @Override
                    public void executeCommand(String command) {
                        // TODO
                    }
                });
            }
        });

        addButton("StyleB - 全局参数引入", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WindowManagerImpl();
            }
        });

        addButton("StyleC - 调用函数引入", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManagerGlobal.getWindowManagerService();
            }
        });
    }
}
