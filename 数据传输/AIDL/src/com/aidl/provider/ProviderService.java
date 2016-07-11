package com.aidl.provider;

import com.aidl.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * 提供一个绑定的服务
 * simple introduction
 *
 * @author YLine 2016-5-21 -> 下午6:47:52
 * @version 
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
