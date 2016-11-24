package com.view.dragbubble.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 仿QQ拖拽气泡
 * @author yline 2016/9/26 --> 23:51
 * @version 1.0.0
 */
public class DragBubble extends TextView
{
	// 是否完成初始化背景色
	private boolean initBgFlag;

	// 对外接口，拖拽完成监听事件
	private OnDragListener onDragListener;

	private int backgroundColor = Color.RED;  // 背景色

	private PointView pointView;

	private int x, y, r;

	private ViewGroup scrollParent;

	private int[] p = new int[2];

	public DragBubble(Context context)
	{
		this(context, null);
	}

	public DragBubble(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initDragBubble();
	}

	public void setOnDragListener(OnDragListener onDragListener)
	{
		this.onDragListener = onDragListener;
	}

	public int getBackgroundColor()
	{
		return backgroundColor;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		if (w != h)
		{ // 将宽高设置为一样
			int x = Math.max(w, h);
			// 设置当前组件大小
			setMeasuredDimension(x, x);
		}
	}

	/**
	 * 设置背景色
	 * @param backgroundColor
	 */
	public void setBackgroundColor(int backgroundColor)
	{
		this.backgroundColor = backgroundColor;
		DragBubble.this.setBackgroundDrawable(createStateListDrawable((getHeight() > getWidth() ? getHeight() : getWidth()) / 2, backgroundColor));

	}

	private void initDragBubble()
	{
		setGravity(Gravity.CENTER);
		// 视图树绘制之前
		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener()
		{

			@Override
			public boolean onPreDraw()
			{  // 返回true则继续进行绘制，返回false则取消绘制
				if (!initBgFlag)
				{
					setBackgroundDrawable(createStateListDrawable(  // 取长边的一半为半径
							(getHeight() > getWidth() ? getHeight() : getWidth()) / 2, backgroundColor));
					initBgFlag = true;
					return false;
				}
				return true;
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		View root = getRootView();
		if (root == null || !(root instanceof ViewGroup))
		{
			return false;
		}
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				root.getLocationOnScreen(p);
				scrollParent = getScrollParent();  // 获取父容器
				if (scrollParent != null)
				{
					scrollParent.requestDisallowInterceptTouchEvent(true); // 父容器将不拦截事件
				}
				int location[] = new int[2];
				getLocationOnScreen(location);
				x = location[0] + (getWidth() / 2) - p[0];
				y = location[1] + (getHeight() / 2) - p[1];
				r = (getWidth() + getHeight()) / 4;
				pointView = new PointView(getContext());
				pointView.setLayoutParams(new ViewGroup.LayoutParams(root.getWidth(), root.getHeight()));
				setDrawingCacheEnabled(true);  // 开启图片缓存，保存当前带数字的图片
				pointView.catchBitmap = getDrawingCache();// 设置PointView拖动时的带数字图片，无须重新绘制
				pointView.setLocation(x, y, r, event.getRawX() - p[0], event.getRawY() - p[1]);
				((ViewGroup) root).addView(pointView);
				DragBubble.this.setVisibility(View.INVISIBLE);
				break;
			case MotionEvent.ACTION_MOVE:
				pointView.refreshXY(event.getRawX() - p[0], event.getRawY() - p[1]);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (scrollParent != null)
				{
					scrollParent.requestDisallowInterceptTouchEvent(false);
				}
				if (!pointView.broken)
				{ // 没有拉断
					pointView.cancel();
				}
				else if (pointView.nearby)
				{ // 拉断了,但是又回去了
					pointView.cancel();
				}
				else
				{  // 彻底拉断了
					pointView.broken();
				}
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * 获取viewGroup
	 * @return
	 */
	private ViewGroup getScrollParent()
	{
		View p = this;
		while (true)
		{
			View v;
			try
			{
				v = (View) p.getParent();
			}
			catch (ClassCastException e)
			{
				return null;
			}
			if (v == null)
			{
				return null;
			}
			// 这里判断他的父布局为哪种view，拓展时可自行添加，或者对外放开接口
			if (v instanceof AbsListView || v instanceof ScrollView || v instanceof ViewPager || v instanceof RecyclerView)
			{
				return (ViewGroup) v;
			}
			p = v;
		}
	}

	/**
	 * DragPoint的拖拽模型
	 */
	private class PointView extends View
	{
		private Bitmap catchBitmap;

		private Circle c1;

		private Circle c2;

		private Paint paint;

		private Path path = new Path();

		private int maxDistance = 10; // 10倍半径距离视为拉断

		private boolean broken; // 是否拉断过

		private boolean out; // 放手的时候是否拉断

		private boolean nearby; // 判断是否超过10倍半径距离

		private int brokenProgress;

		public PointView(Context context)
		{
			super(context);
			initPaint();
		}

		private void initPaint()
		{
			paint = new Paint();
			paint.setColor(backgroundColor);
			paint.setAntiAlias(true);
		}

		public void setLocation(float c1X, float c1Y, float r, float endX, float endY)
		{
			broken = false;
			c1 = new Circle(c1X, c1Y, r);
			c2 = new Circle(endX, endY, r);
		}

		public void refreshXY(float x, float y)
		{
			c2.x = x;
			c2.y = y;
			// 以前的半径应该根据距离缩小点了
			// 计算出距离
			double distance = c1.getDistance(c2);
			int rate = 10;
			c1.r = (float) ((c2.r * c2.r * rate) / (distance + (c2.r * rate)));
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			canvas.drawColor(Color.TRANSPARENT); // 清空画布
			if (out)
			{ // 绘制爆炸效果
				float dr = c2.r / 2 + c2.r * 4 * (brokenProgress / 100f);
				canvas.drawCircle(c2.x, c2.y, c2.r / (brokenProgress + 1), paint);
				canvas.drawCircle(c2.x - dr, c2.y - dr, c2.r / (brokenProgress + 2), paint);
				canvas.drawCircle(c2.x + dr, c2.y - dr, c2.r / (brokenProgress + 2), paint);
				canvas.drawCircle(c2.x - dr, c2.y + dr, c2.r / (brokenProgress + 2), paint);
				canvas.drawCircle(c2.x + dr, c2.y + dr, c2.r / (brokenProgress + 2), paint);
			}
			else
			{
				// 绘制手指跟踪的圆形
				canvas.drawBitmap(catchBitmap, c2.x - c2.r, c2.y - c2.r, paint);
				path.reset();
				float deltaX = c2.x - c1.x;
				float deltaY = -(c2.y - c1.y);
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				double sin = deltaY / distance;
				double cos = deltaX / distance;
				nearby = distance < c2.r * maxDistance;
				if (nearby && !broken)
				{ // 未超过最大距离并且未曾拉断过
					canvas.drawCircle(c1.x, c1.y, c1.r, paint);
					path.moveTo((float) (c1.x - c1.r * sin), (float) (c1.y - c1.r * cos));
					path.lineTo((float) (c1.x + c1.r * sin), (float) (c1.y + c1.r * cos));
					path.quadTo((c1.x + c2.x) / 2, (c1.y + c2.y) / 2, (float) (c2.x + c2.r * sin), (float) (c2.y + c2.r * cos));
					path.lineTo((float) (c2.x - c2.r * sin), (float) (c2.y - c2.r * cos));
					path.quadTo((c1.x + c2.x) / 2, (c1.y + c2.y) / 2, (float) (c1.x - c1.r * sin), (float) (c1.y - c1.r * cos));
					canvas.drawPath(path, paint);
				}
				else
				{
					broken = true;
				}
			}
		}

		public void cancel()
		{
			int duration = 150;
			AnimatorSet set = new AnimatorSet();
			ValueAnimator animx = ValueAnimator.ofFloat(c2.x, c1.x);
			animx.setDuration(duration);
			animx.setInterpolator(new OvershootInterpolator(2));
			animx.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
			{

				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					c2.x = (float) animation.getAnimatedValue();
					invalidate();
				}
			});
			ValueAnimator animy = ValueAnimator.ofFloat(c2.y, c1.y);
			animy.setDuration(duration);
			animy.setInterpolator(new OvershootInterpolator(2));
			animy.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
			{

				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					c2.y = (float) animation.getAnimatedValue();
					invalidate();
				}
			});
			set.playTogether(animx, animy);
			set.addListener(new AnimatorListenerAdapter()
			{

				@Override
				public void onAnimationEnd(Animator animation)
				{
					ViewGroup vg = (ViewGroup) PointView.this.getParent();
					vg.removeView(PointView.this);
					DragBubble.this.setVisibility(View.VISIBLE);
				}
			});
			set.start();
		}

