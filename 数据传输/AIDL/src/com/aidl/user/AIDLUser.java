package com.aidl.user;

import com.aidl.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * simple introduction
 *
 * @author YLine 2016-5-21 -> 下午7:32:58
 * @version 
 */
public class AIDLUser
{
    // 使用的是本包内的Iservice
    private IService iService;
    
    /**
     * 绑定远程服务
     */
    public void testBind(Context context)
    {
        Intent bindIntent = new Intent().setAction("com.aidl.provider");
        context.bindService(bindIntent, new ServiceConn(), Context.BIND_AUTO_CREATE);
    }
    
    /**
     * 调用远程服务一次
     */
    public void testCall()
    {
        if (null != iService)
        {
            try
            {
                iService.callMethodInService();
                LogFileUtil.v(MainApplication.TAG, "AIDLUser -> testCall -> success!!!");
            }
            catch (RemoteException e)
            {
                LogFileUtil.e(MainApplication.TAG, "AIDLUser -> testCall -> failed");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 使用远程服务的前提
     * simple introduction
     *
     * @author YLine 2016-5-21 -> 下午7:35:48
     * @version AIDLUser
     */
    private class ServiceConn implements ServiceConnection
    {
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            // IBinder 转成自己需要的格式,对象
            iService = IService.Stub.asInterface(service);
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            
        }
        
    }
}
