package com.listview.eye.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 固定动画
 */
public class YProgressView extends ImageView
{
	private static final int MARGIN_OUTTER = 10;

	private static final int MARGIN_INNER = 10;

	private int backgroundColor = Color.WHITE;

	private int pointColor = Color.BLACK;

	private float backGroundProcess;

	private float circleProgress;

	private Paint mPaint;

	private Handler mHandler = new Handler();

	private boolean isAnimate;

	/** 外面的圆圈的RectF */
	private RectF outerRectF;

	/** 里面的圆圈的RectF */
	private RectF innerRectF;

	public YProgressView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setWillNotDraw(false);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		backGroundProcess = 0.0f;
		circleProgress = 0.0f;
		isAnimate = false;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		mPaint.setColor(backgroundColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);

		drawBackGroundOuter(canvas, backGroundProcess);

		drawBackGroundInner(canvas, backGroundProcess);

		if (isAnimate)
		{
			mPaint.setColor(pointColor);
			mPaint.setStrokeWidth(5);
			mPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
			mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
			canvas.drawArc(innerRectF, 270 + 360 * circleProgress, 10, false, mPaint);
			canvas.drawArc(outerRectF, 270 - 360 * circleProgress, 5, false, mPaint);
		}
	}

	/**
	 * 绘制外面的圆圈
	 * @param canvas
	 */
	private void drawBackGroundOuter(Canvas canvas, float progress)
	{
		outerRectF = new RectF(2, 2 + MARGIN_OUTTER, getWidth() - 2, getHeight() - 2 - MARGIN_OUTTER);
		canvas.drawArc(outerRectF, 270, 90 * progress, false, mPaint);
		canvas.drawArc(outerRectF, 270, -90 * progress, false, mPaint);
		canvas.drawArc(outerRectF, 90, -90 * progress, false, mPaint);
		canvas.drawArc(outerRectF, 90, 90 * progress, false, mPaint);
	}

	/**
	 * 绘制里面的圆圈
	 * @param canvas
	 * @param progress
	 */
	private void drawBackGroundInner(Canvas canvas, float progress)
	{
		int marginSize = getWidth() / 4;
		innerRectF =
				new RectF(marginSize, marginSize + MARGIN_INNER, getWidth() - marginSize, getHeight() - marginSize
						- MARGIN_INNER);
		canvas.drawArc(innerRectF, 270, 90 * progress, false, mPaint);
		canvas.drawArc(innerRectF, 270, -90 * progress, false, mPaint);
		canvas.drawArc(innerRectF, 90, -90 * progress, false, mPaint);
		canvas.drawArc(innerRectF, 90, 90 * progress, false, mPaint);
	}

	/**
	 * 设置背景当前进度
	 * @param progress 0 - 1
	 */
	public void setProgress(float progress)
	{
		this.backGroundProcess = progress;
		this.invalidate();
	}

	public void setBackGroundColor(int color)
	{
		this.backgroundColor = color;
	}

	public void setPointColor(int color)
	{
		this.pointColor = color;
	}

	/**
	 * 开启圆圈动画
	 */
	public void startAnimate()
	{
		if (!isAnimate)
		{
			isAnimate = true;
			mHandler.post(mRunnable);
		}
	}

	/**
	 * 结束圆圈动画
	 */
	public void stopAnimate()
	{
		isAnimate = false;
		mHandler.removeCallbacks(mRunnable);

		// 动画结束之后,干掉小圆圈
		YProgressView.this.invalidate();
	}

	private Runnable mRunnable = new Runnable()
	{

		@Override
		public void run()
		{
			circleProgress += 0.05;
			if (isAnimate)
			{
				mHandler.postDelayed(this, 50);
			}

			YProgressView.this.invalidate();
		}
	};

}
