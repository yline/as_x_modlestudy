package com.view.test.touchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.yline.utils.LogUtil;

public class B220 extends FrameLayout {
    public B220(Context context) {
        super(context);
    }
    
    public B220(Context context, AttributeSet attrs) {
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.v("");
        boolean result = super.onInterceptTouchEvent(ev);
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