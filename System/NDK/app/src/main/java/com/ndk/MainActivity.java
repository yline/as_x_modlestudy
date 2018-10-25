package com.ndk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ndk.jni.JniManager;
import com.ndk.jni.JniProvider;
import com.yline.test.BaseTestActivity;

/**
 * @author yline 2018/4/24 -- 10:16
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {
	private JniManager mJniManager;
	private JniProvider mJniProvider;
	
	private String mEncodedStr;
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		mJniManager = new JniManager();
		mJniProvider = new JniProvider();
		
		/* ------------------------------------ Java调用C, 测试 ------------------------------------ */
		final TextView libTextView = addTextView("");
		addButton("stringFromJNI", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = mJniManager.stringFromJNI();
				libTextView.setText(str);
			}
		});
		
		final TextView logTextView = addTextView("");
		addButton("logByJni", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String logStr = mJniManager.logByJni("Android->JNI->Log");
				logTextView.setText(logStr);
			}
		});
		
		/* ------------------------------------ C调用Java, 测试 ------------------------------------ */
		addButton("doProvider", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mJniProvider.doProvider();
			}
		});
		
		addButton("doStaticProvider", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mJniProvider.doStaticProvider();
			}
		});
		
		/* ------------------------------------ Java调用C, 加密解密 ------------------------------------ */
		final EditText encodeEditText = addEditText("", "yline&乾天");
		final TextView encodeTextView = addTextView("");
		addButton("encode", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String inputStr = encodeEditText.getText().toString().trim();
				
				mEncodedStr = mJniManager.encode(inputStr, 0);
				encodeTextView.setText(mEncodedStr);
			}
		});
		
		final TextView decodeTextView = addTextView("");
		addButton("decode", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mEncodedStr) {
					decodeTextView.setText(mJniManager.decode(mEncodedStr, 0));
				}
			}
		});
	}
}
