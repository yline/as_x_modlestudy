package com.view.textview.checkhelper.helper;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 手机号 + 短信验证码 + 协议 验证
 *
 * @author yline 2017/7/18 -- 17:35
 * @version 1.0.0
 */
public class PhoneICodeHelper
{
	private boolean isPhoneMatch;

	private boolean isIdentifyCountDown;

	private boolean isProtocolChecked;

	private boolean isResultMatch; // 单指 手机号 + 协议

	private TextView tvIdentifyCode;

	private OnIdentifyCodeListener onIdentifyCodeListener;

	private IdentifyCountDownTask countDownTask;

	public PhoneICodeHelper(EditText etPhone, TextView tvIdentifyCode)
	{
		this(etPhone, tvIdentifyCode, true);
	}

	public PhoneICodeHelper(@NonNull EditText etPhone, @NonNull TextView tvIdentifyCode, boolean isProtocolChecked)
	{
		initCheckView(etPhone, tvIdentifyCode);
		this.isProtocolChecked = isProtocolChecked;
		this.tvIdentifyCode = tvIdentifyCode;
	}

	private void initCheckView(EditText etPhone, TextView tvCode)
	{
		// 校验，手机号
		etPhone.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				boolean isMatch = onPhoneTextChanged(s.toString(), start, before, count);
				if (isMatch != isPhoneMatch)
				{
					isPhoneMatch = isMatch;
					if (null != onIdentifyCodeListener)
					{
						isResultMatch = (isMatch && isProtocolChecked);
						onIdentifyCodeListener.onIdentifyStateChange(tvIdentifyCode, isResultMatch, isIdentifyCountDown, -1);
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});

		// 验证码
		tvCode.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				isResultMatch = (isProtocolChecked && isPhoneMatch);
				if (null != onIdentifyCodeListener)
				{
					onIdentifyCodeListener.onIdentifyClick(v, isResultMatch, isIdentifyCountDown);
				}

				if (isResultMatch && !isIdentifyCountDown)
				{
					countDownTask = new IdentifyCountDownTask();
					countDownTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getIdentifyCodeMaxNumber());
				}
			}
		});
	}

	/**
	 * 校验Phone
	 *
	 * @param inputString 输入的字符串
	 * @param start       每次输入的开始位置
	 * @param before      每次输入的前一个位置
	 * @param count       每次输入的 字符个数
	 * @return
	 */
	protected boolean onPhoneTextChanged(String inputString, int start, int before, int count)
	{
		return TextDecorateUtil.isPhoneMatch(inputString);
	}

	public void setOnIdentifyCodeListener(OnIdentifyCodeListener onIdentifyCodeListener)
	{
		this.onIdentifyCodeListener = onIdentifyCodeListener;
	}

	protected int getIdentifyCodeMaxNumber()
	{
		return 60;
	}

	public boolean isProtocolChecked()
	{
		return isProtocolChecked;
	}

	public void setProtocolChecked(boolean protocolChecked)
	{
		if (isProtocolChecked != protocolChecked)
		{
			isProtocolChecked = protocolChecked;
			isResultMatch = (!isIdentifyCountDown && isProtocolChecked && isPhoneMatch);
			if (null != onIdentifyCodeListener)
			{
				onIdentifyCodeListener.onIdentifyStateChange(tvIdentifyCode, isResultMatch, isIdentifyCountDown, -1);
			}
		}
	}

	/**
	 * 跟随activity生命周期
	 */
	public void onDestroy()
	{
		if (null != countDownTask)
		{
			countDownTask.setDestroy(true);
			countDownTask.cancel(true);
		}
	}

	public interface OnIdentifyCodeListener
	{
		/**
		 * 验证码  状态改变
		 *
		 * @param tvIdentify
		 * @param isLegal     是否满足发送验证码条件(单指 手机号 + 协议)
		 * @param isCountDown 是否在倒计时
		 * @param restTime    剩余时间 unit : s  如果为 -1，则数据不可用
		 */
		void onIdentifyStateChange(TextView tvIdentify, boolean isLegal, boolean isCountDown, int restTime);

		/**
		 * 用户点击 发送验证码 按钮 响应事件
		 *
		 * @param view
		 * @param isMatch     手机 + 协议是否满足
		 * @param isCountDown 是否正在倒计时
		 */
		void onIdentifyClick(View view, boolean isMatch, boolean isCountDown);
	}

	private class IdentifyCountDownTask extends AsyncTask<Integer, Integer, Void>
	{
		private boolean destroy;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			isIdentifyCountDown = true;
			destroy = false;
			if (null != onIdentifyCodeListener)
			{
				onIdentifyCodeListener.onIdentifyStateChange(tvIdentifyCode, isResultMatch, isIdentifyCountDown, -1);
			}
		}

		@Override
		protected Void doInBackground(Integer... params)
		{
			int maxNumber = params[0];
			try
			{
				while (!destroy && maxNumber > 0)
				{
					maxNumber--;
					publishProgress(maxNumber);
					Thread.sleep(1000);
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
			if (null != onIdentifyCodeListener)
			{
				onIdentifyCodeListener.onIdentifyStateChange(tvIdentifyCode, false, isIdentifyCountDown, values[0]);
			}
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			super.onPostExecute(aVoid);
			isIdentifyCountDown = false;
			destroy = true;
			if (null != onIdentifyCodeListener)
			{
				onIdentifyCodeListener.onIdentifyStateChange(tvIdentifyCode, isResultMatch, isIdentifyCountDown, -1);
			}
		}

		public void setDestroy(boolean destroy)
		{
			this.destroy = destroy;
		}
	}
}
