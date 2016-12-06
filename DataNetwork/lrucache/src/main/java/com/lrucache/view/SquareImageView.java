package com.lrucache.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yline on 2016/12/6.
 */
public class SquareImageView extends ImageView
{
	public SquareImageView(Context context)
	{
		super(context);
	}

	public SquareImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
