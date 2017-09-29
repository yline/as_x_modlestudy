package com.view.textview.checkhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.view.textview.checkhelper.R;
import com.view.textview.checkhelper.helper.PhonePwdCodeHelper;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.view.recycler.holder.ViewHolder;

public class PhonePwdCodeActivity extends BaseAppCompatActivity
{
	private ViewHolder viewHolder;

	public static void launcher(Context context)
	{
		if (null != context)
		{
			Intent intent = new Intent(context, PhonePwdCodeActivity.class);
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
		setContentView(R.layout.activity_phone_pwd_code);

		viewHolder = new ViewHolder(this);

		EditText etPhone = viewHolder.get(R.id.et_username);
		EditText etCode = viewHolder.get(R.id.et_code);
		EditText etPwd = viewHolder.get(R.id.et_password);

		CheckBox checkBox = viewHolder.get(R.id.checkBox_phone);

		final PhonePwdCodeHelper phonePwdCodeHelper = new PhonePwdCodeHelper(etPhone, etCode, etPwd, checkBox.isChecked());
		phonePwdCodeHelper.setOnCheckResultListener(new PhonePwdCodeHelper.OnCheckResultListener()
		{
			@Override
			public void onChecked(boolean isMatch)
			{
				if (isMatch)
				{
					viewHolder.get(R.id.btn_login).setBackgroundColor(ContextCompat.getColor(PhonePwdCodeActivity.this, android.R.color.holo_red_light));
				}
				else
				{
					viewHolder.get(R.id.btn_login).setBackgroundColor(ContextCompat.getColor(PhonePwdCodeActivity.this, android.R.color.black));
				}
			}
		});
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
			{
				phonePwdCodeHelper.setProtocolChecked(isChecked);
			}
		});

		// 登录
		viewHolder.setOnClickListener(R.id.btn_login, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				LogFileUtil.v("onClick = " + phonePwdCodeHelper.isPhoneMatch() + phonePwdCodeHelper.isIdentifyCodeMatch() + phonePwdCodeHelper.isPwdMatch() + phonePwdCodeHelper.isProtocolChecked());
				if (phonePwdCodeHelper.isResultMatch())
				{
					SDKManager.toast("手机号、验证码、密码、协议符合规则");
				}
				else
				{
					SDKManager.toast("Error");
				}
			}
		});
	}
}
