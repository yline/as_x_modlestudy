package com.view.theory.gesture.helper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.yline.log.LogFileUtil;

/**
 * * 按一下：
 * 轻触 瞬间的事	  ：		onDown 	onSingleTapUp 	onSingleTapConfirmed(2)
 * 触碰 0.5s - 1s ：     	onDown 	onShowPress 	onSingleTapUp 			onSingleTapConfirmed(2)
 * 长按 1s 以上	  ：		onDown	onShowPress		onLongPress
 * <p/>
 * 拖动：
 * 快速滑屏	 	  ：		onDown	onScroll	onScroll	...		onScroll	onFling
 * 按下停顿一下再滑：		onDown	onShowPress	onScroll    ...		onScroll	结束（有指尖滑动离开的感觉，则执行onFling）
 * <p/>
 * 按两下：
 * 快速两下       ：		onDown	onSingleTapup	onDoubleTap(2)	OnDoubleTapEvent(2)	onDown	OnDoubleTapEvent(2)
 * 缓慢两下	  ：		onDown	onSingleTapup	onSingleTapConfirmed(2)	onDown onSingleTapup onSingleTapConfirmed(2)
 * @author yline 2016/10/6 --> 23:14
 * @version 1.0.0
 */
public class GestureHelper
{
	private static final String TAG = "GestureHelper";

	private GestureDetector gestureDetector;

	public void testGesture(Context context, View view)
	{
		GestureDetector.OnGestureListener listener = new YGestureListener();
		gestureDetector = new GestureDetector(context, listener);

		GestureDetector.OnDoubleTapListener listener2 = new YDoubleGestureListener();
		gestureDetector.setOnDoubleTapListener(listener2);

		view.setFocusable(true);
		view.setClickable(true);
		view.setSelected(true);
		view.setLongClickable(true);

		view.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	public void testSimpleGesture(Context context, View view)
	{
		YSimpleGestureListener listener = new YSimpleGestureListener();
		gestureDetector = new GestureDetector(context, listener);
		view.setFocusable(true);
		view.setClickable(true);
		view.setSelected(true);
		view.setLongClickable(true);
		
		view.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	/**
	 * 手势监听动作
	 */
	private class YGestureListener implements GestureDetector.OnGestureListener
	{
		/**
		 * 用户按下屏幕就会触发
		 */
		@Override
		public boolean onDown(MotionEvent e)
		{
			LogFileUtil.v(TAG, "1 onDown");
			return false;
		}

		/**
		 * 如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，那么onShowPress就会执行
		 * 瞬间多长？
		 */
		@Override
		public void onShowPress(MotionEvent e)
		{
			LogFileUtil.v(TAG, "1 onShowPress");
		}

		/**
		 * 一次单独的轻击抬起操作,也就是轻击一下屏幕，立刻抬起来，才会有这个触发，
		 * 当然,如果除了Down以外还有其它操作,那就不再算是Single操作了,所以也就不会触发这个事件
		 * 触发顺序：
		 * 点击一下非常快的（不滑动）Touchup：
		 * onDown->onSingleTapUp->onSingleTapConfirmed
		 * 点击一下稍微慢点的（不滑动）Touchup：
		 * onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
		 */
		@Override
		public boolean onSingleTapUp(MotionEvent e)
		{
			LogFileUtil.v(TAG, "1 onSingleTapUp");
			return false;    // 仅执行一次，返回true
		}

		/**
		 * 在屏幕上拖动事件。无论是用手拖动view，或者是以抛的动作滚动，都会多次触发,这个方法
		 * 滑屏：手指触动屏幕后，稍微滑动后立即松开
		 * onDown-----》onScroll----》onScroll----》onScroll----》………----->onFling
		 * 拖动:onDown------》onScroll----》onScroll------》onFiling
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			LogFileUtil.v(TAG, "1 onScroll");
			return false;
		}

		/**
		 * 长按触摸屏，超过一定时长，就会触发这个事件
		 * 触发顺序：onDown->onShowPress->onLongPress
		 */
		@Override
		public void onLongPress(MotionEvent e)
		{
			LogFileUtil.v(TAG, "1 onLongPress");
		}

		/**
		 * 滑屏，用户按下触摸屏、快速移动后松开，
		 * 由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
		 * e1：第1个ACTION_DOWN MotionEvent
		 * e2：最后一个ACTION_MOVE MotionEvent
		 * velocityX：X轴上的移动速度，像素/秒
		 * velocityY：Y轴上的移动速度，像素/秒
		 */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			LogFileUtil.v(TAG, "1 onFling");
			return false;
		}
	}

	/**
	 * 双击
	 */
	private class YDoubleGestureListener implements GestureDetector.OnDoubleTapListener
	{
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e)
		{
			LogFileUtil.v(TAG, "2 onSingleTapConfirmed");
			return false;  // true if the event is consumed, else false
		}

		@Override
		public boolean onDoubleTap(MotionEvent e)
		{
			LogFileUtil.v(TAG, "2 onDoubleTap");
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e)
		{
			LogFileUtil.v(TAG, "2 onDoubleTapEvent");
			return false;
		}
	}

	/**
	 * SimpleOnGestureListener类本身已经实现了 上两个接口的所有函数，只是里面全是空的
	 */
	private class YSimpleGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		// 就是上面三个的所有
	}
}
