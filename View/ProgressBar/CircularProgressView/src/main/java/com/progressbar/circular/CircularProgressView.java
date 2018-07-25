package com.progressbar.circular;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 圆形加载动画
 * @author yline
 * @times 2018/7/25 -- 16:44
 */
public class CircularProgressView extends View {
	/* 初始化的常量 */
	private final int angleAnimatorDuration;
	
	private final int sweepAnimatorDuration;
	
	private final int minSweepAngle;
	
	private final float mBorderWidth;
	
	private final int[] mColors;
	
	/* 变量 */
	private final Paint mPaint;
	
	private ObjectAnimator mAngleAnimator, mSweepAnimator;
	
	private float mCurrentGlobalAngleOffset;
	
	private float mCurrentGlobalAngle, mCurrentSweepAngle; // 当前角度，和扫过的角度
	
	private int mCurrentColorIndex, mNextColorIndex; // 当前颜色，和下一个颜色
	
	private boolean mRunning;
	
	private boolean mModeAppearing = true;
	
	private final RectF fBounds = new RectF();
	
	public CircularProgressView(Context context) {
		this(context, null);
	}
	
	public CircularProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView, defStyleAttr, 0);
		
		mBorderWidth = a.getDimension(R.styleable.CircularProgressView_borderWidth, dp2px(context, 4)); // 线条宽度
		angleAnimatorDuration = a.getInt(R.styleable.CircularProgressView_angleAnimationDurationMillis, 2000); // 角度动画时间间隔
		sweepAnimatorDuration = a.getInt(R.styleable.CircularProgressView_sweepAnimationDurationMillis, 900); // 扫描动画时间间隔
		minSweepAngle = a.getInt(R.styleable.CircularProgressView_minSweepAngle, 30); // 最小角度
		
		int colorArrayId = a.getResourceId(R.styleable.CircularProgressView_colorSequence, R.array.circular_default_color_sequence);
		if (isInEditMode()) {
			mColors = new int[]{getResources().getColor(android.R.color.holo_red_light), getResources().getColor(android.R.color.holo_green_light),
					getResources().getColor(android.R.color.holo_blue_light)};
		} else {
			mColors = getResources().getIntArray(colorArrayId);
		}
		a.recycle();
		
		mCurrentColorIndex = 0; // 当前颜色
		mNextColorIndex = 1; // 下一个颜色
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setColor(mColors[mCurrentColorIndex]);
		
		setupAngleAnimation();
		setupSweepAnimator();
	}
	
	@Override
	protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == VISIBLE) {
			innerStart();
		} else {
			innerStop();
		}
	}
	
	@Override
	protected void onAttachedToWindow() {
		innerStart();
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		innerStop();
		super.onDetachedFromWindow();
	}
	
	private void innerStart() {
		if (isRunning()) {
			return;
		}
		mRunning = true;
		mAngleAnimator.start();
		mSweepAnimator.start();
		invalidate();
	}
	
	private void innerStop() {
		if (!isRunning()) {
			return;
		}
		mRunning = false;
		mAngleAnimator.cancel();
		mSweepAnimator.cancel();
		invalidate();
	}
	
	private boolean isRunning() {
		return mRunning;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		fBounds.left = mBorderWidth / 2f + .5f;
		fBounds.right = w - mBorderWidth / 2f - .5f;
		fBounds.top = mBorderWidth / 2f + .5f;
		fBounds.bottom = h - mBorderWidth / 2f - .5f;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
		float sweepAngle = mCurrentSweepAngle;
		if (mModeAppearing) {
			float percent = mCurrentSweepAngle / (360 - minSweepAngle * 2);
			int tempColor = gradient(mColors[mCurrentColorIndex], mColors[mNextColorIndex], percent);
			mPaint.setColor(tempColor);
			
			sweepAngle += minSweepAngle;
		} else {
			startAngle = startAngle + sweepAngle;
			sweepAngle = 360 - sweepAngle - minSweepAngle;
		}
		canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
	}
	
	private void toggleAppearingMode() {
		mModeAppearing = !mModeAppearing;
		if (mModeAppearing) {
			mCurrentColorIndex = (++mCurrentColorIndex) % mColors.length;
			mNextColorIndex = (++mNextColorIndex) % mColors.length;
			mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + minSweepAngle * 2) % 360;
		}
	}
	
	private void setupAngleAnimation() {
		mAngleAnimator = ObjectAnimator.ofFloat(this, new Property<CircularProgressView, Float>(Float.class, "angle") {
			@Override
			public Float get(CircularProgressView object) {
				return object.mCurrentGlobalAngle;
			}
			
			@Override
			public void set(CircularProgressView object, Float value) {
				mCurrentGlobalAngle = value;
				invalidate();
			}
		}, 360f);
		mAngleAnimator.setInterpolator(new LinearInterpolator());
		mAngleAnimator.setDuration(angleAnimatorDuration);
		mAngleAnimator.setRepeatMode(ValueAnimator.RESTART);
		mAngleAnimator.setRepeatCount(ValueAnimator.INFINITE);
	}
	
	private void setupSweepAnimator() {
		mSweepAnimator = ObjectAnimator.ofFloat(this, new Property<CircularProgressView, Float>(Float.class, "arc") {
			@Override
			public Float get(CircularProgressView object) {
				return object.mCurrentSweepAngle;
			}
			
			@Override
			public void set(CircularProgressView object, Float value) {
				mCurrentSweepAngle = value;
				invalidate();
			}
		}, 360f - minSweepAngle * 2);
		mSweepAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		mSweepAnimator.setDuration(sweepAnimatorDuration);
		mSweepAnimator.setRepeatMode(ValueAnimator.RESTART);
		mSweepAnimator.setRepeatCount(ValueAnimator.INFINITE);
		mSweepAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
			
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				toggleAppearingMode();
			}
		});
	}
	
	/**
	 * 计算两种颜色，按照百分比适配出新的颜色
	 */
	private static int gradient(int color1, int color2, float percent) {
		int red1 = (color1 & 0xff0000) >> 16;
		int green1 = (color1 & 0xff00) >> 8;
		int blue1 = color1 & 0xff;
		int red2 = (color2 & 0xff0000) >> 16;
		int green2 = (color2 & 0xff00) >> 8;
		int blue2 = color2 & 0xff;
		int newRed = (int) (red2 * percent + red1 * (1 - percent));
		int newGreen = (int) (green2 * percent + green1 * (1 - percent));
		int newBlue = (int) (blue2 * percent + blue1 * (1 - percent));
		return Color.argb(255, newRed, newGreen, newBlue);
	}
	
	private static int dp2px(Context context, float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
	}
}
