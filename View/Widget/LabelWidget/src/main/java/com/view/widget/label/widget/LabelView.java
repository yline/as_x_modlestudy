package com.view.widget.label.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * 这个容器是容纳一个子控件
 *
 * @author yline 2017/3/23 -- 16:36
 * @version 1.0.0
 */
public class LabelView extends FrameLayout implements Checkable
{
	private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

	private boolean isChecked;

	public LabelView(Context context)
	{
		super(context);
	}

	public LabelView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public View getLabelView()
	{
		return getChildAt(0);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace)
	{
		int[] states = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked())
		{
			mergeDrawableStates(states, CHECK_STATE);
		}
		return states;
	}

	/**
	 * @return The current checked state of the view
	 */
	@Override
	public boolean isChecked()
	{
		return isChecked;
	}

	/**
	 * Change the checked state of the view
	 *
	 * @param checked The new checked state
	 */
	@Override
	public void setChecked(boolean checked)
	{
		if (this.isChecked != checked)
		{
			this.isChecked = checked;
			refreshDrawableState();
		}
	}

	/**
	 * Change the checked state of the view to the inverse of its current state
	 */
	@Override
	public void toggle()
	{
		setChecked(!isChecked);
	}
}
