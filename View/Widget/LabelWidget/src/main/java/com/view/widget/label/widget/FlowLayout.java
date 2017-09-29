package com.view.widget.label.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.view.widget.label.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 流动布局
 * 缕过了逻辑
 *
 * @author yline 2017/4/28 -- 13:52
 * @version 1.0.0
 */
public class FlowLayout extends ViewGroup
{
	private int gravity;

	private int maxCountEachLine;

	public FlowLayout(Context context)
	{
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewLabel);
		gravity = ta.getInt(R.styleable.ViewLabel_label_gravity, LabelGravity.LEFT.value);
		maxCountEachLine = ta.getInt(R.styleable.ViewLabel_label_max_count_each_line, -1);
		ta.recycle();
	}

	public void setLabelGravity(LabelGravity gravity)
	{
		this.gravity = gravity.value;
	}

	public void setMaxCountEachLine(int maxCountEachLine)
	{
		this.maxCountEachLine = maxCountEachLine;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// wrap_content, 总高度
		int width = 0;
		int height = 0;

		// 临时的宽度和高度，考虑对齐的问题
		int lineWidth = 0;
		int lineHeight = 0;

		int cCount = getChildCount();
		int tempCountEachLine = 0;
		for (int i = 0; i < cCount; i++)
		{
			View child = getChildAt(i);
			if (child.getVisibility() == View.GONE)  // 如果，view不可见, 则跳过
			{
				continue;
			}

			// 计算大小，获取子控件的宽高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
			int childWidth = child.getMeasuredWidth() + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
			int childHeight = child.getMeasuredHeight() + childLayoutParams.topMargin + childLayoutParams.bottomMargin;

			// 如果线宽 + 子控件宽度，大于剩余宽度，则换行； 否则，依序排列
			if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight())
			{
				width = Math.max(width, lineWidth);
				lineWidth = childWidth;

				height += lineHeight;
				lineHeight = childHeight;

				tempCountEachLine = 1;
			}
			else if (tempCountEachLine == maxCountEachLine) // 如果超过个数，换行
			{
				width = Math.max(width, lineWidth);
				lineWidth = childWidth;

				height += lineHeight;
				lineHeight = childHeight;

				tempCountEachLine = 1;
			}
			else // 正常计算
			{
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);

				tempCountEachLine += 1;
			}

			// 最后一个时，调整一个高度
			if (i == cCount - 1)
			{
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}

		int setWidth = (modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width + getPaddingLeft() + getPaddingRight();
		int setHeight = (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height + getPaddingTop() + getPaddingBottom();
		setMeasuredDimension(setWidth, setHeight);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		List<List<View>> allViewList = new ArrayList<>();
		List<Integer> lineHeightList = new ArrayList<>();
		List<Integer> lineWidthList = new ArrayList<>();
		List<View> lineViewList = new ArrayList<>();

		int width = getWidth();

		int lineWidth = 0;
		int lineHeight = 0;

		int cCount = getChildCount();
		int tempCountEachLine = 0;
		for (int i = 0; i < cCount; i++)
		{
			View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) // 如果视图不可见，则跳过
			{
				continue;
			}
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

			// 获取大小
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// 满足换行条件，则换行
			if ((childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) || tempCountEachLine == maxCountEachLine)
			{
				lineHeightList.add(lineHeight);
				allViewList.add(lineViewList);
				lineWidthList.add(lineWidth);

				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				lineViewList = new ArrayList<>();

				tempCountEachLine = 0;
			}

			tempCountEachLine += 1;
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
			lineViewList.add(child);
		}
		lineHeightList.add(lineHeight);
		lineWidthList.add(lineWidth);
		allViewList.add(lineViewList);

		int left = getPaddingLeft();
		int top = getPaddingTop();
		int lineNum = allViewList.size();
		int equalDistance = 0;
		for (int i = 0; i < lineNum; i++)
		{
			lineViewList = allViewList.get(i);
			lineHeight = lineHeightList.get(i);

			// set gravity
			int currentLineWidth = lineWidthList.get(i); // 总长度
			if (this.gravity == LabelGravity.LEFT.value)
			{
				left = getPaddingLeft();
			}
			else if (this.gravity == LabelGravity.CENTER.value)
			{
				left = (width - currentLineWidth) / 2 + getPaddingLeft();
			}
			else if (this.gravity == LabelGravity.EQUIDISTANT.value)
			{
				left = getPaddingLeft();
				if (maxCountEachLine - 1 > 0 && equalDistance == 0)
				{
					equalDistance = (width - currentLineWidth) / (maxCountEachLine - 1);
				}
			}
			else
			{
				left = width - currentLineWidth + getPaddingLeft();
			}

			for (int j = 0; j < lineViewList.size(); j++)
			{
				View child = lineViewList.get(j);
				if (child.getVisibility() == View.GONE)
				{
					continue;
				}

				MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				child.layout(lc, tc, rc, bc);
				left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + equalDistance;
			}
			top += lineHeight;
		}
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams()
	{
		return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p)
	{
		return new MarginLayoutParams(p);
	}

	public enum LabelGravity
	{
		LEFT(-1), CENTER(0), EQUIDISTANT(1), RIGHT(2);

		private final int value;

		LabelGravity(int value)
		{
			this.value = value;
		}
	}
}
