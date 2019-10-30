package com.view.test.touchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yline.utils.LogUtil;

public class C111 extends View {
    public C111(Context context) {
        super(context);
    }
    
    public C111(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.v("");
        boolean result = super.dispatchTouchEvent(event);
        LogUtil.v("result = " + result);
        return result;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.v("");
        boolean result = super.onTouchEvent(event);
        LogUtil.v("result = " + result);
        return result;
    }
}
