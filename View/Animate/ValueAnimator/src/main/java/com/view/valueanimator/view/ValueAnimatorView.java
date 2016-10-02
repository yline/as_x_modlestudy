package com.view.valueanimator.view;

import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/10/2.
 */
public class ValueAnimatorView extends View
{
	private static final String TAG = "ValueAnimatorView";

	private static final float RADIUS = 50f;

	private ValueAnimatorBean currentPoint;

	private Paint mPaint;

	public ValueAnimatorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLUE);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (null == currentPoint)
		{
			// 起点
			currentPoint = new ValueAnimatorBean(RADIUS, RADIUS);
			drawCircle(canvas);
			startAnimation();
			LogFileUtil.v(TAG, "onDraw == null + " + currentPoint);
		}
		else
		{
			drawCircle(canvas);
			LogFileUtil.v(TAG, "onDraw + " + currentPoint);
		}
	}

	private void drawCircle(Canvas canvas)
	{
		float x = currentPoint.getX();
		float y = currentPoint.getY();
		canvas.drawCircle(x, y, RADIUS, mPaint);
	}

	/**
	 * 确定边界值，并随时改变currentPoint;从而引起重绘
	 */
	private void startAnimation()
	{
		ValueAnimatorBean startPoint = new ValueAnimatorBean(getWidth() / 2, RADIUS);
		ValueAnimatorBean endPoint = new ValueAnimatorBean(getWidth() / 2, getHeight() - RADIUS);
		ValueAnimator animator = ValueAnimator.ofObject(new AnimatorValueOnPaintPointEvaluator(), startPoint, endPoint);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				currentPoint = (ValueAnimatorBean) animation.getAnimatedValue();
				invalidate();    // 更新当前点
			}
		});
		// anim.setInterpolator(new AccelerateInterpolator(2f));  // 加速
		// anim.setInterpolator(new BounceInterpolator());  // 自由落体运动
		animator.setInterpolator(new DecelerateAccelerateInterpolator());  // 自定义
		animator.setDuration(5000);
		animator.start();
	}

	/** 控制运动速度 */
	private class DecelerateAccelerateInterpolator implements TimeInterpolator
	{
		@Override
		public float getInterpolation(float input)
		{
			float result;
			if (input <= 0.5)
			{
				result = (float) (Math.sin(Math.PI * input)) / 2;
			}
			else
			{
				result = (float) (2 - Math.sin(Math.PI * input)) / 2;
			}
			return result;
		}
	}

	public class ValueAnimatorBean
	{
		private float x;

		private float y;

		public ValueAnimatorBean(float x, float y)
		{
			this.x = x;
			this.y = y;
		}

		public float getX()
		{
			return x;
		}

		public void setX(float x)
		{
			this.x = x;
		}

		public float getY()
		{
			return y;
		}

		public void setY(float y)
		{
			this.y = y;
		}
	}

	/**
	 * 对改变速度的加速度进行控制(函数控制)
	 */
	private class AnimatorValueOnPaintPointEvaluator implements TypeEvaluator
	{
		@Override
		public Object evaluate(float fraction, Object startValue, Object endValue)
		{
			ValueAnimatorBean startPoint = (ValueAnimatorBean) startValue;
			ValueAnimatorBean endPoint = (ValueAnimatorBean) endValue;
			float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
			float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
			ValueAnimatorBean point = new ValueAnimatorBean(x, y);
			return point;
		}
	}
}
