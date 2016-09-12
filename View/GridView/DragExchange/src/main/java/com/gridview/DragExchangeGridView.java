package com.gridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.yline.log.LogFileUtil;

/**
 * 拖拽的GridView,适配做的不好
 *
 * @author yline 2016/9/12 --> 22:39
 * @version 1.0.0
 */
public class DragExchangeGridView extends GridView
{
	private static final String TAG = "DragGridView";

	/** 设置拖动后的倍数 */
	private static final float mDragScale = 1.0f;

	/** 点击时,手指在屏幕的上X位置 */
	public int windowX;

	/** 点击时,手指在屏幕的上Y位置 */
	public int windowY;

	/** 拖拽时,控件在屏幕中的位置 = 手指在屏幕的上X位置 - 手指在控件中的位置 */
	private int win_view_x;

	/** 拖拽时,手指在屏幕的上y位置 - 手指在控件中的位置 = 控件距离最上边的距离 */
	private int win_view_y;

	/** 开始拖动的Item的position */
	private int startPosition;

	/** UP后对应的ITEM的position */
	private int endPosition;

	/** 拖动时候对应的VIEW */
	private View dragImageView = null;

	/** 拖拽时,镜像的方式 */
	private WindowManager windowManager = null;

	/** 拖拽时,镜像的layout */
	private WindowManager.LayoutParams windowParams = null;

	/** 长按,震动 */
	private Vibrator mVibrator;

	/** 是否在移动 */
	private boolean isMoving = false;

	private ViewGroup dragViewGroup;

	/** 列数 */
	private int mColumns;

	public DragExchangeGridView(Context context)
	{
		this(context, null);
	}

