package com.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘
 */
public class KeyBoardUtil {

    public KeyBoardUtil() {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 进入时,隐藏软键盘
     *
     * @param activity
     */
    public static void hideKeybord(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 打开 软键盘
     *
     * @param context   上下文
     * @param mEditText 输入框
     */
    public static void openKeybord(Context context, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param context   上下文
     * @param mEditText 输入框
     */
    public static void closeKeybord(Context context, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
