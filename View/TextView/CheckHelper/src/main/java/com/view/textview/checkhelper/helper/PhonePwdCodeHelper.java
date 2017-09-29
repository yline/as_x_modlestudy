package com.view.textview.checkhelper.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 手机 + 验证码 + 密码 + 协议 校验
 *
 * @author yline 2017/7/19 -- 17:06
 * @version 1.0.0
 */
public class PhonePwdCodeHelper
{
	private boolean isResultMatch;

	private boolean isPhoneMatch;

	private boolean isIdentifyCodeMatch;

	private boolean isPwdMatch;

	private boolean isProtocolChecked;

	private OnCheckResultListener onCheckResultListener;

	public PhonePwdCodeHelper(EditText etPhone, EditText etCode, EditText etPwd)
	{
		this(etPhone, etCode, etPwd, true);
	}

	public PhonePwdCodeHelper(EditText etPhone, EditText etCode, EditText etPwd, boolean protocolChecked)
	{
		initCheckView(etPhone, etCode, etPwd);
		this.isProtocolChecked = protocolChecked;
	}

	private void initCheckView(EditText etPhone, EditText etCode, EditText etPwd)
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
					if (isIdentifyCodeMatch && isPwdMatch)
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

		// 校验，短信验证码
		etCode.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				boolean isMatch = onCodeTextChanged(s.toString(), start, before, count);
				if (isIdentifyCodeMatch != isMatch)
				{
					isIdentifyCodeMatch = isMatch;
					if (isPhoneMatch && isPwdMatch)
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
					if (isPhoneMatch && isIdentifyCodeMatch)
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
	 * 校验Phone
	 *
	 * @param inputString 输入的字符串
	 * @param start       每次输入的开始位置
	 * @param before      每次输入的前一个位置
	 * @param count       每次输入的 字符个数
	 * @return
	 */
	protected boolean onCodeTextChanged(String inputString, int start, int before, int count)
	{
		return TextDecorateUtil.isIdentifyCodeMatch6(inputString);
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

	public boolean isResultMatch()
	{
		return isResultMatch;
	}

	public boolean isPhoneMatch()
	{
		return isPhoneMatch;
	}

	public boolean isIdentifyCodeMatch()
	{
		return isIdentifyCodeMatch;
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
				isResultMatch = (isProtocolChecked && isPhoneMatch && isPwdMatch && isIdentifyCodeMatch);
				onCheckResultListener.onChecked(isResultMatch);
			}
		}
	}

	public void setOnCheckResultListener(OnCheckResultListener onCheckResultListener)
	{
		this.onCheckResultListener = onCheckResultListener;
	}

	public interface OnCheckResultListener
	{
		/**
		 * @param isMatch 传入的几个控件，是否符合要求
		 */
		void onChecked(boolean isMatch);
	}
}
