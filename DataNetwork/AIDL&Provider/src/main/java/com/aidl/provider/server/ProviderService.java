package com.aidl.provider.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.aidl.IService;
import com.yline.utils.LogUtil;

/**
 * 提供一个绑定的服务
 * 提供者aidl文件包名要求和使用者一模一样
 *
 * @author YLine 2016/8/7 --> 16:36
 * @version 1.0.0
 */
public class ProviderService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.v("onBind");
        return new ServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.v("onCreate success");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.v("onStartCommand success");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v("destroyed");
    }

    /**
     * IService,会新建到一个与java同等级的目录上
     *
     * @author YLine 2016/8/7 --> 16:46
     * @version 1.0.0
     */
    private class ServiceBinder extends IService.Stub {
        @Override
        public void callMethodInService() {
            method("callMethodInService");
        }
    }

    private void method(String content) {
        LogUtil.v("method called -> " + content);
    }
}
