package com.wheel.lib;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.List;

/**
 * 滚轮选择器
 * 1，Adapter数据类型不指定，若出现数据对应不上的问题，上层使用的问题
 * 2，默认显示的是对应的Object.toString()方法
 *
 * @version 1.1.0
 */
public class WheelPicker extends View implements IDebug, IWheelPicker, Runnable {
	/**
	 * 数据项对齐方式标识值
	 *
	 * @see #setItemAlign(int)
	 */
	public static final int ALIGN_CENTER = 0, ALIGN_LEFT = 1, ALIGN_RIGHT = 2;
	
	private static final String TAG = WheelPicker.class.getSimpleName();
	
	private final Handler mHandler = new Handler();
	
	private Paint mPaint;
	
	private Scroller mScroller;
	
	private VelocityTracker mTracker;
	
	private Rect mRectDrawn;
	
	private Rect mRectIndicatorHead, mRectIndicatorFoot;
	
	private Rect mRectCurrentItem;
	
	private Camera mCamera;
	
	private Matrix mMatrixRotate, mMatrixDepth;
	
	/**
	 * 滚轮选择器将会绘制的数据项数量
	 *
	 * @see #setVisibleItemCount(int)
	 */
	private int mDrawnItemCount;
	
	/**
	 * 滚轮选择器将会绘制的Item数量的一半
	 */
	private int mHalfDrawnItemCount;
	
	/**
	 * 单个文本最大宽高
	 */
	private int mTextMaxWidth, mTextMaxHeight;
	
	/**
	 * 滚轮选择器单个数据项高度以及单个数据项一半的高度
	 */
	private int mItemHeight, mHalfItemHeight;
	
	/**
	 * 滚轮选择器内容区域高度的一半
	 */
	private int mHalfWheelHeight;
	
	/**
	 * 当前被选中的数据项所显示的数据在数据源中的位置
	 *
	 * @see #getCurrentItemPosition()
	 */
	private int mCurrentItemPosition;
	
	/**
	 * 滚轮滑动时可以滑动到的最小/最大的Y坐标
	 */
	private int mMinFlingY, mMaxFlingY;
	
	/**
	 * 滚轮滑动时的最小/最大速度
	 */
	private int mMinimumVelocity = 50, mMaximumVelocity = 8000;
	
	/**
	 * 滚轮选择器中心坐标
	 */
	private int mWheelCenterX, mWheelCenterY;
	
	/**
	 * 滚轮选择器绘制中心坐标
	 */
	private int mDrawnCenterX, mDrawnCenterY;
	
	/**
	 * 滚轮选择器视图区域在Y轴方向上的偏移值
	 */
	private int mScrollOffsetY;
	
	/**
	 * 用户手指上一次触摸事件发生时事件Y坐标
	 */
	private int mLastPointY;
	
	/**
	 * 手指触摸屏幕时事件点的Y坐标
	 */
	private int mDownPointY;
	
	/**
	 * 点击与触摸的切换阀值
	 */
	private int mTouchSlop = 8;
	
	/**
	 * 是否为点击模式
	 */
	private boolean isClick;
	
	/**
	 * 是否为强制结束滑动
	 */
	private boolean isForceFinishScroll;
	
	private boolean isDebug;
	
	private WheelPickerAttr mPickerAttr;
	
	private WheelPickerAdapter mPickerAdapter;
	
	public WheelPicker(Context context) {
		this(context, null);
	}
	
	public WheelPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPickerAttr = new WheelPickerAttr(context, attrs);
		mPickerAdapter = new WheelPickerAdapter<>();
		
		// 可见数据项改变后更新与之相关的参数
		// Update relevant parameters when the count of visible item changed
		updateVisibleItemCount();
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mPaint.setTextSize(mPickerAttr.getItemTextSize());
		
		// 更新文本对齐方式 Update alignment of text
		updateItemTextAlign();
		
		// 计算文本尺寸 Correct sizes of text
		computeTextSize();
		
		mScroller = new Scroller(getContext());
		
		ViewConfiguration conf = ViewConfiguration.get(getContext());
		mMinimumVelocity = conf.getScaledMinimumFlingVelocity();
		mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
		mTouchSlop = conf.getScaledTouchSlop();
		
