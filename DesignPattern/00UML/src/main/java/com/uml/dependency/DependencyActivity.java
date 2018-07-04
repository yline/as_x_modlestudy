package com.uml.dependency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uml.dependency.a.Client;
import com.uml.dependency.b.MacbookBuilder;
import com.yline.test.BaseTestActivity;

public class DependencyActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, DependencyActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("StyleA - 静态方法调用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Client();
            }
        });

        addButton("StyleB - 局部变量调用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MacbookBuilder().create();
            }
        });
    }
}
