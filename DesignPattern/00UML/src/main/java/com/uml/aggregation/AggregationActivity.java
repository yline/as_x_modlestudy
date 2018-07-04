package com.uml.aggregation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class AggregationActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, AggregationActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {

    }
}
