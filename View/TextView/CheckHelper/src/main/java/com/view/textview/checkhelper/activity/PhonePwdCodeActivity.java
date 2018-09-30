package com.view.textview.checkhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.view.textview.checkhelper.R;
import com.view.textview.checkhelper.checker.CheckHelper;
import com.view.textview.checkhelper.checker.TextDecorateUtil;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.view.recycler.holder.ViewHolder;

import java.util.HashMap;

public class PhonePwdCodeActivity extends BaseAppCompatActivity {
	private ViewHolder viewHolder;
	
	public static void launcher(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, PhonePwdCodeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private CheckHelper checkHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_pwd_code);
		
		viewHolder = new ViewHolder(this);
		
		EditText etPhone = viewHolder.get(R.id.et_username);
		EditText etCode = viewHolder.get(R.id.et_code);
		EditText etPwd = viewHolder.get(R.id.et_password);
		
		CheckBox checkBox = viewHolder.get(R.id.checkBox_phone);
		
		checkHelper = new CheckHelper();
		checkHelper.addTextElement(etPhone, "phone", new CheckHelper.OnTextCheckCallback() {
			@Override
			public boolean isMatch(String text) {
				return TextDecorateUtil.isPhoneMatch(text);
			}
		});
		checkHelper.addTextElement(etCode, "code", new CheckHelper.OnTextCheckCallback() {
			@Override
			public boolean isMatch(String text) {
				return TextDecorateUtil.isIdentifyCodeMatch6(text);
			}
		});
		checkHelper.addTextElement(etPwd, "pwd", new CheckHelper.OnTextCheckCallback() {
			@Override
			public boolean isMatch(String text) {
				return TextDecorateUtil.isPhonePwdMatch(text);
			}
		});
		checkHelper.addCheckBoxElement(checkBox, "checkbox", true, new CheckHelper.OnCheckBoxCheckCallback() {
			@Override
			public boolean isMatch(boolean isChecked) {
				return isChecked;
			}
		});
		checkHelper.setOnCheckResultCallback(new CheckHelper.OnCheckResultCallback() {
			@Override
			public void onResult(HashMap<String, Boolean> arrayMap, boolean isAllMatch) {
				if (isAllMatch) {
					viewHolder.get(R.id.btn_login).setBackgroundColor(ContextCompat.getColor(PhonePwdCodeActivity.this, android.R.color.holo_red_light));
				} else {
					viewHolder.get(R.id.btn_login).setBackgroundColor(ContextCompat.getColor(PhonePwdCodeActivity.this, android.R.color.black));
				}
			}
		});
		
		// 登录
		viewHolder.setOnClickListener(R.id.btn_login, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkHelper.isAllMatch()) {
					SDKManager.toast("手机号、验证码、密码、协议符合规则");
				} else {
					SDKManager.toast("Error");
				}
			}
		});
	}
}
