package com.view.textview.checkhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.style.ClickableSpan;
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

/**
 * 手机号 + 密码
 *
 * @author yline 2017/7/18 -- 14:54
 * @version 1.0.0
 */
public class PhonePwdActivity extends BaseAppCompatActivity {
	private ViewHolder viewHolder;
	
	public static void launcher(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, PhonePwdActivity.class);
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
		setContentView(R.layout.activity_phone_pwd);
		
		viewHolder = new ViewHolder(this);
		
		EditText etPhone = viewHolder.get(R.id.et_username);
		EditText etPwd = viewHolder.get(R.id.et_password);
		final CheckBox checkBox = viewHolder.get(R.id.checkBox_phone);
		
		checkHelper = new CheckHelper();
		checkHelper.addTextElement(etPhone, "phone", new CheckHelper.OnTextCheckCallback() {
			@Override
			public boolean isMatch(String text) {
				return TextDecorateUtil.isPhoneMatch(text);
			}
		});
		checkHelper.addTextElement(etPwd, "pwd", new CheckHelper.OnTextCheckCallback() {
			@Override
			public boolean isMatch(String text) {
				return TextDecorateUtil.isPhonePwdMatch(text);
			}
		});
		checkHelper.addCheckBoxElement(checkBox, "protocol", true, new CheckHelper.OnCheckBoxCheckCallback() {
			@Override
			public boolean isMatch(boolean isChecked) {
				return isChecked;
			}
		});
		checkHelper.setOnCheckResultCallback(new CheckHelper.OnCheckResultCallback() {
			@Override
			public void onResult(HashMap<String, Boolean> arrayMap, boolean isAllMatch) {
				if (isAllMatch) {
					viewHolder.get(R.id.btn_login).setBackgroundColor(ContextCompat.getColor(PhonePwdActivity.this, android.R.color.holo_red_light));
				} else {
					viewHolder.get(R.id.btn_login).setBackgroundColor(ContextCompat.getColor(PhonePwdActivity.this, android.R.color.black));
				}
			}
		});
		
		// 协议 监听事件
		TextDecorateUtil.decorateProtocolTextView(checkBox, 7, 4, new ClickableSpan() {
			@Override
			public void onClick(View view) {
				SDKManager.toast("协议被点击了");
			}
		});
		
		// 登录 监听事件
		viewHolder.setOnClickListener(R.id.btn_login, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkHelper.isAllMatch()) {
					SDKManager.toast("手机号、密码、协议符合规则");
				} else {
					SDKManager.toast("Error");
				}
			}
		});
	}
}
