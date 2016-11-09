package com.system.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yline.log.LogFileUtil;

/**
 * 该示例包含:
 * 1,生命周期(onCreate、onStartCommand、onDestroy)
 * 2,binder流程(onBind、onUnbind);可以通过Binder通信(ServiceConnection)
 * onCreate首次开启的时候调用
 * onStartCommand每次开启的时候,都会调用一次
 * @author yline 2016/11/10 --> 1:41
 * @version 1.0.0
 */
public class TestService extends Service
{
	private static final String TAG = "TestService";
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		LogFileUtil.v(TAG, "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		int superReturn = super.onStartCommand(intent, flags, startId);
		LogFileUtil.v(TAG, "onStartCommand superResult = " + superReturn);
		return superReturn;
	}
	
	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		LogFileUtil.v(TAG, "onBind");
		return new TestBinder();
	}
	
	@Override
	public boolean onUnbind(Intent intent)
	{
		boolean superReturn = super.onUnbind(intent);
		LogFileUtil.v(TAG, "onUnbind superResult = " + superReturn);
		return superReturn;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		LogFileUtil.v(TAG, "onDestroy");
	}
	
	public static void serviceStart(Context context)
	{
		Intent intent = new Intent();
		intent.setClass(context, TestService.class);
		context.startService(intent);
	}
	
	public static void serviceStop(Context context)
	{
		Intent intent = new Intent();
		intent.setClass(context, TestService.class);
		context.stopService(intent);
	}
	
	public static void serviceBind(Context context, @NonNull ServiceConnection conn, int flags)
	{
		Intent intent = new Intent();
		intent.setClass(context, TestService.class);
		context.bindService(intent, conn, flags);
	}
	
	public static void serviceUnbind(Context context, @NonNull ServiceConnection conn)
	{
		context.unbindService(conn);
		conn = null;
	}
	
	public class TestBinder extends Binder implements ITestBinderCallback
	{
		
		@Override
		public void execute()
		{
			LogFileUtil.v(TAG, "TestBinder execute");
		}
	}
	
	public interface ITestBinderCallback
	{
		void execute();
	}
}
