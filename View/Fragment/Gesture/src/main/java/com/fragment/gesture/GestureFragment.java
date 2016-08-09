package com.fragment.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fragment.gesture.activity.MainApplication;
import com.view.fragment.gesture.R;
import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;

/**
 * 手势判断
 *
 * @author YLine 2016/8/9 --> 19:40
 * @version 1.0.0
 */
public class GestureFragment extends BaseFragment
{
	/** 水平手势,水平方向判断有效距离 dp */
	private static final int MIN_HG_HRANGE = 80;

	/** 水平手势,垂直方向判断无效距离 dp */
	private static final int MAX_HG_VRANGE = (int) (MIN_HG_HRANGE / 1.73f);

	/** 垂直手势,垂直方向判断有效距离 dp */
	private static final int MIN_VG_VRANGE = 80;

	/** 垂直手势,水平方向判断无效距离 dp */
	private static final int MAX_VG_HRANGE = (int) (MIN_VG_VRANGE / 1.73f);

	private CHolder cHolder;

	private boolean isListening = true;

	public void setListening(boolean listenable)
	{
		this.isListening = listenable;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_gesture, container, false);
	}

	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		cHolder = new CHolder(outMetrics.widthPixels, outMetrics.heightPixels);

		final GestureDetector gestureDetector = new GestureDetector(getActivity(), new gestureListener());

		view.setClickable(true);
		view.setOnTouchListener(new OnTouchListener()
		{

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	;

	private class gestureListener extends GestureDetector.SimpleOnGestureListener
	{

		@Override
		public boolean onDoubleTap(MotionEvent e)
		{ // 双击
			if (getActivity() instanceof onGestureJugdeCallback && isListening)
			{
				((onGestureJugdeCallback) getActivity()).onGestureResult(GestureJudge.DOUBLE_CLICK);
				LogFileUtil.v(MainApplication.TAG, "result = DOUBLE_CLICK");
			}
			return super.onDoubleTap(e);
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
			float startX = e1.getX();
			float startY = e1.getY();
			float endX = e2.getX();
			float endY = e2.getY();

			LogFileUtil.v(MainApplication.TAG, "startX = " + startX + " ,endX = " + endX + ",dX = " + (endX - startX));
			LogFileUtil.v(MainApplication.TAG, "startY = " + startY + " ,endY = " + endY + ",dY = " + (endY - startY));

			if (getActivity() instanceof onGestureJugdeCallback && isListening)
			{
				GestureJudge result = judgeTypeOfGesture(startX, startY, endX, endY);
				LogFileUtil.v(MainApplication.TAG, "result = " + result);
				((onGestureJugdeCallback) getActivity()).onGestureResult(result);
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	private class CHolder
	{
		private int viewWidth, viewHeight;

		/** 水平手势,水平方向判断有效距离 px */
		public int minHgHRange;

		/** 水平手势,垂直方向判断无效距离 px */
		public int maxHgVRange;

		/** 垂直手势,垂直方向判断有效距离 px */
		public int minVgVRange;

		/** 垂直手势,水平方向判断无效距离 px */
		public int maxVgHRange;

		public CHolder(int width, int height)
		{
			this.viewWidth = width;
			this.viewHeight = height;

			LogFileUtil.v(MainApplication.TAG, "width = " + viewWidth + ",height = " + viewHeight);

			minHgHRange = Math.min(width / 4, dp2px(getActivity(), MIN_HG_HRANGE));
			maxHgVRange = Math.max(width / 4, dp2px(getActivity(), MAX_HG_VRANGE));
			minVgVRange = Math.min(height / 4, dp2px(getActivity(), MIN_VG_VRANGE));
			maxVgHRange = Math.max(height / 4, dp2px(getActivity(), MAX_VG_HRANGE));

			LogFileUtil.v(MainApplication.TAG, "minHgHRange = " + minHgHRange + ",maxHgVRange = " + maxHgVRange);
			LogFileUtil.v(MainApplication.TAG, "minVgVRange = " + minVgVRange + ",maxVgHRange = " + maxVgHRange);
		}

		// 垂直手势,左右区分
		public boolean isRight(float centerX)
		{
			return centerX > viewWidth / 2 ? true : false;
		}

        /*
        // 水平手势,上下区分
        public boolean isUp(float centerY)
        {
            return centerY > viewHeight / 2 ? true : false;
        }
        */

		private int dp2px(Context context, float dpValue)
		{
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
		}
	}

	// 手势判断类
	private GestureJudge judgeTypeOfGesture(float startX, float startY, float endX, float endY)
	{
		// 水平手势
		if (Math.abs(endX - startX) > cHolder.minHgHRange && Math.abs(endY - startY) < cHolder.maxHgVRange)
		{
			if (endX - startX > cHolder.minHgHRange)
			{
				return GestureJudge.HG_RIGHT;
			}
			else if (startX - endX > cHolder.minHgHRange)
			{
				return GestureJudge.HG_LEFT;
			}
		}

		// 垂直手势
		if (Math.abs(endY - startY) > cHolder.minVgVRange && Math.abs(endX - startX) < cHolder.maxVgHRange)
		{
			if (endY - startY > cHolder.minVgVRange)
			{ // 上下区分
				return cHolder.isRight((startX + endX) / 2) ? GestureJudge.VG_RIGHT_DOWN : GestureJudge.VG_LEFT_DOWN;
			}
			else if (startY - endY > cHolder.minVgVRange)
			{
				return cHolder.isRight((startX + endX) / 2) ? GestureJudge.VG_RIGHT_UP : GestureJudge.VG_LEFT_UP;
			}
		}

		return GestureJudge.NONE; // 不在手势范围内
	}

	// 手势种类
	public enum GestureJudge
	{
		/** 不属于定下的手势 */
		NONE,

		/** 水平左滑 */
		HG_LEFT,
		/** 水平右滑 */
		HG_RIGHT,

		/** 垂直左方上滑 */
		VG_LEFT_UP,
		/** 垂直左方下滑 */
		VG_LEFT_DOWN,

		/** 垂直右方上滑 */
		VG_RIGHT_UP,
		/** 垂直右方下滑 */
		VG_RIGHT_DOWN,

		/** 双击 */
		DOUBLE_CLICK;
	}

	public interface onGestureJugdeCallback
	{
		void onGestureResult(GestureJudge result);
	}

}