		mRectDrawn = new Rect();
		
		mRectIndicatorHead = new Rect();
		mRectIndicatorFoot = new Rect();
		
		mRectCurrentItem = new Rect();
		
		mCamera = new Camera();
		
		mMatrixRotate = new Matrix();
		mMatrixDepth = new Matrix();
	}
	
	private void updateVisibleItemCount() {
		if (mPickerAttr.getVisibleItemCount() < 2) {
			throw new ArithmeticException("Wheel's visible item count can not be less than 2!");
		}
		
		// 确保滚轮选择器可见数据项数量为奇数
		// Be sure count of visible item is odd number
		if (mPickerAttr.getVisibleItemCount() % 2 == 0) {
			mPickerAttr.setVisibleItemCount(mPickerAttr.getVisibleItemCount() + 1);
		}
		mDrawnItemCount = mPickerAttr.getVisibleItemCount() + 2;
		mHalfDrawnItemCount = mDrawnItemCount / 2;
	}
	
	private void computeTextSize() {
		mTextMaxWidth = mTextMaxHeight = 0;
		if (mPickerAttr.isHasSameWidth()) {
			mTextMaxWidth = (int) mPaint.measureText(mPickerAdapter.getDataValue(0));
		} else if (isPosInRang(mPickerAttr.getTextMaxWidthPosition())) {
			mTextMaxWidth = (int) mPaint.measureText(mPickerAdapter.getDataValue(mPickerAttr.getTextMaxWidthPosition()));
		} else if (!TextUtils.isEmpty(mPickerAttr.getMaxWidthText())) {
			mTextMaxWidth = (int) mPaint.measureText(mPickerAttr.getMaxWidthText());
		} else {
			for (int i = 0; i < mPickerAdapter.getCount(); i++) {
				String text = mPickerAdapter.getDataValue(i);
				int width = (int) mPaint.measureText(text);
				mTextMaxWidth = Math.max(mTextMaxWidth, width);
			}
		}
		Paint.FontMetrics metrics = mPaint.getFontMetrics();
		mTextMaxHeight = (int) (metrics.bottom - metrics.top);
	}
	
	private void updateItemTextAlign() {
		switch (mPickerAttr.getItemAlign()) {
			case ALIGN_LEFT:
				mPaint.setTextAlign(Paint.Align.LEFT);
				break;
			case ALIGN_RIGHT:
				mPaint.setTextAlign(Paint.Align.RIGHT);
				break;
			default:
				mPaint.setTextAlign(Paint.Align.CENTER);
				break;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		// 计算原始内容尺寸
		// Correct sizes of original content
		int resultWidth = mTextMaxWidth;
		int resultHeight = mTextMaxHeight * mPickerAttr.getVisibleItemCount() + mPickerAttr.getItemSpace() * (mPickerAttr.getVisibleItemCount() - 1);
		
		// 如果开启弯曲效果则需要重新计算弯曲后的尺寸
		// Correct view sizes again if curved is enable
		if (mPickerAttr.isCurved()) {
			resultHeight = (int) (2 * resultHeight / Math.PI);
		}
		if (isDebug) {
			Log.i(TAG, "Wheel's content size is (" + resultWidth + ":" + resultHeight + ")");
		}
		
		// 考虑内边距对尺寸的影响
		// Consideration padding influence the view sizes
		resultWidth += getPaddingLeft() + getPaddingRight();
		resultHeight += getPaddingTop() + getPaddingBottom();
		if (isDebug) {
			Log.i(TAG, "Wheel's size is (" + resultWidth + ":" + resultHeight + ")");
		}
		
		// 考虑父容器对尺寸的影响
		// Consideration sizes of parent can influence the view sizes
		resultWidth = measureSize(modeWidth, sizeWidth, resultWidth);
		resultHeight = measureSize(modeHeight, sizeHeight, resultHeight);
		
		setMeasuredDimension(resultWidth, resultHeight);
	}
	
	private int measureSize(int mode, int sizeExpect, int sizeActual) {
		int realSize;
		if (mode == MeasureSpec.EXACTLY) {
			realSize = sizeExpect;
		} else {
			realSize = sizeActual;
			if (mode == MeasureSpec.AT_MOST) {
				realSize = Math.min(realSize, sizeExpect);
			}
		}
		return realSize;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		// 设置内容区域
		// Set content region
		mRectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
		if (isDebug) {
			Log.i(TAG, "Wheel's drawn rect size is (" + mRectDrawn.width() + ":" + mRectDrawn.height() + ") and location is (" + mRectDrawn.left + ":" + mRectDrawn.top + ")");
		}
		
		// 获取内容区域中心坐标
		// Get the center coordinates of content region
		mWheelCenterX = mRectDrawn.centerX();
		mWheelCenterY = mRectDrawn.centerY();
		
		// 计算数据项绘制中心
		// Correct item drawn center
		computeDrawnCenter();
		
		mHalfWheelHeight = mRectDrawn.height() / 2;
		
		mItemHeight = mRectDrawn.height() / mPickerAttr.getVisibleItemCount();
		mHalfItemHeight = mItemHeight / 2;
		
		// 初始化滑动最大坐标
		// Initialize fling max Y-coordinates
		computeFlingLimitY();
		
		// 计算指示器绘制区域
		// Correct region of indicator
		computeIndicatorRect();
		
		// 计算当前选中的数据项区域
		// Correct region of current select item
		computeCurrentItemRect();
	}
	
	private void computeDrawnCenter() {
		switch (mPickerAttr.getItemAlign()) {
			case ALIGN_LEFT:
				mDrawnCenterX = mRectDrawn.left;
				break;
			case ALIGN_RIGHT:
				mDrawnCenterX = mRectDrawn.right;
				break;
			default:
				mDrawnCenterX = mWheelCenterX;
				break;
		}
		mDrawnCenterY = (int) (mWheelCenterY - ((mPaint.ascent() + mPaint.descent()) / 2));
	}
	
	private void computeFlingLimitY() {
		int currentItemOffset = mPickerAttr.getSelectedItemPosition() * mItemHeight;
		mMinFlingY = mPickerAttr.isCyclic() ? Integer.MIN_VALUE : -mItemHeight * (mPickerAdapter.getCount() - 1) + currentItemOffset;
		mMaxFlingY = mPickerAttr.isCyclic() ? Integer.MAX_VALUE : currentItemOffset;
	}
	
	private void computeIndicatorRect() {
		if (!mPickerAttr.isHasIndicator()) {
			return;
		}
		int halfIndicatorSize = mPickerAttr.getIndicatorSize() / 2;
		int indicatorHeadCenterY = mWheelCenterY + mHalfItemHeight;
		int indicatorFootCenterY = mWheelCenterY - mHalfItemHeight;
		mRectIndicatorHead.set(mRectDrawn.left, indicatorHeadCenterY - halfIndicatorSize, mRectDrawn.right, indicatorHeadCenterY + halfIndicatorSize);
		mRectIndicatorFoot.set(mRectDrawn.left, indicatorFootCenterY - halfIndicatorSize, mRectDrawn.right, indicatorFootCenterY + halfIndicatorSize);
	}
	
	private void computeCurrentItemRect() {
		if (!mPickerAttr.isHasCurtain() && mPickerAttr.getSelectedItemTextColor() == -1) {
			return;
		}
		mRectCurrentItem.set(mRectDrawn.left, mWheelCenterY - mHalfItemHeight, mRectDrawn.right, mWheelCenterY + mHalfItemHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		mPickerAdapter.callWheelScrolled(mScrollOffsetY);
		
		int drawnDataStartPos = -mScrollOffsetY / mItemHeight - mHalfDrawnItemCount;
		for (int drawnDataPos = drawnDataStartPos + mPickerAttr.getSelectedItemPosition(), drawnOffsetPos = -mHalfDrawnItemCount; drawnDataPos < drawnDataStartPos + mPickerAttr.getSelectedItemPosition() + mDrawnItemCount; drawnDataPos++, drawnOffsetPos++) {
			String data = "";
			if (mPickerAttr.isCyclic()) {
				int actualPos = drawnDataPos % mPickerAdapter.getCount();
				actualPos = actualPos < 0 ? (actualPos + mPickerAdapter.getCount()) : actualPos;
				data = mPickerAdapter.getDataValue(actualPos);
			} else {
				if (isPosInRang(drawnDataPos)) {
					data = mPickerAdapter.getDataValue(drawnDataPos);
				}
			}
			mPaint.setColor(mPickerAttr.getItemTextColor());
			mPaint.setStyle(Paint.Style.FILL);
			int mDrawnItemCenterY = mDrawnCenterY + (drawnOffsetPos * mItemHeight) + mScrollOffsetY % mItemHeight;
			
			int distanceToCenter = 0;
			if (mPickerAttr.isCurved()) {
				// 计算数据项绘制中心距离滚轮中心的距离比率
				// Correct ratio of item's drawn center to wheel center
				float ratio = (mDrawnCenterY - Math.abs(mDrawnCenterY - mDrawnItemCenterY) - mRectDrawn.top) * 1.0F / (mDrawnCenterY - mRectDrawn.top);
				
				// 计算单位
				// Correct unit
				int unit = 0;
				if (mDrawnItemCenterY > mDrawnCenterY) {
					unit = 1;
				} else if (mDrawnItemCenterY < mDrawnCenterY) {
					unit = -1;
				}
				
				float degree = (-(1 - ratio) * 90 * unit);
				if (degree < -90) {
					degree = -90;
				}
				if (degree > 90) {
					degree = 90;
				}
				distanceToCenter = computeSpace((int) degree);
				
				int transX = mWheelCenterX;
				switch (mPickerAttr.getItemAlign()) {
					case ALIGN_LEFT:
						transX = mRectDrawn.left;
						break;
					case ALIGN_RIGHT:
						transX = mRectDrawn.right;
						break;
					default:
						transX = mWheelCenterX;
				}
				int transY = mWheelCenterY - distanceToCenter;
				
				mCamera.save();
				mCamera.rotateX(degree);
				mCamera.getMatrix(mMatrixRotate);
				mCamera.restore();
				mMatrixRotate.preTranslate(-transX, -transY);
				mMatrixRotate.postTranslate(transX, transY);
				
				mCamera.save();
				mCamera.translate(0, 0, computeDepth((int) degree));
				mCamera.getMatrix(mMatrixDepth);
				mCamera.restore();
				mMatrixDepth.preTranslate(-transX, -transY);
				mMatrixDepth.postTranslate(transX, transY);
				
				mMatrixRotate.postConcat(mMatrixDepth);
			}
			if (mPickerAttr.isHasAtmospheric()) {
				int alpha = (int) ((mDrawnCenterY - Math.abs(mDrawnCenterY - mDrawnItemCenterY)) * 1.0F / mDrawnCenterY * 255);
				alpha = alpha < 0 ? 0 : alpha;
				mPaint.setAlpha(alpha);
			}
			// 根据卷曲与否计算数据项绘制Y方向中心坐标
			// Correct item's drawn centerY base on curved state
			int drawnCenterY = mPickerAttr.isCurved() ? mDrawnCenterY - distanceToCenter : mDrawnItemCenterY;
			
			// 判断是否需要为当前数据项绘制不同颜色
			// Judges need to draw different color for current item or not
			if (mPickerAttr.getSelectedItemTextColor() != -1) {
				canvas.save();
				if (mPickerAttr.isCurved()) {
					canvas.concat(mMatrixRotate);
				}
				canvas.clipRect(mRectCurrentItem, Region.Op.DIFFERENCE);
				canvas.drawText(data, mDrawnCenterX, drawnCenterY, mPaint);
				canvas.restore();
				
				mPaint.setColor(mPickerAttr.getSelectedItemTextColor());
				canvas.save();
				if (mPickerAttr.isCurved()) {
					canvas.concat(mMatrixRotate);
				}
				canvas.clipRect(mRectCurrentItem);
				canvas.drawText(data, mDrawnCenterX, drawnCenterY, mPaint);
				canvas.restore();
			} else {
				canvas.save();
				canvas.clipRect(mRectDrawn);
				if (mPickerAttr.isCurved()) {
					canvas.concat(mMatrixRotate);
				}
				canvas.drawText(data, mDrawnCenterX, drawnCenterY, mPaint);
				canvas.restore();
			}
			if (isDebug) {
				canvas.save();
				canvas.clipRect(mRectDrawn);
				mPaint.setColor(0xFFEE3333);
				int lineCenterY = mWheelCenterY + (drawnOffsetPos * mItemHeight);
				canvas.drawLine(mRectDrawn.left, lineCenterY, mRectDrawn.right, lineCenterY, mPaint);
				mPaint.setColor(0xFF3333EE);
				mPaint.setStyle(Paint.Style.STROKE);
				int top = lineCenterY - mHalfItemHeight;
				canvas.drawRect(mRectDrawn.left, top, mRectDrawn.right, top + mItemHeight, mPaint);
				canvas.restore();
			}
		}
		// 是否需要绘制幕布
		// Need to draw curtain or not
		if (mPickerAttr.isHasCurtain()) {
			mPaint.setColor(mPickerAttr.getCurtainColor());
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(mRectCurrentItem, mPaint);
		}
		// 是否需要绘制指示器
		// Need to draw indicator or not
		if (mPickerAttr.isHasIndicator()) {
			mPaint.setColor(mPickerAttr.getIndicatorColor());
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(mRectIndicatorHead, mPaint);
			canvas.drawRect(mRectIndicatorFoot, mPaint);
		}
		if (isDebug) {
			mPaint.setColor(0x4433EE33);
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, getPaddingLeft(), getHeight(), mPaint);
			canvas.drawRect(0, 0, getWidth(), getPaddingTop(), mPaint);
			canvas.drawRect(getWidth() - getPaddingRight(), 0, getWidth(), getHeight(), mPaint);
			canvas.drawRect(0, getHeight() - getPaddingBottom(), getWidth(), getHeight(), mPaint);
		}
	}
	
	private boolean isPosInRang(int position) {
		return position >= 0 && position < mPickerAdapter.getCount();
	}
	
	private int computeSpace(int degree) {
		return (int) (Math.sin(Math.toRadians(degree)) * mHalfWheelHeight);
	}
	
	private int computeDepth(int degree) {
		return (int) (mHalfWheelHeight - Math.cos(Math.toRadians(degree)) * mHalfWheelHeight);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (null != getParent()) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				if (null == mTracker) {
					mTracker = VelocityTracker.obtain();
				} else {
					mTracker.clear();
				}
				mTracker.addMovement(event);
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
					isForceFinishScroll = true;
				}
				mDownPointY = mLastPointY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (Math.abs(mDownPointY - event.getY()) < mTouchSlop) {
					isClick = true;
					break;
				}
				isClick = false;
				mTracker.addMovement(event);
				mPickerAdapter.callWheelScrollStateChanged(WheelPickerAdapter.SCROLL_STATE_DRAGGING);
				
				// 滚动内容
				// Scroll WheelPicker's content
				float move = event.getY() - mLastPointY;
				if (Math.abs(move) < 1) {
					break;
				}
				mScrollOffsetY += move;
				mLastPointY = (int) event.getY();
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				if (null != getParent()) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				if (isClick) {
					break;
				}
				mTracker.addMovement(event);
				
				mTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				
				// 根据速度判断是该滚动还是滑动
				// Judges the WheelPicker is scroll or fling base on current velocity
				isForceFinishScroll = false;
				int velocity = (int) mTracker.getYVelocity();
				if (Math.abs(velocity) > mMinimumVelocity) {
					mScroller.fling(0, mScrollOffsetY, 0, velocity, 0, 0, mMinFlingY, mMaxFlingY);
					mScroller.setFinalY(mScroller.getFinalY() + computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
				} else {
					mScroller.startScroll(0, mScrollOffsetY, 0, computeDistanceToEndPoint(mScrollOffsetY % mItemHeight));
				}
				// 校正坐标
				// Correct coordinates
				if (!mPickerAttr.isCyclic()) {
					if (mScroller.getFinalY() > mMaxFlingY) {
						mScroller.setFinalY(mMaxFlingY);
					} else if (mScroller.getFinalY() < mMinFlingY) {
						mScroller.setFinalY(mMinFlingY);
					}
				}
				mHandler.post(this);
				if (null != mTracker) {
					mTracker.recycle();
					mTracker = null;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				if (null != getParent()) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				if (null != mTracker) {
					mTracker.recycle();
					mTracker = null;
				}
				break;
			default:
				break;
		}
		return true;
	}
	
	private int computeDistanceToEndPoint(int remainder) {
		if (Math.abs(remainder) > mHalfItemHeight) {
			if (mScrollOffsetY < 0) {
				return -mItemHeight - remainder;
			} else {
				return mItemHeight - remainder;
			}
		} else {
			return -remainder;
		}
	}
	
	@Override
	public void run() {
		if (mPickerAdapter.getCount() == 0) {
			return;
		}
		if (mScroller.isFinished() && !isForceFinishScroll) {
			if (mItemHeight == 0) {
				return;
			}
			int position = (-mScrollOffsetY / mItemHeight + mPickerAttr.getSelectedItemPosition()) % mPickerAdapter.getCount();
			position = position < 0 ? position + mPickerAdapter.getCount() : position;
			if (isDebug) {
				Log.i(TAG, position + ":" + mPickerAdapter.getDataValue(position) + ":" + mScrollOffsetY);
			}
			mCurrentItemPosition = position;
			
			mPickerAdapter.callItemSelected(this, position);
			mPickerAdapter.callWheelSelected(position);
			mPickerAdapter.callWheelScrollStateChanged(WheelPickerAdapter.SCROLL_STATE_IDLE);
		}
		if (mScroller.computeScrollOffset()) {
			mPickerAdapter.callWheelScrollStateChanged(WheelPickerAdapter.SCROLL_STATE_SCROLLING);
			mScrollOffsetY = mScroller.getCurrY();
			postInvalidate();
			mHandler.postDelayed(this, 16);
		}
	}
	
	@Override
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	@Override
	public int getCurrentItemPosition() {
		return mCurrentItemPosition;
	}
	
	@Override
	public Typeface getTypeface() {
		if (null != mPaint) {
			return mPaint.getTypeface();
		}
		return null;
	}
	
	@Override
	public void setTypeface(Typeface tf) {
		if (null != mPaint) {
			mPaint.setTypeface(tf);
		}
		computeTextSize();
		requestLayout();
		invalidate();
	}
	
	/**
	 * 设置内容数据
	 *
	 * @param data 数据
	 */
	public void setData(List data) {
		mPickerAdapter.setData(data);
		notifyDataSetChanged();
	}
	
	public String getDataValue(int position) {
		return mPickerAdapter.getDataValue(position);
	}
	
	public int getCount() {
		return mPickerAdapter.getCount();
	}
	
	/**
	 * 设置自定义的Adapter，允许自定义实现valueOf
	 *
	 * @param pickerAdapter 数据适配
	 */
	public void setAdapter(WheelPickerAdapter pickerAdapter) {
		this.mPickerAdapter = pickerAdapter;
	}
	
	public void notifyDataSetChanged() {
		// 重置位置
		if (mPickerAttr.getSelectedItemPosition() > mPickerAdapter.getCount() - 1 || mCurrentItemPosition > mPickerAdapter.getCount() - 1) {
			mCurrentItemPosition = mPickerAdapter.getCount() - 1;
			mPickerAttr.setSelectedItemPosition(mCurrentItemPosition);
		} else {
			mPickerAttr.setSelectedItemPosition(mCurrentItemPosition);
		}
		mScrollOffsetY = 0;
		computeTextSize();
		computeFlingLimitY();
		requestLayout();
		invalidate();
	}
	
	/**
	 * 设置滚轮Item选中监听器
	 *
	 * @param listener 滚轮Item选中监听器{@link OnItemSelectedListener}
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		mPickerAdapter.setOnItemSelectedListener(listener);
	}
	
	/**
	 * 设置滚轮滚动状态改变监听器
	 *
	 * @param listener 滚轮滚动状态改变监听器
	 * @see OnWheelChangeListener
	 */
	public void setOnWheelChangeListener(OnWheelChangeListener listener) {
		mPickerAdapter.setOnWheelChangeListener(listener);
	}
	
	/******************************************* 以下是Attr 的set、get方法 ************************************************/
	@Override
	public int getVisibleItemCount() {
		return mPickerAttr.getVisibleItemCount();
	}
	
	@Override
	public void setVisibleItemCount(int count) {
		mPickerAttr.setVisibleItemCount(count);
		updateVisibleItemCount();
		requestLayout();
	}
	
	@Override
	public boolean isCyclic() {
		return mPickerAttr.isCyclic();
	}
	
	@Override
	public void setCyclic(boolean isCyclic) {
		mPickerAttr.setCyclic(isCyclic);
		computeFlingLimitY();
		invalidate();
	}
	
	@Override
	public int getSelectedItemPosition() {
		return mPickerAttr.getSelectedItemPosition();
	}
	
	@Override
	public void setSelectedItemPosition(int position) {
		mPickerAttr.setSelectedItemPosition(position);
		position = Math.min(position, mPickerAdapter.getCount() - 1);
		position = Math.max(position, 0);
		mCurrentItemPosition = position;
		mScrollOffsetY = 0;
		computeFlingLimitY();
		requestLayout();
		invalidate();
	}
	
	@Override
	public void setSameWidth(boolean hasSameWidth) {
		mPickerAttr.setHasSameWidth(hasSameWidth);
		computeTextSize();
		requestLayout();
		invalidate();
	}
	
	@Override
	public boolean hasSameWidth() {
		return mPickerAttr.isHasSameWidth();
	}
	
	@Override
	public String getMaximumWidthText() {
		return mPickerAttr.getMaxWidthText();
	}
	
	@Override
	public void setMaximumWidthText(String text) {
		if (null == text) {
			throw new NullPointerException("Maximum width text can not be null!");
		}
		mPickerAttr.setMaxWidthText(text);
		computeTextSize();
		requestLayout();
		invalidate();
	}
	
	@Override
	public int getMaximumWidthTextPosition() {
		return mPickerAttr.getTextMaxWidthPosition();
	}
	
	@Override
	public void setMaximumWidthTextPosition(int position) {
		if (!isPosInRang(position)) {
			throw new ArrayIndexOutOfBoundsException("Maximum width text Position must in [0, " + mPickerAdapter.getCount() + "), but current is " + position);
		}
		mPickerAttr.setTextMaxWidthPosition(position);
		computeTextSize();
		requestLayout();
		invalidate();
	}
	
	@Override
	public int getSelectedItemTextColor() {
		return mPickerAttr.getSelectedItemTextColor();
	}
	
	@Override
	public void setSelectedItemTextColor(int color) {
		mPickerAttr.setSelectedItemTextColor(color);
		computeCurrentItemRect();
		invalidate();
	}
	
	@Override
	public int getItemTextColor() {
		return mPickerAttr.getItemTextColor();
	}
	
	@Override
	public void setItemTextColor(int color) {
		mPickerAttr.setItemTextColor(color);
		invalidate();
	}
	
	@Override
	public int getItemTextSize() {
		return mPickerAttr.getItemTextSize();
	}
	
	@Override
	public void setItemTextSize(int size) {
		mPickerAttr.setItemTextSize(size);
		mPaint.setTextSize(size);
		computeTextSize();
		requestLayout();
		invalidate();
	}
	
	@Override
	public int getItemSpace() {
		return mPickerAttr.getItemSpace();
	}
	
	@Override
	public void setItemSpace(int space) {
		mPickerAttr.setItemSpace(space);
		requestLayout();
		invalidate();
	}
	
	@Override
	public void setIndicator(boolean hasIndicator) {
		mPickerAttr.setHasIndicator(hasIndicator);
		computeIndicatorRect();
		invalidate();
	}
	
	@Override
	public boolean hasIndicator() {
		return mPickerAttr.isHasIndicator();
	}
	
	@Override
	public int getIndicatorSize() {
		return mPickerAttr.getIndicatorSize();
	}
	
	@Override
	public void setIndicatorSize(int size) {
		mPickerAttr.setIndicatorSize(size);
		computeIndicatorRect();
		invalidate();
	}
	
	@Override
	public int getIndicatorColor() {
		return mPickerAttr.getIndicatorColor();
	}
	
	@Override
	public void setIndicatorColor(int color) {
		mPickerAttr.setIndicatorColor(color);
		invalidate();
	}
	
	@Override
	public void setCurtain(boolean hasCurtain) {
		mPickerAttr.setHasCurtain(hasCurtain);
		computeCurrentItemRect();
		invalidate();
	}
	
	@Override
	public boolean hasCurtain() {
		return mPickerAttr.isHasCurtain();
	}
	
	@Override
	public int getCurtainColor() {
		return mPickerAttr.getCurtainColor();
	}
	
	@Override
	public void setCurtainColor(int color) {
		mPickerAttr.setCurtainColor(color);
		invalidate();
	}
	
	@Override
	public void setAtmospheric(boolean hasAtmospheric) {
		mPickerAttr.setHasAtmospheric(hasAtmospheric);
		invalidate();
	}
	
	@Override
	public boolean hasAtmospheric() {
		return mPickerAttr.isHasAtmospheric();
	}
	
	@Override
	public boolean isCurved() {
		return mPickerAttr.isCurved();
	}
	
	@Override
	public void setCurved(boolean isCurved) {
		mPickerAttr.setCurved(isCurved);
		requestLayout();
		invalidate();
	}
	
	@Override
	public int getItemAlign() {
		return mPickerAttr.getItemAlign();
	}
	
	@Override
	public void setItemAlign(int align) {
		mPickerAttr.setItemAlign(align);
		updateItemTextAlign();
		computeDrawnCenter();
		invalidate();
	}
	
	
	/**
	 * 滚轮选择器Item项被选中时监听接口
	 *
	 * @version 1.1.0
	 */
	public interface OnItemSelectedListener<T> {
		/**
		 * 当滚轮选择器数据项被选中时回调该方法
		 * 滚动选择器滚动停止后会回调该方法并将当前选中的数据和数据在数据列表中对应的位置返回
		 *
		 * @param picker   滚轮选择器
		 * @param t        当前选中的数据
		 * @param position 当前选中的数据在数据列表中的位置
		 */
		void onItemSelected(WheelPicker picker, T t, int position);
	}
	
	/**
	 * 滚轮选择器滚动时监听接口
	 *
	 * @since 2016-06-17
	 */
	public interface OnWheelChangeListener<T> {
		/**
		 * 当滚轮选择器滚动时回调该方法
		 * 滚轮选择器滚动时会将当前滚动位置与滚轮初始位置之间的偏移距离返回，该偏移距离有正负之分，正值表示
		 * 滚轮正在往上滚动，负值则表示滚轮正在往下滚动
		 *
		 * @param offset 当前滚轮滚动距离上一次滚轮滚动停止后偏移的距离
		 */
		void onWheelScrolled(int offset);
		
		/**
		 * 当滚轮选择器停止后回调该方法
		 * 滚轮选择器停止后会回调该方法并将当前选中的数据项在数据列表中的位置返回
		 *
		 * @param t        当前选中的数据
		 * @param position 当前选中的数据项在数据列表中的位置
		 */
		void onWheelSelected(T t, int position);
		
		/**
		 * 当滚轮选择器滚动状态改变时回调该方法
		 * 滚动选择器的状态总是会在静止、拖动和滑动三者之间切换，当状态改变时回调该方法
		 *
		 * @param state 滚轮选择器滚动状态，其值仅可能为下列之一
		 *              {@link WheelPickerAdapter#SCROLL_STATE_IDLE}
		 *              表示滚动选择器处于静止状态
		 *              {@link WheelPickerAdapter#SCROLL_STATE_DRAGGING}
		 *              表示滚动选择器处于拖动状态
		 *              {@link WheelPickerAdapter#SCROLL_STATE_SCROLLING}
		 *              表示滚动选择器处于滑动状态
		 *              <p/>
		 *              State of WheelPicker, only one of the following
		 *              {@link WheelPickerAdapter#SCROLL_STATE_IDLE}
		 *              Express WheelPicker in state of idle
		 *              {@link WheelPickerAdapter#SCROLL_STATE_DRAGGING}
		 *              Express WheelPicker in state of dragging
		 *              {@link WheelPickerAdapter#SCROLL_STATE_SCROLLING}
		 *              Express WheelPicker in state of scrolling
		 */
		void onWheelScrollStateChanged(int state);
	}
}