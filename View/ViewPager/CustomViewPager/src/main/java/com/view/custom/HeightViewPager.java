package com.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 恒定高度的ViewPager
 * @author yline 2016/10/6 --> 14:33
 * @version 1.0.0
 */
public class HeightViewPager extends ViewPager
{
	private boolean isWrapContent = true;

	private int childHeight = 0;

	public HeightViewPager(Context context)
	{
		this(context, null);
	}

	public HeightViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (isWrapContent)
		{
			// 获取最高高度
			for (int i = 0; i < getChildCount(); i++)
			{
				View child = getChildAt(i);
				child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED));
				int h = child.getMeasuredHeight();
				if (h > childHeight)
					childHeight = h;
			}
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
