package com.uml.realization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.uml.realization.a.WindowManagerImpl;
import com.uml.realization.b.WindowManagerService;
import com.yline.test.BaseTestActivity;

public class RealizationActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, RealizationActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("StyleA - implements", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WindowManagerImpl().removeView(null);
            }
        });

        addButton("StyleB - aidl", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new WindowManagerService().basicTypes(0, 0, true, 0, 0, "");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
