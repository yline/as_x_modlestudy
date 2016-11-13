package com.view.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by yline on 2016/11/14.
 */
public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder
{
	private SparseArray<View> sArray;

	public CommonRecyclerViewHolder(View itemView)
	{
		super(itemView);
		sArray = new SparseArray<View>();
	}

	public CommonRecyclerViewHolder setText(int viewId, String content)
	{
		TextView textView = this.get(viewId);
		textView.setText(content);
		return this;
	}

	public CommonRecyclerViewHolder setLayoutMargins(int viewId, int left, int top, int right, int bottom)
	{
		ViewGroup viewGroup = this.get(viewId);
		if (viewGroup.getParent() instanceof LinearLayout)
		{
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
			params.setMargins(left, top, right, bottom);
			viewGroup.setLayoutParams(params);
		}
		else if (viewGroup.getParent() instanceof FrameLayout)
		{
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
			params.setMargins(left, top, right, bottom);
			viewGroup.setLayoutParams(params);
		}
		else if (viewGroup.getParent() instanceof RelativeLayout)
		{
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewGroup.getLayoutParams();
			params.setMargins(left, top, right, bottom);
			viewGroup.setLayoutParams(params);
		}
		else
		{
			throw new IllegalArgumentException("invalid parent layout");
		}
		return this;
	}

	/**
	 * 瀑布流效果,可以使用
	 * @param viewId
	 * @param width
	 * @param height
	 * @return
	 */
	public CommonRecyclerViewHolder setLayout(int viewId, int width, int height)
	{
		ViewGroup viewGroup = this.get(viewId);
		ViewGroup.LayoutParams params = viewGroup.getLayoutParams();
		if (-1 != width)
		{
			params.width = width;
		}

		if (-1 != height)
		{
			params.height = height;
		}

		viewGroup.setLayoutParams(params);
		return this;
	}

	/**
	 * 获取到相应的资源
	 * @param viewId
	 * @return
	 */
	public <T extends View> T get(int viewId)
	{
		if (sArray.get(viewId) == null)
		{
			View view = itemView.findViewById(viewId);
			sArray.put(viewId, view);
		}
		return (T) sArray.get(viewId);
	}
}
