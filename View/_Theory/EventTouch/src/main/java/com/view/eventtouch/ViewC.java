package com.view.eventtouch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yline.log.LogFileUtil;

/**
 * 默认情况下的执行顺序:
 * LinearlayoutA--> onInterceptTouchEvent: ->  result = false,action = ACTION_DOWN
 * LinearLayoutB--> onInterceptTouchEvent: ->  result = false,action = ACTION_DOWN
 * ViewC--> onTouchEvent: ->  result = false,action = ACTION_DOWN
 * ViewC--> dispatchTouchEvent: ->  result = false,action = ACTION_DOWN
 * LinearLayoutB--> onTouchEvent: ->  result = false,action = ACTION_DOWN
 * LinearLayoutB--> dispatchTouchEvent: ->  result = false,action = ACTION_DOWN
 * LinearlayoutA--> onTouchEvent: ->  result = false,action = ACTION_DOWN
 * LinearlayoutA--> dispatchTouchEvent: ->  result = false,action = ACTION_DOWN
 * <p/>
 * 返回值:(初始情况,LinearLayout全为false)
 * true:拦截,不继续流程;或处理了,不用审核了
 * false:不拦截,继续流程;或未处理,交给上级处理
 * <p/>
 * onInterceptTouchEvent:父类传给子类事件时(if true,子类就不会接到触发事件)
 * onTouchEvent:子类处理完事件,传给父类时(if true,父类就不会接收到子类的处理结果)
 * <p/>
 * dispatchTouchEvent:事件分发时的操作,平常使用的几率并不会太大
 */
public class ViewC extends View
{
	private static final String TAG = "ViewC";

	public ViewC(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LogFileUtil.v(TAG, "ViewC created");
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		boolean result = super.dispatchTouchEvent(ev);
		Log(TAG + "--> dispatchTouchEvent:", result, ev);
		return result;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = super.onTouchEvent(event);
		Log(TAG + "--> onTouchEvent:", result, event);
		return result;
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		LogFileUtil.v(TAG, "ViewC onDetachedFromWindow");
	}

	/**
	 * 打印日志
	 *
	 * @param result
	 * @param event
	 */
	public static void Log(String tag, boolean result, MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				LogFileUtil.v(tag, " result = " + result + ",action = " + "ACTION_DOWN");
				break;
			case MotionEvent.ACTION_UP:
				LogFileUtil.v(tag, " result = " + result + ",action = " + "ACTION_UP");
				break;
			default:
				LogFileUtil.v(tag, " result = " + result + ",action = " + event.getAction());
				break;
		}

	}
}
