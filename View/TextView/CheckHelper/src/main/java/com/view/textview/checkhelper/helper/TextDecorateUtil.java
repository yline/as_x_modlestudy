package com.view.textview.checkhelper.helper;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * 文字校验类
 *
 * @author yline 2017/7/18 -- 16:52
 * @version 1.0.0
 */
public class TextDecorateUtil
{
	/**
	 * 校验手机号
	 *
	 * @param inputString 输入的字符串
	 * @return 是否符合规则
	 */
	public static boolean isPhoneMatch(String inputString)
	{
		if (TextUtils.isEmpty(inputString))
		{
			return false;
		}

		if (inputString.length() == 11)
		{
			return true;
		}

		return false;
	}

	/**
	 * 校验密码
	 *
	 * @param inputString 输入的字符串
	 * @return 是否符合规则
	 */
	public static boolean isPhonePwdMatch(String inputString)
	{
		if (TextUtils.isEmpty(inputString))
		{
			return false;
		}

		if (inputString.length() > 5 && inputString.length() < 17)
		{
			return true;
		}

		return false;
	}

	/**
	 * 校验 短信验证码
	 *
	 * @param inputString 输入的字符串
	 * @return 是否符合规则
	 */
	public static boolean isIdentifyCodeMatch6(String inputString)
	{
		if (TextUtils.isEmpty(inputString))
		{
			return false;
		}

		if (inputString.length() == 6)
		{
			return true;
		}

		return false;
	}

	public static void decorateProtocolTextView(@NonNull TextView tvProtocol, int from, int length, final ClickableSpan callback)
	{
		decorateProtocolTextView(tvProtocol, from, length, 0xffff0000, true, callback);
	}

	public static void decorateProtocolTextView(@NonNull TextView tvProtocol, int from, int length, @ColorInt int clickableColor, final boolean isUnderLine, final ClickableSpan callback)
	{
		int totalLength = tvProtocol.length();
		if (totalLength < from + length)
		{
			throw new IllegalArgumentException("Protocol decorate failed");
		}

		String protocolText = tvProtocol.getText().toString().trim();
		SpannableString spannableString = new SpannableString(protocolText);
		spannableString.setSpan(new ClickableSpan()
		{
			@Override
			public void updateDrawState(TextPaint ds)
			{
				if (!isUnderLine)
				{
					ds.setUnderlineText(false); // 出去下划线
				}
				else
				{
					super.updateDrawState(ds);
				}
			}

			@Override
			public void onClick(View widget)
			{
				if (null != callback)
				{
					callback.onClick(widget);
				}
			}
		}, from, from + length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(clickableColor), from, from + length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		tvProtocol.setText(spannableString);
		tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
