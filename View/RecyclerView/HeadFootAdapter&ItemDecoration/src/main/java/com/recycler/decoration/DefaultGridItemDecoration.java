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
		if (-1 != getDividerResourceId())
		{
			mDivider = ContextCompat.getDrawable(context, getDividerResourceId());
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
			if (isHeadDivider(currentPosition))
			{
				break;
			}

			if (isLastDivider(totalCount, currentPosition, spanCount))
			{
				break;
			}

			drawHorizontal(c, child);
			drawVertical(c, child);
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

		if (isHeadDivider(currentPosition))
		{
			return;
		}

		if (isLastDivideDisappear())
		{
			final int queueCount = (totalCount % spanCount == 0) ? (totalCount - spanCount) : (totalCount - totalCount % spanCount);
			if (currentPosition == queueCount + spanCount - 1)
			{
				return;
			}
			else if (currentPosition >= queueCount)
			{
				int orientation = getOrientation(parent);
				// 如果是最后一行，则不需要绘制底部
				if (LinearLayoutManager.VERTICAL == orientation)
				{
					outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
				}
				else if (LinearLayoutManager.HORIZONTAL == orientation) // 如果是最后一列，则不需要绘制右边
				{
					outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
				}
				return;
			}
		}

		int orientation = getOrientation(parent);
		boolean isLast = currentPosition % spanCount == spanCount - 1;
		if (isLast && LinearLayoutManager.VERTICAL == orientation) // 如果是最后一行，则不需要绘制底部
		{
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		}
		else if (isLast && LinearLayoutManager.HORIZONTAL == orientation) // 如果是最后一列，则不需要绘制右边
		{
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
		else
		{
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
		}
	}

	/**
	 * 是否满足，最后一行或一列，分割线，消失的条件
	 *
	 * @param totalCount
	 * @param currentPosition
	 * @param spanCount
	 * @return
	 */
	private boolean isLastDivider(int totalCount, int currentPosition, int spanCount)
	{
		final int queueCount = (totalCount % spanCount == 0) ? (totalCount - spanCount) : (totalCount - totalCount % spanCount);
		if (isLastDivideDisappear() && currentPosition >= queueCount)
		{
			return true;
		}
		return false;
	}

	// 方向,当前item是否是最后一行或列

	/**
	 * 获取 列数或行数
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
	 * 获取方向
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
	 * 是否满足，头部，分割线，消失的条件
	 *
	 * @param currentPosition
	 * @return
	 */
	private boolean isHeadDivider(int currentPosition)
	{
		if (getHeadNumber() > currentPosition)
		{
			return true;
		}

		return false;
	}

	/* ------------------------------------ 提供重写的参数 ---------------------------------------- */

	/**
	 * 最后一个分割线是否绘制
	 *
	 * @return
	 */
	protected boolean isLastDivideDisappear()
	{
		return true;
	}

	/**
	 * 分割线资源
	 *
	 * @return
	 */
	protected int getDividerResourceId()
	{
		return -1;
	}

	/**
	 * 添加的头部的数量
	 *
	 * @return
	 */
	protected int getHeadNumber()
	{
		return 0;
	}
}
