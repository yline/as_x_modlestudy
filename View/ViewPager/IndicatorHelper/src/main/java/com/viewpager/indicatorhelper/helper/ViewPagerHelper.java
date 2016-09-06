package com.viewpager.indicatorhelper.helper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.viewpager.indicatorhelper.R;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerHelper implements OnPageChangeListener
{
	private static final String TAG = "ViewPagerHelper";

	private Context context;

	private ViewPager viewPager;

	private ViewPagerAdapter viewPagerAdapter;

	private LinearLayout llIndicator;

	private IndicatorHelper llIndicatorHelper = new IndicatorHelper();

	/** 默认进入时,第一个为选中图标 */
	private int currentIndex = 0;

	public ViewPagerHelper(Context context, ViewPager viewPager)
	{
		this.context = context;
		this.viewPager = viewPager;
	}

	public ViewPagerHelper setIndicator(LinearLayout llIndicator)
	{
		this.llIndicator = llIndicator;
		return this;
	}

	/** 真正创建ViewPager加载器 */
	public void create()
	{
		LogFileUtil.v(TAG, "have create");
		viewPagerAdapter = new ViewPagerAdapter(context);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	/** 新增一个页面 */
	public void addView(View view)
	{
		LogFileUtil.v(TAG, "addView");
		viewPagerAdapter.addView(view);
		viewPagerAdapter.notifyDataSetChanged();

		llIndicatorHelper.add(llIndicator);
		llIndicatorHelper.updateIndicator(llIndicator, viewPagerAdapter.getCount(), currentIndex);
	}

	/** 新增一个页面 */
	public void addView(int position, View view)
	{
		LogFileUtil.v(TAG, "addView with position");
		viewPagerAdapter.addView(position, view);
		viewPagerAdapter.notifyDataSetChanged();

		llIndicatorHelper.add(llIndicator, position);
		llIndicatorHelper.updateIndicator(llIndicator, viewPagerAdapter.getCount(), currentIndex);
	}

	/** 移除指定页面 */
	public void removeView(int position)
	{
		LogFileUtil.v(TAG, "removeView with position");
		viewPagerAdapter.remove(position);
		viewPagerAdapter.notifyDataSetChanged();

		llIndicatorHelper.remove(llIndicator, position);
		int index = currentIndex > viewPagerAdapter.getCount() - 1 ? viewPagerAdapter.getCount() - 1 : currentIndex;
		llIndicatorHelper.updateIndicator(llIndicator, viewPagerAdapter.getCount(), index);
	}

	/** 返回当前页面数目 */
	public int getCount()
	{
		return viewPagerAdapter.getCount();
	}

	/**
	 * Adapter for ViewPager
	 */
	private class ViewPagerAdapter extends PagerAdapter
	{
		// private Context context;
		private List<View> viewList;

		public ViewPagerAdapter(Context context)
		{
			// this.context = context;
			viewList = new ArrayList<View>();
		}

		@Override
		public int getCount()
		{
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position)
		{
			View view = viewList.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
			// super.destroyItem(container, position, object); 重写后,父类方法就不支持了
		}

		/**
		 * 设置成每次刷新页面,解决重叠的bug
		 *
		 * @see PagerAdapter#getItemPosition(Object)
		 */
		@Override
		public int getItemPosition(Object object)
		{
			return PagerAdapter.POSITION_NONE;
		}

		public void addView(View view)
		{
			this.viewList.add(view);
		}

		public void addView(int position, View view)
		{
			this.viewList.add(position, view);
		}

		public void remove(int position)
		{
			this.viewList.remove(position);
		}
	}

	/**
	 * Indicator for ViewPager
	 */
	private class IndicatorHelper
	{
		/**
		 * 新增一个指示点
		 *
		 * @param indicator
		 */
		private void add(LinearLayout indicator)
		{
			// 判断是否放入了值
			if (null == indicator)
			{
				LogFileUtil.e(TAG, "IndicatorHelper indicator is null");
				return;
			}

			ImageView tempViewPagerIndicator = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
			params.setMargins(10, 0, 10, 0);
			tempViewPagerIndicator.setLayoutParams(params);
			indicator.addView(tempViewPagerIndicator);
		}

		/**
		 * 新增一个指示点
		 *
		 * @param indicator
		 */
		private void add(LinearLayout indicator, int position)
		{
			// 判断是否放入了值
			if (null == indicator)
			{
				LogFileUtil.e(TAG, "IndicatorHelper indicator is null");
				return;
			}

			ImageView tempViewPagerIndicator = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
			params.setMargins(10, 0, 10, 0);
			tempViewPagerIndicator.setLayoutParams(params);
			indicator.addView(tempViewPagerIndicator, position);
		}

		/**
		 * 移除一个指示器
		 *
		 * @param indicator
		 * @param position
		 */
		private void remove(LinearLayout indicator, int position)
		{
			// 判断是否放入了值
			if (null == indicator)
			{
				LogFileUtil.e(TAG, "IndicatorHelper remove is null");
				return;
			}

			if (indicator.getChildCount() > position)
			{
				indicator.removeViewAt(position);
			}
			else
			{
				LogFileUtil.e(TAG, "IndicatorHelper remove index of bound");
			}
		}

		/**
		 * 更新显示的指示点
		 *
		 * @param layout     容器本身
		 * @param count      当前指示点个数
		 * @param focusIndex 当前显示的位置
		 */
		private void updateIndicator(LinearLayout layout, int count, int focusIndex)
		{
			// 判断是否放入了值
			if (null == layout)
			{
				LogFileUtil.e(TAG, "updateIndicator indicator is null");
				return;
			}

			if (layout.getChildCount() < count)
			{
				LogFileUtil.e(TAG, "updateIndicator index of bound");
				return;
			}

			for (int i = 0; i < count; i++)
			{
				if (i == focusIndex)
				{
					layout.getChildAt(i).setScaleX(1.5f);
					layout.getChildAt(i).setScaleY(1.5f);
					layout.getChildAt(i).setBackgroundResource(R.drawable.bg_app_indicator_focus);
					LogFileUtil.v(TAG, "updateAnyViewPagerIndicator count = " + count + ",focusIndex = " + focusIndex);
				}
				else
				{
					layout.getChildAt(i).setScaleX(1.0f);
					layout.getChildAt(i).setScaleY(1.0f);
					layout.getChildAt(i).setBackgroundResource(R.drawable.bg_app_indicator_default);
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int position)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		currentIndex = position;
		llIndicatorHelper.updateIndicator(llIndicator, viewPagerAdapter.getCount(), currentIndex);
	}
}
