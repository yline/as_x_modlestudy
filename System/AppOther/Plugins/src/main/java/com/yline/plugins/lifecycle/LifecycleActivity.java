package com.yline.plugins.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.yline.test.BaseTestActivity;

/**
 * @author yline 2020-02-19 -- 15:34
 */
public class LifecycleActivity extends BaseTestActivity implements LifecycleOwner {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, LifecycleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private LifecycleRegistry registry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(new LifecycleObserver());
        getLifecycle().addObserver(new LifecycleCustomObserver());
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        if (null == registry) {
            registry = new LifecycleRegistry(this);
        }
        return registry;
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("SubSecond", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubSecondActivity.launch(LifecycleActivity.this);
            }
        });
    }
}
