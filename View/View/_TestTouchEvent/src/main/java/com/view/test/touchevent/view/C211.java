package com.view.test.touchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yline.utils.LogUtil;

public class C211 extends View {
    public C211(Context context) {
        super(context);
    }
    
    public C211(Context context, AttributeSet attrs) {
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
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.v("" + ", action = " + event.getAction());
        boolean result = super.onTouchEvent(event);
        LogUtil.v("result = " + result + ", action = " + event.getAction());
        return result;
    }
}
