package com.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class DefaultGridItemDecoration extends RecyclerView.ItemDecoration
{
	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	private Drawable mDivider;

	public DefaultGridItemDecoration(Context context)
	{
		if (-1 != getDivideResourceId())
		{
			mDivider = ContextCompat.getDrawable(context, getDivideResourceId());
		}
		else
		{
			final TypedArray a = context.obtainStyledAttributes(ATTRS);
			mDivider = a.getDrawable(0);
			a.recycle();
		}
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
	{
		final int childCount = parent.getChildCount();
		final int totalCount = parent.getAdapter().getItemCount();
		int spanCount = getSpanCount(parent);

		int currentPosition;
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);

			currentPosition = parent.getChildAdapterPosition(child);
			if (isDrawDivideLine(totalCount, currentPosition))
			{
				if (isLastDivideLine(totalCount, currentPosition, spanCount) && !isDivideLastLine())
				{
					int orientation = getOrientation(parent);
					if (orientation == LinearLayoutManager.VERTICAL)
					{
						drawVertical(c, child);
					}
					else
					{
						drawHorizontal(c, child);
					}
				}
				else
				{
					drawHorizontal(c, child);
					drawVertical(c, child);
				}
			}
		}
	}

	private void drawHorizontal(Canvas c, View child)
	{
		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
		final int top = child.getBottom() + params.bottomMargin;
		final int bottom = top + mDivider.getIntrinsicHeight();
		final int left = child.getLeft() - params.leftMargin;
		final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
		mDivider.setBounds(left, top, right, bottom);
		mDivider.draw(c);
	}

	private void drawVertical(Canvas c, View child)
	{
		final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
		final int top = child.getTop() - params.topMargin;
		final int bottom = child.getBottom() + params.bottomMargin;
		final int left = child.getRight() + params.rightMargin;
		final int right = left + mDivider.getIntrinsicWidth();
		mDivider.setBounds(left, top, right, bottom);
		mDivider.draw(c);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);
		int spanCount = getSpanCount(parent);
		int totalCount = parent.getAdapter().getItemCount();

		int currentPosition = parent.getChildAdapterPosition(view);
		if (isDrawDivideLine(totalCount, currentPosition))
		{
			int orientation = getOrientation(parent);
			boolean isLastDivideLine = isLastDivideLine(totalCount, currentPosition, spanCount);

			currentPosition -= getHeadNumber();
			boolean isLastSpan = currentPosition % spanCount == spanCount - 1; // 判断是否是 最后一个span

			if (isLastDivideLine && !isDivideLastLine())
			{
				if (!isLastSpan)
				{
					if (LinearLayoutManager.VERTICAL == orientation)
					{
						outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
					}
					else if (LinearLayoutManager.HORIZONTAL == orientation)
					{
						outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
					}
				}
			}
			else
			{
				if (isLastSpan)
				{
					if (LinearLayoutManager.VERTICAL == orientation)
					{
						outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
					}
					else if (LinearLayoutManager.HORIZONTAL == orientation)
					{
						outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
					}
				}
				else
				{
					outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
				}
			}
		}
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 工具类 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

	/**
	 * 获取行数 或 列数
	 *
	 * @param parent
	 * @return
	 */
	private int getSpanCount(RecyclerView parent)
	{
		// 列数
		int spanCount = -1;
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{
			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		}
		else if (layoutManager instanceof StaggeredGridLayoutManager)
		{
			spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		}
		return spanCount;
	}

	/**
	 * 获取当前控件的方向
	 *
	 * @param parent
	 * @return
	 */
	private int getOrientation(RecyclerView parent)
	{
		int orientation = -1;
		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager)
		{
			orientation = ((GridLayoutManager) layoutManager).getOrientation();
		}
		else if (layoutManager instanceof StaggeredGridLayoutManager)
		{
			orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
		}
		return orientation;
	}

	/**
	 * 判断是否绘制分割线（仅仅头部和底部）
	 *
	 * @param totalCount
	 * @param currentPosition
	 * @return
	 */
	private boolean isDrawDivideLine(int totalCount, int currentPosition)
	{
		// 头部
		if (currentPosition < getHeadNumber() && !isDivideHead())
		{
			return false;
		}

		// 底部
		if (currentPosition > totalCount - 1 - getFootNumber() && !isDivideFoot())
		{
			return false;
		}

		return true;
	}

	/**
	 * 判断是否是最后一行 或 一列
	 *
	 * @param totalCount      总数
	 * @param currentPosition 当前位置
	 * @param spanCount       行数 或者 列数
	 * @return
	 */
	private boolean isLastDivideLine(int totalCount, int currentPosition, int spanCount)
	{
		int tempTotalCount = (totalCount - getFootNumber() - getHeadNumber()) / spanCount * spanCount + spanCount; // 整除
		currentPosition -= getHeadNumber();

		if (currentPosition >= tempTotalCount - spanCount && currentPosition < tempTotalCount)
		{
			return true;
		}

		return false;
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 提供重写的参数 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

	/**
	 * 分割线资源
	 *
	 * @return
	 */
	protected int getDivideResourceId()
	{
		return -1;
	}

	/**
	 * 头部的数量
	 *
	 * @return
	 */
	protected int getHeadNumber()
	{
		return 0;
	}

	/**
	 * 头部是否有分割线
	 *
	 * @return
	 */
	protected boolean isDivideHead()
	{
		return false;
	}

	/**
	 * 底部的数量
	 *
	 * @return
	 */
	protected int getFootNumber()
	{
		return 0;
	}

	/**
	 * 底部是否有分割线
	 *
	 * @return
	 */
	protected boolean isDivideFoot()
	{
		return false;
	}

	/**
	 * 最后一个分割线是否分割
	 *
	 * @return
	 */
	protected boolean isDivideLastLine()
	{
		return false;
	}
}
