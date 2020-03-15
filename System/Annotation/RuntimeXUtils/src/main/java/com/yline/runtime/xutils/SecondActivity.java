package com.yline.runtime.xutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.yline.runtime.xutils.lib.ViewInjectorManager;
import com.yline.runtime.xutils.lib.view.annotation.ContentView;


@ContentView(R.layout.activity_second)
public class SecondActivity extends FragmentActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, SecondActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewInjectorManager.getInstance().inject(this);
        fragmentManager.beginTransaction().add(R.id.second_container, new SecondFragment()).commit();
    }
}
