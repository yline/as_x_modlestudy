package com.view.pattern.lock;

import com.view.pattern.lock.view.LockPatternHelper;
import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication {
	
	@Override
	public void onCreate() {
		super.onCreate();
		LockPatternHelper.getInstance().setApplication(this);
	}
}
