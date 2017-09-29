package com.viewpager.carousel.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.view.viewpager.carousel.R;
import com.yline.utils.UIResizeUtil;

/**
 * 支持定制 + 动态加载图片
 *
 * @author yline 2017/2/14 --> 15:08
 * @version 1.0.0
 */
public class ADWidget
{
	private View parentView;

	private ViewPager viewPager;

	private LinearLayout linearLayout;

	private OnPageListener listener;

	private UserTouchState touchState;

	private Handler handler;

	private int count = 5;

	/**
	 * 唯一的一 个方法
	 */
	public void start(Context context, int count)
	{
		this.count = count;

		parentView = LayoutInflater.from(context).inflate(getLayoutResource(), null);

		LinearLayout linearLayout = (LinearLayout) parentView.findViewById(getPointId());
		initPoint(context, linearLayout);

		ViewPager viewPager = (ViewPager) parentView.findViewById(getViewPagerId());
		initViewPagerView(context, viewPager);

		startAutoRecycle();
	}

	/**
	 * 将当前布局加载到某个布局中
	 *
	 * @param viewGroup
	 */
	public void attach(ViewGroup viewGroup)
	{
		viewGroup.addView(parentView);
	}

	public void setListener(OnPageListener listener)
	{
		this.listener = listener;
	}

	public View getParentView()
	{
		return parentView;
	}

	private void initPoint(Context context, LinearLayout parentLayout)
	{
		this.linearLayout = parentLayout;

		initIndicatorPoint(context, parentLayout, count);
		selectIndicatorPoint(parentLayout, getStartPosition());
	}

	private void initViewPagerView(Context context, ViewPager viewPager)
	{
		this.viewPager = viewPager;

		viewPager.setOffscreenPageLimit(count);
		UIResizeUtil.build().setIsWidthAdapter(false)
				.setWidth(getViewPagerWidth()).setHeight(getViewPagerHeight()).commit(viewPager);

		ViewPagerAdapter adapter = new ViewPagerAdapter(context);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(getStartPosition());

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				// 指示点
				selectIndicatorPoint(linearLayout, position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				// position = isRecycle() ? position % count : position;
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
	}

	/**
	 * 添加指示点
	 *
	 * @param context      本Activity
	 * @param parentLayout 指示点父框体(此处LinearLayout)
	 * @param count        指示点个数
	 */
	private void initIndicatorPoint(Context context, LinearLayout parentLayout, int count)
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.width = getPointSizeBefore();
		layoutParams.height = getPointSizeBefore();
		if (0 != getPointLeftMargin())
		{
			layoutParams.leftMargin = getPointLeftMargin();
		}
		if (0 != getPointRightMargin())
		{
			layoutParams.rightMargin = getPointRightMargin();
		}
		if (0 != getPointTopMargin())
		{
			layoutParams.topMargin = getPointTopMargin();
		}
		if (0 != getPointBottomMargin())
		{
			layoutParams.bottomMargin = getPointBottomMargin();
		}

		for (int i = 0; i < count; i++)
		{
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(getPointResource());
			parentLayout.addView(imageView, layoutParams);
		}
	}

	/**
	 * 指示点状态
	 *
	 * @param parentLayout 指示点父框体(此处LinearLayout)
	 * @param position     当前的指示点
	 */
	private void selectIndicatorPoint(LinearLayout parentLayout, int position)
	{
		position = isRecycle() ? position % count : position;
		for (int i = 0; i < parentLayout.getChildCount(); i++)
		{
			if (i == position)
			{
				UIResizeUtil.build().setIsWidthAdapter(false)
						.setWidth(getPointSizeAfter()).setHeight(getPointSizeAfter())
						.commit(parentLayout.getChildAt(i));
				parentLayout.getChildAt(i).setSelected(true);
			}
			else
			{
				UIResizeUtil.build().setIsWidthAdapter(false)
						.setWidth(getPointSizeBefore()).setHeight(getPointSizeBefore())
						.commit(parentLayout.getChildAt(i));
				parentLayout.getChildAt(i).setSelected(false);
			}
		}
	}

	private class ViewPagerAdapter extends PagerAdapter
	{
		private Context sContext;

		public ViewPagerAdapter(Context context)
		{
			this.sContext = context;
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return (view == object);
		}

