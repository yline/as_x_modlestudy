package com.view.wm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.view.wm.manager.FloatWindowManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {
    private static final int PERMISSION = 1000;  //权限申请

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        FloatService.registerCircleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("xxx-点悬浮框");
                IApplication.toast("点击悬浮框");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!(requestCode == PERMISSION && !FloatWindowManager.hasPermissionsGranted(MainActivity.this, permissions))) { // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "悬浮框权限未通过", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        // 权限请求
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> lackedPermission = new ArrayList<>();
            if (!(checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
            }

            if (lackedPermission.size() > 0) {
                String[] requestPermissions = new String[lackedPermission.size()];
                lackedPermission.toArray(requestPermissions);
                requestPermissions(requestPermissions, PERMISSION);
            }
        }
    }

}
