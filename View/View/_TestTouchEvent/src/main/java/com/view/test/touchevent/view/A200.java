package com.view.test.touchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.yline.utils.LogUtil;

public class A200 extends FrameLayout {
    public A200(Context context) {
        super(context);
    }
    
    public A200(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.v("" + ", action = " + event.getAction());
        boolean result = super.dispatchTouchEvent(event);
        LogUtil.v("result = " + result + ", action = " + event.getAction());
        return result;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.v("" + ", action = " + ev.getAction());
        boolean result = super.onInterceptTouchEvent(ev);
        LogUtil.v("result = " + result + ", action = " + ev.getAction());
        return result;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.v("" + ", action = " + event.getAction());
        boolean result = super.onTouchEvent(event);
        LogUtil.v("result = " + result + ", action = " + event.getAction());
        return result;
    }
}