	public DragExchangeGridView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public DragExchangeGridView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/** 禁止滑动 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

		mColumns = getNumColumns();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		// 这里可以实现画线功能
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
		{
			windowX = (int) ev.getX();
			windowY = (int) ev.getY();
			setGridClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 长按点击监听
	 *
	 * @param ev
	 */
	private void setGridClickListener(final MotionEvent ev)
	{
		setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				LogFileUtil.v(TAG, "onItemLongClick -> dragPosition = " + startPosition);

				startPosition = position;
				dragViewGroup = (ViewGroup) getChildAt(startPosition - getFirstVisiblePosition());

				if (startPosition != AdapterView.INVALID_POSITION)
				{
					win_view_x = windowX - dragViewGroup.getLeft() + getLeft();//VIEW相对自己的X，半斤
					win_view_y = windowY - dragViewGroup.getTop() + getTop();//VIEW相对自己的y，半斤

					// 获取当前控件
					dragViewGroup.setVisibility(View.INVISIBLE);
					dragViewGroup.destroyDrawingCache();
					dragViewGroup.setDrawingCacheEnabled(true);
					Bitmap dragBitmap = Bitmap.createBitmap(dragViewGroup.getDrawingCache());
					mVibrator.vibrate(50);//设置震动时间
					startDrag(dragBitmap, (int) ev.getRawX(), (int) ev.getRawY());

					isMoving = true;
					if (getAdapter() instanceof IDragListener)
					{
						((IDragListener) getAdapter()).onDragStart();
					}

					/** 请求父类不要拦截事件 */
					requestDisallowInterceptTouchEvent(true);
					return true;
				}

				return false;
			}
		});

		setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (getAdapter() instanceof IItemClickListener)
				{
					((IItemClickListener) getAdapter()).onItemClick(view, position);
				}
			}
		});
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event)
	{
		return super.onGenericMotionEvent(event);
	}

	/**
	 * 这里调用了多次onTouchEvent
	 *
	 * @see android.widget.AbsListView#onTouchEvent(MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		boolean isUp2Parent = super.onTouchEvent(ev);
		if (null != dragImageView && startPosition != AdapterView.INVALID_POSITION)
		{
			//  移动时候的对应x,y位置
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			int dPosition = pointToPosition(moveX, moveY);
			switch (ev.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					moveX = (int) ev.getX();
					moveY = (int) ev.getY();

					windowX = (int) ev.getX();
					windowY = (int) ev.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					onDrag((int) ev.getRawX(), (int) ev.getRawY());
					if (isMoving)
					{
						onMove(dPosition);
					}
					break;
				case MotionEvent.ACTION_UP:
					stopDrag();
					finishDrag(moveX, moveY);
					if (getAdapter() instanceof IDragListener)
					{
						((IDragListener) getAdapter()).onDragEnd(endPosition);
					}
					break;
				default:
					break;
			}
		}
		return isUp2Parent;
	}

	/**
	 * getWidth() = 1280;getHeight() = 580;
	 * getPaddingLeft() = getPaddingRight() = getPaddingBottom() = getPaddingTop() = 0;
	 * getLeft() = 0;getRight() = 1280;getTop() = 30;getBottom() = 610;
	 * getHorizontalSpacing() = 20;getVerticalSpacing() = 20;
	 * 拖动时,操作
	 *
	 * @param dPosition 拖拽时,手指的位置
	 */
	private void onMove(int dPosition)
	{
		// 位置还未改变时,啥也不干
		if ((dPosition == AdapterView.INVALID_POSITION) || (dPosition == startPosition))
		{
			return;
		}

		// 位置发生改变后:
		endPosition = dPosition;

		// 获取到移动的view
		ViewGroup moveViewGroup = (ViewGroup) getChildAt(endPosition);

		// 确定动画移动距离
		LogFileUtil.v(TAG, "startPosition = " + startPosition + ", endPosition = " + endPosition + ",cloumn = " + mColumns);
		// 计算行列差
		int diffX = startPosition % mColumns - endPosition % mColumns;
		int diffY = startPosition / mColumns - endPosition / mColumns;
		// LogFileUtil.v(TAG, "diffX = " + diffX + ",diffY = " + diffY);

		// 实现动画
		Animation moveAnimation = getMoveAnimation(diffX, diffY);
		moveViewGroup.startAnimation(moveAnimation);
		moveAnimation.setAnimationListener(new Animation.AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
				// 解决抖动问题
				isMoving = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				LogFileUtil.v(TAG, "onAnimationEnd -> ");
				if (getAdapter() instanceof IDragListener)
				{
					((IDragListener) getAdapter()).onExchange(startPosition, endPosition);
				}
				startPosition = endPosition;
				isMoving = true;
			}
		});
	}

	/**
	 * 获取移动动画
	 *
	 * @param toXValue 移动X的距离,相对于自身的倍数
	 * @param toYValue 移动Y的距离,相对于自身的倍数
	 * @return 动画
	 */
	private Animation getMoveAnimation(float toXValue, float toYValue)
	{
		LogFileUtil.v(TAG, "toXValue = " + toXValue + ",toYValue" + toYValue);
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, toXValue, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, toYValue);// 当前位置移动到指定位置

		// 设置一个动画效果执行完毕后，View对象保留在终止的位置。
		mTranslateAnimation.setFillAfter(true);
		mTranslateAnimation.setDuration(100);
		return mTranslateAnimation;
	}

	/**
	 * 开始拖动
	 *
	 * @param dragBitmap 拖动时的Bitmap
	 * @param x          拖动时,手指在屏幕的上X位置
	 * @param y          拖动时,手指在屏幕的上Y位置
	 */
	private void startDrag(Bitmap dragBitmap, int x, int y)
	{
		stopDrag();
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;
		windowParams.x = x - win_view_x;
		windowParams.y = y - win_view_y;
		windowParams.width = (int) (mDragScale * dragBitmap.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
		windowParams.height = (int) (mDragScale * dragBitmap.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
		windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		//        this.windowParams.flags =
		//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
		//                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		this.windowParams.format = PixelFormat.TRANSLUCENT;
		this.windowParams.windowAnimations = 0;
		ImageView imageView = new ImageView(getContext());
		imageView.setImageBitmap(dragBitmap);
		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
		windowManager.addView(imageView, windowParams);
		dragImageView = imageView;
	}

	/** 正在拖动时,移动WindowManager,layout */
	private void onDrag(int rawx, int rawy)
	{
		if (null != dragImageView)
		{
			windowParams.alpha = 1.f;
			windowParams.x = rawx - win_view_x;
			windowParams.y = rawy - win_view_y;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}

	/** 拖动结束,清空资源 */
	private void stopDrag()
	{
		if (null != dragImageView)
		{
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}

	/**
	 * 拖拽结束,更新界面
	 */
	private void finishDrag(int x, int y)
	{
		isMoving = false;
		endPosition = pointToPosition(x, y);
		if (getAdapter() instanceof DragExchangeAdapter)
		{
			((DragExchangeAdapter) getAdapter()).notifyDataSetChanged();
		}
	}

	public interface IItemClickListener
	{
		/**
		 * DragGridView中Item点击事件
		 *
		 * @param view     The view within the AdapterView that was clicked (this will be a view provided by the adapter)
		 * @param position position The position of the view in the adapter.
		 */
		void onItemClick(View view, int position);
	}

	public interface IDragListener
	{
		/** 拖拽开始 */
		public void onDragStart();

		/**
		 * 交换位置
		 *
		 * @param startPosition
		 * @param endPosition
		 */
		public void onExchange(int startPosition, int endPosition);

		/**
		 * 拖拽结束
		 *
		 * @param endPosition
		 */
		public void onDragEnd(int endPosition);
	}
}
