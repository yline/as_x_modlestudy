package com.view.textview.checkhelper.checker;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class CheckHelper {
	private Random random;
	private HashMap<String, Boolean> hashMap = new HashMap<>();
	private OnCheckResultCallback checkResultCallback;
	
	public void addTextElement(TextView textView, final OnTextCheckCallback callback) {
		addTextElement(textView, genRandom(), false, callback);
	}
	
	public void addTextElement(TextView textView, final String tag, final OnTextCheckCallback callback) {
		addTextElement(textView, tag, false, callback);
	}
	
	public void addTextElement(TextView textView, final String tag, boolean defaultValue, final OnTextCheckCallback callback) {
		hashMap.put(tag, defaultValue);
		textView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				boolean isMatch = callback.isMatch(s.toString());
				
				Boolean value = hashMap.get(tag);
				boolean oldMatch = (null != value) && value;
				if (isMatch != oldMatch) {
					hashMap.put(tag, isMatch);
					if (null != checkResultCallback) {
						checkResultCallback.onResult(hashMap, isAllMatch(hashMap));
					}
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
			}
		});
	}
	
	public void addCheckBoxElement(CheckBox checkBox, final String tag, boolean defaultValue, final OnCheckBoxCheckCallback callback) {
		hashMap.put(tag, defaultValue);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean isMatch = callback.isMatch(isChecked);
				
				Boolean value = hashMap.get(tag);
				boolean oldMatch = (null != value) && value;
				if (isMatch != oldMatch) {
					hashMap.put(tag, isMatch);
					if (null != checkResultCallback) {
						checkResultCallback.onResult(hashMap, isAllMatch(hashMap));
					}
				}
			}
		});
	}
	
	public void setOnCheckResultCallback(OnCheckResultCallback callback) {
		this.checkResultCallback = callback;
	}
	
	public interface OnTextCheckCallback {
		/**
		 * @param text 输入的内容
		 * @return 是否符合要求
		 */
		boolean isMatch(String text);
	}
	
	public interface OnCheckBoxCheckCallback {
		/**
		 * @param isChecked 新的状态
		 * @return 是否满足
		 */
		boolean isMatch(boolean isChecked);
	}
	
	
	public interface OnCheckResultCallback {
		/**
		 * @param arrayMap   当前的符合判断
		 * @param isAllMatch 加入的元素是否全部符合
		 */
		void onResult(HashMap<String, Boolean> arrayMap, boolean isAllMatch);
	}
	
	private String genRandom() {
		if (null == random) {
			random = new Random();
		}
		return System.currentTimeMillis() + "-" + random.nextInt(Integer.MAX_VALUE);
	}
	
	public boolean isAllMatch() {
		return isAllMatch(hashMap);
	}
	
	public boolean isAllMatch(HashMap<String, Boolean> arrayMap) {
		for (Boolean bool : arrayMap.values()) {
			if (!bool) {
				return false;
			}
		}
		return true;
	}
}
