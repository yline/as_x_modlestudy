package com.view.eventtouch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.yline.log.LogFileUtil;

/**
 * 最外层的View
 *
 * @author yline 2016/9/6 --> 22:43
 * @version 1.0.0
 */
public class LinearlayoutA extends LinearLayout
{
	private static final String TAG = "LinearlayoutA";

	public LinearlayoutA(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LogFileUtil.v(TAG, "LinearlayoutA created");
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		boolean result = super.dispatchTouchEvent(ev);
		ViewC.Log(TAG + "--> dispatchTouchEvent:", result, ev);
		return result;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		boolean result = super.onInterceptTouchEvent(ev);
		ViewC.Log(TAG + "--> onInterceptTouchEvent:", result, ev);
		return result;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = super.onTouchEvent(event);
		ViewC.Log(TAG + "--> onTouchEvent:", result, event);
		return result;
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		LogFileUtil.v(TAG, "LinearlayoutA onDetachedFromWindow");
	}
}
