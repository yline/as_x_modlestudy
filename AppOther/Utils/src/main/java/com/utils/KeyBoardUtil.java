package com.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘
 */
public class KeyBoardUtil
{
    
    public KeyBoardUtil()
    {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 打开 软键盘
     * 
     * @param mContext  上下文
     * @param mEditText	 输入框
     */
    public static void openKeybord(Context mContext, EditText mEditText)
    {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    
    /**
     * 关闭软键盘
     * 
     * @param mContext  上下文
     * @param mEditText	输入框
     */
    public static void closeKeybord(Context mContext, EditText mEditText)
    {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
