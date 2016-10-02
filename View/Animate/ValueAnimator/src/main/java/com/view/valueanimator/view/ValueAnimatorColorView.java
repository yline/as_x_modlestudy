package com.view.valueanimator.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yline on 2016/10/2.
 */
public class ValueAnimatorColorView extends View
{
	public static final float RADIUS = 50f;

	private PaintPointBean currentPoint;

	private Paint mPaint;

	private String color;

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
		mPaint.setColor(Color.parseColor(color));
		invalidate();
	}

	public ValueAnimatorColorView(Context context, AttributeSet attrs)
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
			currentPoint = new PaintPointBean(RADIUS, RADIUS);
			drawCircle(canvas);
			startAnimation();
		}
		else
		{
			drawCircle(canvas);
		}
	}

	private void drawCircle(Canvas canvas)
	{
		float x = currentPoint.getX();
		float y = currentPoint.getY();
		canvas.drawCircle(x, y, RADIUS, mPaint);
	}

	private void startAnimation()
	{
		PaintPointBean startPoint = new PaintPointBean(RADIUS, RADIUS);
		PaintPointBean endPoint = new PaintPointBean(getWidth() - RADIUS, getHeight() - RADIUS);

		ValueAnimator anim = ValueAnimator.ofObject(new PaintColorEvaluatorB(), startPoint, endPoint);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{

			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				currentPoint = (PaintPointBean) animation.getAnimatedValue();
				invalidate();  // 重绘
			}
		});
		ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new PaintColorEvaluatorA(), "#0000FF", "#FF0000");

		AnimatorSet animSet = new AnimatorSet();
		animSet.play(anim).with(anim2);
		animSet.setDuration(5000);
		animSet.start();
	}

	public class PaintPointBean
	{
		private float x;

		private float y;

		public PaintPointBean(float x, float y)
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
	 * #GRB
	 * blog : http://blog.csdn.net/guolin_blog/article/details/43816093
	 */
	@SuppressWarnings("rawtypes")
	public class PaintColorEvaluatorA implements TypeEvaluator
	{
		private int mCurrentRed = -1;

		private int mCurrentGreen = -1;

		private int mCurrentBlue = -1;

		@Override
		public Object evaluate(float fraction, Object startValue, Object endValue)
		{
			String startColor = (String) startValue;
			String endColor = (String) endValue;
			int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
			int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
			int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
			int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
			int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
			int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);
			mCurrentRed = (int) (startRed + fraction * (endRed - startRed));
			mCurrentGreen = (int) (startGreen + fraction * (endGreen - startGreen));
			mCurrentBlue = (int) (startBlue + fraction * (endBlue - startBlue));
			String currentColor = "#" + getHexString(mCurrentRed) + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
			return currentColor;
		}

		private String getHexString(int value)
		{
			String hexString = Integer.toHexString(value);
			if (hexString.length() == 1)
			{
				hexString = "0" + hexString;
			}
			return hexString;
		}
	}

	@SuppressWarnings("rawtypes")
	public class PaintColorEvaluatorB implements TypeEvaluator
	{
		@Override
		public Object evaluate(float fraction, Object startValue, Object endValue)
		{
			PaintPointBean startPoint = (PaintPointBean) startValue;
			PaintPointBean endPoint = (PaintPointBean) endValue;
			float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
			float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
			PaintPointBean point = new PaintPointBean(x, y);
			return point;
		}
	}
}
