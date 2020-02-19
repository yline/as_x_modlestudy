package com.yline.plugins.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class SubSecondActivity extends BaseTestActivity {
    public static void launch(Context context){
        if (null != context){
            Intent intent = new Intent();
            intent.setClass(context, SubSecondActivity.class);
            if (!(context instanceof Activity)){
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
    
    @Override
    public void testStart(View view, Bundle savedInstanceState) {

    }
}