		public void broken()
		{
			out = true;
			int duration = 500;
			ValueAnimator a = ValueAnimator.ofInt(0, 100);
			a.setDuration(duration);
			a.setInterpolator(new LinearInterpolator());
			a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
			{

				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					// 获取动画进度，范围为0~100.用来控制爆炸时的动画
					brokenProgress = (int) animation.getAnimatedValue();
					invalidate();
				}
			});
			a.addListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					ViewGroup vg = (ViewGroup) PointView.this.getParent();
					vg.removeView(PointView.this);
				}
			});
			a.start();
			if (onDragListener != null)
			{
				onDragListener.onDragOut();
			}
		}

		private class Circle
		{
			float x;

			float y;

			float r;

			public Circle(float x, float y, float r)
			{
				this.x = x;
				this.y = y;
				this.r = r;
			}

			public double getDistance(Circle c)
			{
				float deltaX = x - c.x;
				float deltaY = y - c.y;
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				return distance;
			}
		}
	}

	/**
	 * 制作一个有颜色背景的圆形图标
	 * @param radius 角度
	 * @param color  填充色
	 * @return StateListDrawable 此类定义了不同状态下的图片资源
	 */
	public static StateListDrawable createStateListDrawable(int radius, int color)
	{
		StateListDrawable bg = new StateListDrawable();
		GradientDrawable gradientStateNormal = new GradientDrawable();
		gradientStateNormal.setColor(color);
		gradientStateNormal.setShape(GradientDrawable.RECTANGLE);
		gradientStateNormal.setCornerRadius(radius);
		// 设置边框宽度及颜色
		gradientStateNormal.setStroke(0, 0);
		// view无状态（就是正常状态）下的显示设置
		bg.addState(View.EMPTY_STATE_SET, gradientStateNormal);
		return bg;
	}

	/**
	 * 拖拽的事件监听
	 */
	public interface OnDragListener
	{
		/**
		 * 拖拽结束时，执行
		 */
		void onDragOut();
	}
}
