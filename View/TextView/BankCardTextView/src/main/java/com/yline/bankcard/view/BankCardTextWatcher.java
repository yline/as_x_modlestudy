package com.yline.bankcard.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 实现，输入银行卡号，间隔四位，空格一个
 *
 * @author linjiang@kjtpay.com
 * @times 2018/8/6 -- 10:29
 */
public class BankCardTextWatcher implements TextWatcher {
	private static final char SPACE = ' ';
	
	private EditText mEditText;
	private int selection; // 光标位置
	
	public BankCardTextWatcher(EditText editText) {
		super();
		this.mEditText = editText;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (count < after) {
			selection = start + after;
		} else if (count > after) {
			selection = start;
		} else {
			selection = start + count;
		}
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int length = s.length(); // 输入字符的长度
		if (length > 0) {
			String textString = s.toString();
			
			StringBuilder stringBuilder = new StringBuilder();
			char tempChar; // 当前字符
			int tempLength; // 已经累加的字符的长度
			
			// 修改输入内容，并修正光标位置
			int selectionDiff = 0; // 光标位置修改
			for (int i = 0; i < length; i++) {
				tempChar = textString.charAt(i);
				tempLength = stringBuilder.length();
				
				if (tempChar != SPACE) {
					if (tempLength % 5 == 4) {
						stringBuilder.append(SPACE);
						// 光标位置
						if (selection > i) {
							selectionDiff++;
						}
					}
					stringBuilder.append(tempChar);
				} else {
					// 光标位置
					if (selection > i) {
						selectionDiff--;
					}
				}
			}
			
			String stringWithSpace = stringBuilder.toString();
			
			// 更新用户看到的信息
			mEditText.removeTextChangedListener(this);
			mEditText.setText(stringWithSpace);
			mEditText.setSelection(Math.min(selection + selectionDiff, stringBuilder.length())); // 光标，防止异常情况崩溃
			mEditText.addTextChangedListener(this);
		}
	}
	
	@Override
	public void afterTextChanged(Editable s) {
	}
	
	/**
	 * 因为空格，所以提供一个删除空格的函数
	 *
	 * @param stringWithSpace 输入字符串
	 * @return 已删除空格的字符串
	 */
	public static String clearSpace(String stringWithSpace) {
		return stringWithSpace.replace(String.valueOf(SPACE), "");
	}
}
