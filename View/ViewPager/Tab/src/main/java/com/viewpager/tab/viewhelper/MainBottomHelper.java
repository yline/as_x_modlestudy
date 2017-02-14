package com.viewpager.tab.viewhelper;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.view.viewpager.tab.R;
import com.yline.utils.third.UIResizeUtil;

public class MainBottomHelper
{
	private static final int colorBefore = Color.BLACK;

	private static final int colorAfter = Color.GREEN;

	private static final int numberOfTab = 4;

	private static final int lineWidth = UIResizeUtil.getDesignWidth() / numberOfTab;

	private ViewHolder viewHolder = new ViewHolder();

	private ViewPager viewPager;

	private OnTabSelectedListener listener;

	public MainBottomHelper()
	{
	}

	public void initViewPagerView(ViewPager viewPager)
	{
		this.viewPager = viewPager;
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				setTextColor(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPx)
			{
				UIResizeUtil.build().setLeftMargin((int) (lineWidth * (position + positionOffset))).commit(viewHolder.lineIv);
			}

			@Override
			public void onPageScrollStateChanged(int position)
			{

			}
		});
	}

	public void initTabView(View parentView)
	{
		initView(parentView);
		initData();
		setTabsClick();
	}

	public void setListener(OnTabSelectedListener listener)
	{
		this.listener = listener;
	}

	private void initView(View view)
	{
		viewHolder.tvOne = (TextView) view.findViewById(R.id.tv_tab_one);
		viewHolder.tvTwo = (TextView) view.findViewById(R.id.tv_tab_two);
		viewHolder.tvThree = (TextView) view.findViewById(R.id.tv_tab_three);
		viewHolder.tvFour = (TextView) view.findViewById(R.id.tv_tab_four);

		viewHolder.llOne = (LinearLayout) view.findViewById(R.id.ll_tab_one);
		viewHolder.llTwo = (LinearLayout) view.findViewById(R.id.ll_tab_two);
		viewHolder.llThree = (LinearLayout) view.findViewById(R.id.ll_tab_three);
		viewHolder.llFour = (LinearLayout) view.findViewById(R.id.ll_tab_four);

		viewHolder.lineIv = (ImageView) view.findViewById(R.id.iv_tab_line);
	}

	private void initData()
	{
		setTextColor(0);

		UIResizeUtil.build().setWidth(lineWidth).setLeftMargin(0 * lineWidth).commit(viewHolder.lineIv);
	}

	/** line 点击事件 */
	private void setTabsClick()
	{
		tabsChangeClickListener tabListener = new tabsChangeClickListener();

		viewHolder.llOne.setOnClickListener(tabListener);
		viewHolder.llOne.setTag(0);
		viewHolder.llTwo.setOnClickListener(tabListener);
		viewHolder.llTwo.setTag(1);
		viewHolder.llThree.setOnClickListener(tabListener);
		viewHolder.llThree.setTag(2);
		viewHolder.llFour.setOnClickListener(tabListener);
		viewHolder.llFour.setTag(3);
	}

	private class tabsChangeClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			int position = (Integer) v.getTag();

			setTextColor(position);    // 改变字体颜色
			UIResizeUtil.build().setLeftMargin(position * lineWidth).commit(viewHolder.lineIv);

			viewPager.setCurrentItem(position);

			if (null != listener)
			{
				listener.onTabSelected(position);
			}
		}
	}

	/**
	 * 设置标签字体颜色
	 * @param position 滑动结束,位置
	 */
	private void setTextColor(int position)
	{
		viewHolder.tvOne.setTextColor(colorBefore);
		viewHolder.tvTwo.setTextColor(colorBefore);
		viewHolder.tvThree.setTextColor(colorBefore);
		viewHolder.tvFour.setTextColor(colorBefore);

		switch (position)
		{
			case 0:
				viewHolder.tvOne.setTextColor(colorAfter);
				break;
			case 1:
				viewHolder.tvTwo.setTextColor(colorAfter);
				break;
			case 2:
				viewHolder.tvThree.setTextColor(colorAfter);
				break;
			case 3:
				viewHolder.tvFour.setTextColor(colorAfter);
				break;
		}
	}

	/**
	 * tab 标签 点击接口
	 */
	public interface OnTabSelectedListener
	{
		/**
		 * 改变标签位置
		 * @param position (0-3)
		 */
		void onTabSelected(int position);
	}

	private class ViewHolder
	{
		private TextView tvOne, tvTwo, tvThree, tvFour;

		private LinearLayout llOne, llTwo, llThree, llFour;

		private ImageView lineIv;
	}
}
