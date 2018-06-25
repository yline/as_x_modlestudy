package com.aidl.user.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.aidl.IService;
import com.yline.log.LogFileUtil;

/**
 * 提供者aidl文件包名要求和使用者一模一样
 *
 * @author YLine
 *         <p>
 *         2016年7月30日 下午4:16:24
 */
public class AIDLManager {
    private static final String ACTION_FILTER = "com.yline.aidl.provider";

    // 使用的是本包内的Iservice
    private IService iService;

    /**
     * 绑定远程服务
     */
    public void testBind(Context context) {
        Intent bindIntent = new Intent().setAction(ACTION_FILTER);
        context.bindService(bindIntent, new ServiceConn(), Context.BIND_AUTO_CREATE);
    }

    /**
     * 调用远程服务一次
     */
    public void testCall() {
        if (null != iService) {
            try {
                iService.callMethodInService();
                LogFileUtil.v(MainApplication.TAG, "AIDLUser -> testCall -> success!!!");
            } catch (RemoteException e) {
                LogFileUtil.e(MainApplication.TAG, "AIDLUser -> testCall -> failed");
            }
        }
    }

    /**
     * 使用远程服务的前提
     *
     * @author YLine
     *         <p>
     *         2016年7月30日 下午4:03:49
     */
    public class ServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // IBinder 转成自己需要的格式,对象
            iService = IService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    }
}
