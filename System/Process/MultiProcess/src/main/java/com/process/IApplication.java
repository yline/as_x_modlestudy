package com.process;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.process.service.MainService;
import com.process.service.MusicProcessService;
import com.yline.application.SDKConfig;
import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;

/**
 * @author yline 2018/4/19 -- 15:18
 * @version 1.0.0
 */
public class IApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SDKManager.init(this, new SDKConfig());

        MainService.launcher(this);
        MusicProcessService.launcher(this);
//        PushProcessActivity.launcher(this);
//        RemoteProcessService.launcher(this);

        if (isNameProcess("")) {
            LogUtil.v("MainProcess");
        } else if (isNameProcess(":core")) {
            LogUtil.v("CoreProcess");
        } else if (isNameProcess(":image")) {
            LogUtil.v("ImageProcess");
        } else if (isNameProcess(":push")) {
            LogUtil.v("PushProcess");
        } else if (isNameProcess(":music")) {
            LogUtil.v("MusicProcess");
        }
    }

    private boolean isNameProcess(String processSuffix) {
        int pid = android.os.Process.myPid(); // 同一个应用，不同进程，pid不同
        String processName = "";

        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (null != manager) {
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    processName = processInfo.processName;
                    break;
                }
            }
            return (!TextUtils.isEmpty(processName) && processName.equals(getPackageName() + processSuffix));
        }
        return false;
    }
}
