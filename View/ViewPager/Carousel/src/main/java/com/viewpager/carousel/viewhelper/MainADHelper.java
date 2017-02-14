package com.viewpager.carousel.viewhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.view.viewpager.carousel.R;
import com.viewpager.carousel.activity.MainApplication;
import com.yline.log.LogFileUtil;
import com.yline.utils.third.DensityUtil;
import com.yline.utils.third.UIResizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yline 2017/2/14 --> 18:07
 * @version 1.0.0
 */
public class MainADHelper
{
	private Context context;

	private ViewPager viewPager;
	
	private LinearLayout linearLayout;

	private OnPageClickListener listener;
	
	private ParamHolder param;
	
	private UserTouchState touchState;

	private Handler handler;

	public ParamHolder build()
	{
		param = new ParamHolder();
		param.init();
		return param;
	}
	
	public void initPoint(LinearLayout parentLayout)
	{
		if (null != param && null != context)
		{
			this.linearLayout = parentLayout;
			
			initIndicatorPoint(context, parentLayout, param.resource.size());
			selectIndicatorPoint(parentLayout, param.startPosition);
		}
		else
		{
			LogFileUtil.v("the param or context is null");
		}
	}
	
	public void initViewPagerView(ViewPager viewPager)
	{
		if (null != param && null != context)
		{
			this.viewPager = viewPager;

			viewPager.setOffscreenPageLimit(param.resource.size());
			UIResizeUtil.build().setIsWidthAdapter(false)
					.setWidth(param.viewPagerWidth).setHeight(param.viewPagerHeight).commit(viewPager);

			ViewPagerAdapter adapter = new ViewPagerAdapter();
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(param.startPosition);

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
					// position = param.isRecycle ? position % param.resource.size() : position;
				}

				@Override
				public void onPageScrollStateChanged(int state)
				{

				}
			});
		}
		else
		{
			LogFileUtil.v("the param or context is null");
		}
	}

	public void setListener(OnPageClickListener listener)
	{
		this.listener = listener;
	}

	/**
	 * 添加指示点
	 * @param context      本Activity
	 * @param parentLayout 指示点父框体(此处LinearLayout)
	 * @param count        指示点个数
	 */
	private void initIndicatorPoint(Context context, LinearLayout parentLayout, int count)
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);

		layoutParams.width = param.pointSizeBefore;
		layoutParams.height = param.pointSizeBefore;
		if (0 != param.pointLeftMargin)
		{
			layoutParams.leftMargin = param.pointLeftMargin;
		}
		if (0 != param.pointRightMargin)
		{
			layoutParams.rightMargin = param.pointRightMargin;
		}
		if (0 != param.pointTopMargin)
		{
			layoutParams.topMargin = param.pointTopMargin;
		}
		if (0 != param.pointBottomMargin)
		{
			layoutParams.bottomMargin = param.pointBottomMargin;
		}
		
		for (int i = 0; i < count; i++)
		{
			View view = new View(context);
			view.setBackgroundResource(param.pointResource);
			parentLayout.addView(view, layoutParams);
		}
	}
	
	/**
	 * 指示点状态
	 * @param parentLayout 指示点父框体(此处LinearLayout)
	 * @param position     当前的指示点
	 */
	private void selectIndicatorPoint(LinearLayout parentLayout, int position)
	{
		position = param.isRecycle ? position % param.resource.size() : position;
		for (int i = 0; i < parentLayout.getChildCount(); i++)
		{
			if (i == position)
			{
				parentLayout.getChildAt(i).setSelected(true);
				UIResizeUtil.build().setIsWidthAdapter(false)
						.setWidth(param.pointSizeAfter).setHeight(param.pointSizeAfter)
						.commit(parentLayout.getChildAt(i));
			}
			else
			{
				parentLayout.getChildAt(i).setSelected(false);
				UIResizeUtil.build().setIsWidthAdapter(false)
						.setWidth(param.pointSizeBefore).setHeight(param.pointSizeBefore)
						.commit(parentLayout.getChildAt(i));
			}
		}
	}

	private class ViewPagerAdapter extends PagerAdapter
	{
		
		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return (view == object);
		}
		
		@Override
		public int getCount()
		{
			int count = param.resource.size();
			if (param.isAutoRecycle)
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
			int count = param.resource.size();
			position = param.isRecycle ? position % count : position;
			
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(param.resource.get(position));
			
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
						listener.onPagerClick(v, index);
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
			if (param.isRecycle)
			{
				int position = viewPager.getCurrentItem();
				if (0 == position)
				{
					viewPager.setCurrentItem(param.resource.size(), false);
				}
				else if (position == Integer.MAX_VALUE)
				{
					viewPager.setCurrentItem(position % param.resource.size(), false);
				}
			}
		}
	}

	/**
	 * 开启自动循环播放
	 */
	public void startAutoRecycle()
	{
		if (null != param && null != context && null != viewPager)
		{
			if (param.isRecycle)
			{
				startRecycle();
			}
		}
	}

	/** 这个必须在ViewPager等初始化完成之后,才能开始 */
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
					handler.sendEmptyMessageDelayed(1, param.recycleAutoTime);
					touchState = UserTouchState.OnAuto;
				}
				else if (touchState == UserTouchState.OnAuto)
				{
					handler.sendEmptyMessageDelayed(1, param.recycleAutoTime);
					autoScroll();
				}
				else
				{
					handler.sendEmptyMessageDelayed(1, param.recycleAutoTime);
					touchState = UserTouchState.OnAuto;
				}
			}
		};
		handler.sendEmptyMessageDelayed(1, param.recycleAutoTime);
	}

	private void autoScroll()
	{
		if (param.isRecycleRight)
		{
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
		}
		else
		{
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
		}
	}

	public class ParamHolder
	{
		// viewPager
		private int viewPagerWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		
		private int viewPagerHeight;
		
		// point
		private int pointSizeBefore = 15;
		
		private int pointSizeAfter = 15;
		
		private int pointLeftMargin = 15;
		
		private int pointRightMargin = 0;
		
		private int pointTopMargin = 0;
		
		private int pointBottomMargin = 0;
		
		// 资源
		private List<Integer> resource;
		
		private int pointResource;
		
		// 循环设置
		private boolean isRecycle = true; // 是否轮回
		
		private boolean isAutoRecycle = true; // 是否自动播放(必须轮回才能自动播放)
		
		private boolean isRecycleRight = true; // 向右滑动
		
		private int recycleAutoTime = 5500; // 自动播放
		
		// 起点设置
		private int startPosition = 0;
		
		private void init()
		{
			viewPagerHeight = DensityUtil.dp2px(MainApplication.getApplication(), 180);
			
			resource = new ArrayList<>();
			resource.add(R.mipmap.ic_launcher);
			resource.add(R.mipmap.ic_launcher);
			pointResource = R.drawable.point_main_id;
		}
		
		public ParamHolder setViewPagerWidth(int viewPagerWidth)
		{
			this.viewPagerWidth = viewPagerWidth;
			return this;
		}
		
		public ParamHolder setViewPagerHeight(int viewPagerHeight)
		{
			this.viewPagerHeight = viewPagerHeight;
			return this;
		}
		
		public ParamHolder setPointSizeBefore(int pointSizeBefore)
		{
			this.pointSizeBefore = pointSizeBefore;
			return this;
		}
		
		public ParamHolder setPointSizeAfter(int pointSizeAfter)
		{
			this.pointSizeAfter = pointSizeAfter;
			return this;
		}
		
		public ParamHolder setPointLeftMargin(int pointLeftMargin)
		{
			this.pointLeftMargin = pointLeftMargin;
			return this;
		}
		
		public ParamHolder setPointRightMargin(int pointRightMargin)
		{
			this.pointRightMargin = pointRightMargin;
			return this;
		}
		
		public ParamHolder setPointTopMargin(int pointTopMargin)
		{
			this.pointTopMargin = pointTopMargin;
			return this;
		}
		
		public ParamHolder setPointBottomMargin(int pointBottomMargin)
		{
			this.pointBottomMargin = pointBottomMargin;
			return this;
		}
		
		public ParamHolder setResource(List<Integer> resource)
		{
			this.resource.clear();
			this.resource = resource;
			return this;
		}
		
		public ParamHolder setPointResource(int pointResource)
		{
			this.pointResource = pointResource;
			return this;
		}
		
		public ParamHolder setRecycle(boolean recycle)
		{
			this.isRecycle = recycle;
			return this;
		}
		
		public ParamHolder setAutoRecycle(boolean autoRecycle)
		{
			this.isAutoRecycle = autoRecycle;
			return this;
		}

		/**
		 * 如果设置成false,则将起始点设置为:Integer.MAX_VALUE;
		 * @param recycleRight
		 * @return
		 */
		public ParamHolder setRecycleRight(boolean recycleRight)
		{
			this.isRecycleRight = recycleRight;
			if (!recycleRight)
			{
				startPosition = 465585120 - 1; // (该值等于 1~21 的公倍数)
			}
			return this;
		}
		
		public ParamHolder setRecycleAutoTime(int recycleAutoTime)
		{
			this.recycleAutoTime = recycleAutoTime;
			return this;
		}

		public ParamHolder setStartPosition(int startPosition)
		{
			this.startPosition = startPosition;
			return this;
		}
		
		/**
		 * 完成对参数的校验;
		 * PS：暂时没啥具体用途
		 */
		public void commit(Context cxt)
		{
			context = cxt;
			LogFileUtil.v("param and context init over");
		}
	}
	
	private enum UserTouchState
	{
		/** 用户滑动 */
		OnMove,
		/** 用户松手 */
		OnUp,
		/** 自动播放 */
		OnAuto
	}
	
	/**
	 * viewPager 点击接口
	 */
	public interface OnPageClickListener
	{
		/**
		 * @param v        当前控件
		 * @param position 位置(0,开始)
		 */
		void onPagerClick(View v, int position);
	}
}
