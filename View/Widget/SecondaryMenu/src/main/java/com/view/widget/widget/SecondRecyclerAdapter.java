package com.view.widget.widget;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.view.widget.R;
import com.yline.view.recycler.adapter.CommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二级列表中，第二个列表的，adapter
 *
 * @author yline 2017/5/23 -- 10:50
 * @version 1.0.0
 */
public class SecondRecyclerAdapter extends CommonRecyclerAdapter<String>
{
	private boolean[] isSelected;

	private OnSecondListClickListener onSecondListClickListener;

	public void setOnSecondListClickListener(OnSecondListClickListener onSecondListClickListener)
	{
		this.onSecondListClickListener = onSecondListClickListener;
	}

	@Override
	public void setDataList(List<String> strings)
	{
		super.setDataList(strings);
		isSelected = new boolean[strings.size()];
	}

	public void setSelectPositionList(List<Integer> positionList)
	{
		for (int position : positionList)
		{
			if (position < sList.size() && position > -1)
			{
				isSelected[position] = true;
				notifyItemChanged(position);
			}
		}
	}

	public boolean isSelected(int position)
	{
		if (position < sList.size())
		{
			return isSelected[position];
		}
		return false;
	}

	public void setSelectPosition(int position, boolean isSelect)
	{
		if (position < sList.size() && position > -1)
		{
			isSelected[position] = isSelect;
			notifyItemChanged(position);
		}
	}

	public void setSelectPositionAndCancelAll(int position)
	{
		if (position < sList.size() && position > -1)
		{
			Arrays.fill(isSelected, false);
			isSelected[position] = true;
			notifyDataSetChanged();
		}
	}

	/**
	 * 获取已选择的数据
	 *
	 * @return
	 */
	public List<String> getSelectedList()
	{
		List<String> secondSelectedList = new ArrayList<>();
		if (null != isSelected)
		{
			for (int i = 0; i < isSelected.length; i++)
			{
				if (isSelected[i])
				{
					secondSelectedList.add(sList.get(i));
				}
			}
		}
		return secondSelectedList;
	}

	@Override
	public int getItemRes()
	{
		return getItemResource();
	}

	@Override
	public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position)
	{
		viewHolder.setText(R.id.tv_item_second, sList.get(position));
		viewHolder.getItemView().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				isSelected[position] = !isSelected[position];
				notifyItemChanged(position);

				if (null != onSecondListClickListener)
				{
					onSecondListClickListener.onSecondClick(viewHolder, sList.get(position), position, isSelected[position]);
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
		return R.layout.lib_view_recycler_secondary_item_second;
	}

	public interface OnSecondListClickListener
	{
		/**
		 * 列表被点击时，响应
		 *
		 * @param viewHolder
		 * @param str
		 * @param position
		 * @param isSelected
		 */
		void onSecondClick(RecyclerViewHolder viewHolder, String str, int position, boolean isSelected);
	}
}
