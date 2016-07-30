package com.aidl.provider.provider;

import com.aidl.IService;
import com.aidl.provider.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * 提供一个绑定的服务
 * 提供者aidl文件包名要求和使用者一模一样
 * @author YLine
 *
 * 2016年7月30日 下午3:45:52
 */
public class ProviderService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        LogFileUtil.v(MainApplication.TAG, "ProviderService -> onBind");
        return new ServiceBinder();
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        LogFileUtil.v(MainApplication.TAG, "ProviderService -> onCreate success");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        LogFileUtil.v(MainApplication.TAG, "ProviderService -> onStartCommand success");
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LogFileUtil.v(MainApplication.TAG, "ProviderService -> destroyed");
    }
    
    private class ServiceBinder extends IService.Stub
    {
        @Override
        public void callMethodInService()
            throws RemoteException
        {
            method("ProviderService -> ServiceBinder -> ServiceBinder is called");
        }
    }
    
    private void method(String content)
    {
        LogFileUtil.v(MainApplication.TAG, "ProviderService -> " + content);
    }
}
