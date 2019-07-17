package com.aidl.binder.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;

import com.aidl.binder.binder.IService;
import com.yline.utils.LogUtil;

import java.util.List;

/**
 * 提供者aidl文件包名要求和使用者一模一样
 *
 * @author YLine
 * 2016年7月30日 下午4:16:24
 */
public class AIDLManager {
    private static final String ACTION_FILTER = "com.yline.aidl.binder";

    /**
     * 绑定远程服务
     */
    public static ServiceConn bindServiceConn(Context context) {
        Intent tempIntent = new Intent(ACTION_FILTER);
        final Intent bindIntent = createExplicitFromImplicitIntent(context, tempIntent);

        ServiceConn serviceConn = new ServiceConn();
        context.bindService(bindIntent, serviceConn, Context.BIND_AUTO_CREATE);
        return serviceConn;
    }

    private static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    /**
     * 调用远程服务一次
     */
    public static void callServiceConn(ServiceConn conn) {
        conn.callMethodService();
    }

    /**
     * 对象是否存活
     *
     * @return true 服务还在
     */
    public static boolean isServiceConnAlive(ServiceConn conn) {
        return conn.isConnected();
    }

    /**
     * 使用远程服务的前提
     */
    public static class ServiceConn implements ServiceConnection {
        // 使用的是本包内的Iservice
        private IService iService;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // IBinder 转成自己需要的格式,对象
            iService = IService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService = null;
        }

        private void callMethodService() {
            if (null != iService) {
                try {
                    iService.callMethodInService();
                    LogUtil.v("AIDLUser -> callServiceConn -> success!!!");
                } catch (RemoteException e) {
                    LogUtil.e("AIDLUser -> callServiceConn -> failed", e);
                }
            }
        }

        private boolean isConnected() {
            return null != iService;
        }
    }
}
