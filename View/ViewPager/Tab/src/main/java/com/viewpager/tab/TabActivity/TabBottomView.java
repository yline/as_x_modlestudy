package com.viewpager.tab.TabActivity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.view.viewpager.tab.R;
import com.yline.application.SDKManager;
import com.yline.utils.UIResizeUtil;
import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.holder.ViewHolder;

public class TabBottomView extends RelativeLayout {
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int THIRD = 2;
	public static final int FOUR = 3;
	
	public static final int COUNT = 4;
	
	private static final int colorBefore = Color.BLACK;
	private static final int colorAfter = Color.GREEN;
	
	private final int lineWidth;
	
	private ViewPager viewPager;
	private ViewHolder mViewHolder;
	
	public TabBottomView(Context context) {
		this(context, null);
	}
	
	public TabBottomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.tab_bottom_view, this, true);
		lineWidth = UIScreenUtil.getScreenWidth(SDKManager.getApplication()) / COUNT;
		
		initView(this);
	}
	
	public void initViewPagerView(ViewPager viewPager) {
		this.viewPager = viewPager;
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				updateTextView(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
				updateLineView(position + positionOffset);
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
			
			}
		});
	}
	
	private void initView(View view) {
		mViewHolder = new ViewHolder(view);
		initViewClick();
		
		initData();
	}
	
	private void initData() {
		updateTextView(FIRST);
		updateLineView(FIRST);
	}
	
	/**
	 * line 点击事件
	 */
	private void initViewClick() {
		mViewHolder.setOnClickListener(R.id.tab_bottom_one, new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateTextView(FIRST);
				updateLineView(FIRST);
				viewPager.setCurrentItem(FIRST);
			}
		});
		mViewHolder.setOnClickListener(R.id.tab_bottom_two, new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateTextView(SECOND);
				updateLineView(SECOND);
				viewPager.setCurrentItem(SECOND);
			}
		});
		mViewHolder.setOnClickListener(R.id.tab_bottom_three, new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateTextView(THIRD);
				updateLineView(THIRD);
				viewPager.setCurrentItem(THIRD);
			}
		});
		mViewHolder.setOnClickListener(R.id.tab_bottom_four, new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateTextView(FOUR);
				updateLineView(FOUR);
				viewPager.setCurrentItem(FOUR);
			}
		});
	}
	
	/**
	 * 改变 线条颜色
	 *
	 * @param position 位置
	 */
	private void updateLineView(float position) {
		View lineView = mViewHolder.get(R.id.tab_bottom_line);
		UIResizeUtil.build().setWidth(lineWidth).setLeftMargin((int) (position * lineWidth)).commit(lineView);
	}
	
	/**
	 * 改变 字体颜色
	 *
	 * @param position 位置
	 */
	private void updateTextView(int position) {
		((TextView) mViewHolder.get(R.id.tab_bottom_one_text)).setTextColor(colorBefore);
		((TextView) mViewHolder.get(R.id.tab_bottom_two_text)).setTextColor(colorBefore);
		((TextView) mViewHolder.get(R.id.tab_bottom_three_text)).setTextColor(colorBefore);
		((TextView) mViewHolder.get(R.id.tab_bottom_four_text)).setTextColor(colorBefore);
		
		switch (position) {
			case FIRST:
				((TextView) mViewHolder.get(R.id.tab_bottom_one_text)).setTextColor(colorAfter);
				break;
			case SECOND:
				((TextView) mViewHolder.get(R.id.tab_bottom_two_text)).setTextColor(colorAfter);
				break;
			case THIRD:
				((TextView) mViewHolder.get(R.id.tab_bottom_three_text)).setTextColor(colorAfter);
				break;
			case FOUR:
				((TextView) mViewHolder.get(R.id.tab_bottom_four_text)).setTextColor(colorAfter);
				break;
			default:
				break;
		}
	}
}
