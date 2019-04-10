package com.unactive.windowmanager.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.system.broadcast.receiver.unactivemanager.R;
import com.unactive.windowmanager.receiver.PassViewReceiver;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

/**
 * 利用DeviceAdminReceiver 实现防卸载功能,通过windowManager的方式
 *
 * @author YLine 2016/8/8 --> 22:44
 * @version 1.0.0
 */
public class MainActivity extends BaseActivity {
    private DevicePolicyManager mDevicePolicyManager;

    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        // 激活
        findViewById(R.id.btn_active).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isActive = mDevicePolicyManager.isAdminActive(mComponentName);
                LogUtil.v("btn_active onClicked, isActive = " + isActive);

                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                MainActivity.this.startActivity(intent);
            }
        });

        // 取消激活
        findViewById(R.id.btn_unactive).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LogUtil.v("btn_unactive onClicked");
                mDevicePolicyManager.removeActiveAdmin(mComponentName);
                LogUtil.v("removeActiveAdmin isActive = " + mDevicePolicyManager.isAdminActive(mComponentName));
            }
        });
    }

    private void initData() {
        mDevicePolicyManager = (DevicePolicyManager) MainApplication.getApplication().getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(MainApplication.getApplication(), PassViewReceiver.class);
    }
}