		@Override
		public int getCount()
		{
			if (isAutoRecycle())
			{
				return Integer.MAX_VALUE;
			}
			else
			{
				return count;
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			position = isRecycle() ? position % count : position;

			ImageView imageView = new ImageView(sContext);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			if (null != listener)
			{
				listener.onPageInstance(imageView, position);
			}

			container.addView(imageView);

			// viewPager点击事件
			final int index = position;
			imageView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (null != listener)
					{
						listener.onPageClick(v, index);
					}
				}
			});

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public void startUpdate(ViewGroup container)
		{
			super.startUpdate(container);
		}

		@Override
		public void finishUpdate(ViewGroup container)
		{
			super.finishUpdate(container);
			if (isRecycle())
			{
				int position = viewPager.getCurrentItem();
				if (0 == position)
				{
					viewPager.setCurrentItem(count, false);
				}
				else if (position == Integer.MAX_VALUE)
				{
					viewPager.setCurrentItem(position % count, false);
				}
			}
		}
	}

	/**
	 * 开启自动循环播放
	 */
	private void startAutoRecycle()
	{
		if (isRecycle())
		{
			startRecycle();
		}
	}

	/**
	 * 这个必须在ViewPager等初始化完成之后,才能开始
	 */
	private void startRecycle()
	{

		viewPager.setOnTouchListener(new View.OnTouchListener()
		{

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						touchState = UserTouchState.OnMove;
						break;
					case MotionEvent.ACTION_UP:
						touchState = UserTouchState.OnUp;
						break;
					default:
						break;
				}

				return false;
			}
		});

		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				if (touchState == UserTouchState.OnMove)
				{
					handler.sendEmptyMessageDelayed(1, getRecycleAutoTime());
					touchState = UserTouchState.OnAuto;
				}
				else if (touchState == UserTouchState.OnAuto)
				{
					handler.sendEmptyMessageDelayed(1, getRecycleAutoTime());
					autoScroll();
				}
				else
				{
					handler.sendEmptyMessageDelayed(1, getRecycleAutoTime());
					touchState = UserTouchState.OnAuto;
				}
			}
		};
		handler.sendEmptyMessageDelayed(1, getRecycleAutoTime());
	}

	private void autoScroll()
	{
		if (isRecycleRight())
		{
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
		}
		else
		{
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
		}
	}

	// 从这里开始设置参数；这些参数都是可以被重写的
	protected boolean isRecycle()
	{
		return true;
	}

	protected boolean isRecycleRight()
	{
		return true;
	}

	protected boolean isAutoRecycle()
	{
		return true;
	}

	protected int getRecycleAutoTime()
	{
		return 4500;
	}

	protected int getPointSizeAfter()
	{
		return 15;
	}

	protected int getPointSizeBefore()
	{
		return 15;
	}

	protected int getPointLeftMargin()
	{
		return 10;
	}

	protected int getPointRightMargin()
	{
		return 0;
	}

	protected int getPointTopMargin()
	{
		return 0;
	}

	protected int getPointBottomMargin()
	{
		return 0;
	}

	protected int getStartPosition()
	{
		return 0;
	}

	protected int getViewPagerWidth()
	{
		return ViewGroup.LayoutParams.MATCH_PARENT;
	}

	protected int getViewPagerHeight()
	{
		return ViewGroup.LayoutParams.WRAP_CONTENT;
	}

	protected int getPointResource()
	{
		return R.drawable.widget_ad_point;
	}

	protected int getLayoutResource()
	{
		return R.layout.widget_ad;
	}

	protected int getViewPagerId()
	{
		return R.id.viewpager_widget_ad;
	}

	protected int getPointId()
	{
		return R.id.ll_widget_ad;
	}

	private enum UserTouchState
	{
		/**
		 * 用户滑动
		 */
		OnMove,
		/**
		 * 用户松手
		 */
		OnUp,
		/**
		 * 自动播放
		 */
		OnAuto
	}

	/**
	 * viewPager 接口
	 * 图片初始化、图片点击
	 */
	public interface OnPageListener
	{
		/**
		 * @param v        当前控件
		 * @param position 位置(0,开始)
		 */
		void onPageClick(View v, int position);

		/**
		 * 图片初始化
		 *
		 * @param imageView
		 * @param position
		 */
		void onPageInstance(ImageView imageView, int position);
	}
}
