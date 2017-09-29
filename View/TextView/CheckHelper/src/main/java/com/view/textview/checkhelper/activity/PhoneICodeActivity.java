package com.view.textview.checkhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.view.textview.checkhelper.R;
import com.view.textview.checkhelper.helper.PhoneICodeHelper;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.holder.ViewHolder;

public class PhoneICodeActivity extends BaseAppCompatActivity
{
	private ViewHolder viewHolder;

	private PhoneICodeHelper phoneICodeHelper;

	public static void launcher(Context context)
	{
		if (null != context)
		{
			Intent intent = new Intent(context, PhoneICodeActivity.class);
			if (!(context instanceof Activity))
			{
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_identify_code);

		viewHolder = new ViewHolder(this);

		EditText etPhone = viewHolder.get(R.id.et_username);
		final TextView tvSendCode = viewHolder.get(R.id.tv_identify);
		CheckBox checkBox = viewHolder.get(R.id.checkBox_phone);

		phoneICodeHelper = new PhoneICodeHelper(etPhone, tvSendCode, checkBox.isChecked())
		{
			@Override
			protected int getIdentifyCodeMaxNumber()
			{
				return 10;
			}
		};
		phoneICodeHelper.setOnIdentifyCodeListener(new PhoneICodeHelper.OnIdentifyCodeListener()
		{
			@Override
			public void onIdentifyStateChange(TextView tvIdentify, boolean isLegal, boolean isCountDown, int restTime)
			{
				if (isCountDown)
				{
					if (restTime != -1)
					{
						tvSendCode.setTextColor(0xff757474);
						tvSendCode.setText(String.format("重新发送(%d)", restTime));
					}
				}
				else
				{
					if (isLegal)
					{
						tvSendCode.setTextColor(0xffff0000);
						tvSendCode.setText("发送验证码");
					}
					else
					{
						tvSendCode.setTextColor(0xff757474);
						tvSendCode.setText("发送验证码");
					}
				}
			}

			@Override
			public void onIdentifyClick(View view, boolean isMatch, boolean isCountDown)
			{
				if (isMatch && !isCountDown)
				{
					SDKManager.toast("手机号正确，协议勾选了, 短信发送成功");
				}
				else
				{
					SDKManager.toast("手机号错误 或者 协议未勾选 或者正在倒计时");
				}
			}
		});

		// 协议
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
			{
				phoneICodeHelper.setProtocolChecked(isChecked);
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		phoneICodeHelper.onDestroy();
	}
}
