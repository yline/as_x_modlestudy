package com.view.widget.widget;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.view.widget.R;
import com.yline.view.recycler.adapter.CommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 二级列表中，第一个列表的，adapter
 *
 * @author yline 2017/5/23 -- 10:50
 * @version 1.0.0
 */
public class FirstRecyclerAdapter extends CommonRecyclerAdapter<String>
{
	private boolean[] isSelected;

	private int oldPosition = -1;

	private OnFirstListClickListener onFirstListClickListener;

	public void setOnFirstListClickListener(OnFirstListClickListener onFirstListClickListener)
	{
		this.onFirstListClickListener = onFirstListClickListener;
	}

	@Override
	public void setDataList(List<String> strings)
	{
		super.setDataList(strings);
		isSelected = new boolean[strings.size()];
		oldPosition = -1;
	}

	public void setSelectPosition(int position)
	{
		if (position < sList.size() && position >= 0)
		{
			oldPosition = position;
			isSelected[position] = true;
			notifyItemChanged(position);
		}
	}

	public String getSelectedString()
	{
		if (-1 == oldPosition)
		{
			return null;
		}
		else
		{
			return sList.get(oldPosition);
		}
	}

	@Override
	public int getItemRes()
	{
		return getItemResource();
	}

	@Override
	public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position)
	{
		viewHolder.setText(R.id.tv_item_first, sList.get(position));
		viewHolder.setOnClickListener(R.id.tv_item_first, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (oldPosition == position)
				{
					// do nothing
				}
				else
				{
					if (oldPosition == -1)
					{
						isSelected[position] = true;
						oldPosition = position;
						notifyItemChanged(position);
					}
					else
					{
						isSelected[oldPosition] = false;
						notifyItemChanged(oldPosition);

						isSelected[position] = true;
						oldPosition = position;
						notifyItemChanged(position);
					}

					if (null != onFirstListClickListener)
					{
						onFirstListClickListener.onFirstClick(viewHolder, sList.get(position), position);
					}
				}
			}
		});

		updateItemState(viewHolder, position);
	}

	private void updateItemState(RecyclerViewHolder viewHolder, int position)
	{
		viewHolder.getItemView().setSelected(isSelected[position]);
	}

	@LayoutRes
	protected int getItemResource()
	{
		return R.layout.lib_view_recycler_secondary_item_first;
	}

	public interface OnFirstListClickListener
	{
		/**
		 * 列表被点击时，响应
		 *
		 * @param viewHolder
		 * @param str
		 * @param position
		 */
		void onFirstClick(RecyclerViewHolder viewHolder, String str, int position);
	}
}
