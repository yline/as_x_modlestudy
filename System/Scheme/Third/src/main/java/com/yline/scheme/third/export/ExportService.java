package com.yline.scheme.third.export;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yline.scheme.third.util.Utils;
import com.yline.utils.LogUtil;

public class ExportService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		LogUtil.v("");
		return null;
	}
	
	@Override
	public void onCreate() {
		LogUtil.v("");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null != intent){
			Utils.schemePrint(intent.getData());
		}else {
			LogUtil.v("intent is null");
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		LogUtil.v("");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		LogUtil.v("");
		super.onDestroy();
	}
}
