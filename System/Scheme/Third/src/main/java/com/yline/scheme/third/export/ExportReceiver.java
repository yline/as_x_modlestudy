package com.yline.scheme.third.export;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yline.scheme.third.util.UriUtils;
import com.yline.utils.LogUtil;

public class ExportReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (null != intent){
			UriUtils.schemePrint(intent.getData());
		}else {
			LogUtil.v("intent is null");
		}
	}
}
