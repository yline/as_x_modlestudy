package com.viewgroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.viewgroup.R;

/**
 * @author yline 2017/2/20 --> 18:49
 * @version 1.0.0
 */
public class TagLayout extends ViewGroup
{
	private BaseAdapter mAdapter;

	private TagBean tagBean;
	
	private OnTagItemClickListener mListener;
	
	private DataChangeObserver mObserver;
	
	public TagLayout(Context context)
	{
		this(context, null);
	}
	
	public TagLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public TagLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		tagBean = new TagBean(context, attrs);
	}

	private void drawLayout()
	{
		if (mAdapter == null || mAdapter.getCount() == 0)
		{
			return;
		}
		
		this.removeAllViews();
		
		for (int i = 0; i < mAdapter.getCount(); i++)
		{
			View view = mAdapter.getView(i, null, null);
			final int position = i;
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (mListener != null)
					{
						mListener.onClick(position);
					}
				}
			});
			this.addView(view);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int wantHeight = 0;
		int wantWidth = resolveSize(0, widthMeasureSpec);
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();
		int childLeft = paddingLeft;
		int childTop = paddingTop;
		int lineHeight = 0;
		
		//TODO 固定列的数量所需要的代码
		for (int i = 0; i < getChildCount(); i++)
		{
			final View childView = getChildAt(i);
			LayoutParams params = childView.getLayoutParams();
			childView.measure(
					getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, params.width),
					getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, params.height)
			);
			int childHeight = childView.getMeasuredHeight();
			int childWidth = childView.getMeasuredWidth();
			lineHeight = Math.max(childHeight, lineHeight);
			
			if (childLeft + childWidth + paddingRight > wantWidth)
			{
				childLeft = paddingLeft;
				childTop += tagBean.lineSpacing + childHeight;
				lineHeight = childHeight;
			}
			
			childLeft += childWidth + tagBean.tagSpacing;
		}
		wantHeight += childTop + lineHeight + paddingBottom;
		setMeasuredDimension(wantWidth, resolveSize(wantHeight, heightMeasureSpec));
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		// TODO 固定列的数量所需要的代码
		
		int width = r - l;
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int childLeft = paddingLeft;
		int childTop = paddingTop;
		int lineHeight = 0;
		
		for (int i = 0; i < getChildCount(); i++)
		{
			final View childView = getChildAt(i);
			if (childView.getVisibility() == View.GONE)
			{
				continue;
			}
			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();
			lineHeight = Math.max(childHeight, lineHeight);
			
			if (childLeft + childWidth + paddingRight > width)
			{
				childLeft = paddingLeft;
				childTop += tagBean.lineSpacing + lineHeight;
				lineHeight = childHeight;
			}
			
			childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
			childLeft += childWidth + tagBean.tagSpacing;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new LayoutParams(this.getContext(), attrs);
	}
	
	public void setAdapter(BaseAdapter adapter)
	{
		if (mAdapter == null)
		{
			mAdapter = adapter;
			if (mObserver == null)
			{
				mObserver = new DataChangeObserver();
				mAdapter.registerDataSetObserver(mObserver);
			}
			drawLayout();
		}
	}
	
	public void setItemClickListener(OnTagItemClickListener mListener)
	{
		this.mListener = mListener;
	}
	
	public interface OnTagItemClickListener
	{
		void onClick(int position);
	}

	/** 观察者 */
	private class DataChangeObserver extends DataSetObserver
	{
		@Override
		public void onChanged()
		{
			TagLayout.this.drawLayout();
		}
		
		@Override
		public void onInvalidated()
		{
			super.onInvalidated();
		}
	}

	private class TagBean
	{
		private static final int DEFAULT_LINE_SPACING = 5;

		private static final int DEFAULT_TAG_SPACING = 10;

		private static final int DEFAULT_FIXED_COLUMN_SIZE = 3; //默认列数

		private int lineSpacing = DEFAULT_LINE_SPACING;

		private int tagSpacing = DEFAULT_TAG_SPACING;

		private int columnSize = DEFAULT_FIXED_COLUMN_SIZE;

		private boolean isFixed = false;

		public TagBean(Context context, AttributeSet attrs)
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagCloudLayout);
			lineSpacing = a.getDimensionPixelSize(R.styleable.TagCloudLayout_lineSpacing, DEFAULT_LINE_SPACING);
			tagSpacing = a.getDimensionPixelSize(R.styleable.TagCloudLayout_tagSpacing, DEFAULT_TAG_SPACING);
			columnSize = a.getInteger(R.styleable.TagCloudLayout_columnSize, DEFAULT_FIXED_COLUMN_SIZE);
			isFixed = a.getBoolean(R.styleable.TagCloudLayout_isFixed, false);
			a.recycle();
		}
	}
}
