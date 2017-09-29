package com.view.textview.checkhelper.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 手机号 + 密码 + 协议 校验
 *
 * @author yline 2017/7/18 -- 15:27
 * @version 1.0.0
 */
public class PhonePwdHelper
{
	private boolean isResultMatch;

	private boolean isPhoneMatch;

	private boolean isPwdMatch;

	private boolean isProtocolChecked;

	private OnCheckResultListener onCheckResultListener;

	public PhonePwdHelper(EditText etPhone, EditText etPwd)
	{
		this(etPhone, etPwd, true);
	}

	public PhonePwdHelper(EditText etPhone, EditText etPwd, boolean isProtocolChecked)
	{
		initCheckView(etPhone, etPwd);
		this.isProtocolChecked = isProtocolChecked;
	}

	private void initCheckView(EditText etPhone, EditText etPwd)
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
				if (isPhoneMatch != isMatch)
				{
					isPhoneMatch = isMatch;
					if (isPwdMatch)
					{
						isResultMatch = (isMatch && isProtocolChecked);
						if (null != onCheckResultListener)
						{
							onCheckResultListener.onChecked(isResultMatch);
						}
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});

		// 校验，密码
		etPwd.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				boolean isMatch = onPwdTextChanged(s.toString(), start, before, count);
				if (isPwdMatch != isMatch)
				{
					isPwdMatch = isMatch;
					if (isPhoneMatch)
					{
						isResultMatch = (isMatch && isProtocolChecked);
						if (null != onCheckResultListener)
						{
							onCheckResultListener.onChecked(isResultMatch);
						}
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s)
			{

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

	/**
	 * 校验Pwd
	 *
	 * @param inputString 输入的字符串
	 * @param start       每次输入的开始位置
	 * @param before      每次输入的前一个位置
	 * @param count       每次输入的 字符个数
	 * @return
	 */
	protected boolean onPwdTextChanged(String inputString, int start, int before, int count)
	{
		return TextDecorateUtil.isPhonePwdMatch(inputString);
	}

	public void setOnCheckResultListener(OnCheckResultListener onCheckResultListener)
	{
		this.onCheckResultListener = onCheckResultListener;
	}

	public boolean isResultMatch()
	{
		return isResultMatch;
	}

	public boolean isPhoneMatch()
	{
		return isPhoneMatch;
	}

	public boolean isPwdMatch()
	{
		return isPwdMatch;
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
			if (null != onCheckResultListener)
			{
				isResultMatch = (isProtocolChecked && isPhoneMatch && isPwdMatch);
				onCheckResultListener.onChecked(isResultMatch);
			}
		}
	}

	public interface OnCheckResultListener
	{
		/**
		 * @param isMatch 传入的几个控件，是否符合要求
		 */
		void onChecked(boolean isMatch);
	}
}
