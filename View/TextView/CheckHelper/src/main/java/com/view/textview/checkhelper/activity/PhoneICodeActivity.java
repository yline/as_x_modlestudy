package com.view.textview.checkhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.view.textview.checkhelper.R;
import com.view.textview.checkhelper.checker.CheckHelper;
import com.view.textview.checkhelper.checker.TextDecorateUtil;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.holder.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;

public class PhoneICodeActivity extends BaseAppCompatActivity {
	public static void launcher(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, PhoneICodeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private CheckHelper checkHelper;
	private CountDownHandler countDownHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_identify_code);
		
		ViewHolder viewHolder = new ViewHolder(this);
		
		EditText etPhone = viewHolder.get(R.id.et_username);
		final TextView tvSendCode = viewHolder.get(R.id.tv_identify);
		CheckBox checkBox = viewHolder.get(R.id.checkBox_phone);
		
		countDownHandler = new CountDownHandler(tvSendCode);
		
		checkHelper = new CheckHelper();
		checkHelper.addTextElement(etPhone, "phone", new CheckHelper.OnTextCheckCallback() {
			@Override
			public boolean isMatch(String text) {
				return TextDecorateUtil.isPhoneMatch(text);
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
				if (!countDownHandler.isCountDown()) {
					if (isAllMatch) {
						tvSendCode.setTextColor(0xffff0000);
						tvSendCode.setText("发送验证码");
					} else {
						tvSendCode.setTextColor(0xff757474);
						tvSendCode.setText("发送验证码");
					}
				}
			}
		});
		
		tvSendCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isAllMatch = checkHelper.isAllMatch();
				LogUtil.v("isAllMatch = " + isAllMatch);
				
				boolean isCountDown = countDownHandler.isCountDown();
				;
				LogUtil.v("isCountDown = " + isCountDown);
				
				boolean isMatch = isAllMatch && !isCountDown;
				if (isMatch) {
					countDownHandler.sendEmptyMessage(10);
					SDKManager.toast("手机号正确，协议勾选了, 短信发送成功");
				} else {
					SDKManager.toast("手机号错误 或者 协议未勾选 或者正在倒计时");
				}
			}
		});
	}
	
	private static class CountDownHandler extends Handler {
		private int countTime = -1;
		private WeakReference<TextView> sendTextReference;
		
		private CountDownHandler(TextView sendTextView) {
			sendTextReference = new WeakReference<>(sendTextView);
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			countTime = msg.what;
			if (countTime > 0) {
				countTime--;
				
				TextView sendTextView = sendTextReference.get();
				LogUtil.v("sendTextView = " + sendTextView);
				if (null != sendTextView) {
					sendTextView.setTextColor(0xff757474);
					sendTextView.setText(String.format(Locale.CHINA, "重新发送(%d)", countTime));
				}
				
				sendEmptyMessageDelayed(countTime, 1000);
			} else {
				TextView sendTextView = sendTextReference.get();
				if (null != sendTextView) {
					sendTextView.setTextColor(0xffff0000);
					sendTextView.setText("发送验证码");
				}
			}
		}
		
		public boolean isCountDown() {
			return (countTime > 0);
		}
	}
}
