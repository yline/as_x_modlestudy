package com.nohttp.helper;

import android.view.View;

import com.nohttp.R;
import com.yline.common.CommonRecyclerAdapter;
import com.yline.common.CommonRecyclerViewHolder;

public class RecyclerListMultiAdapter extends CommonRecyclerAdapter<RecyclerListMultiBean>
{
	@Override
	public void onBindViewHolder(CommonRecyclerViewHolder viewHolder, final int position)
	{
		viewHolder.setText(R.id.item_list_title, sList.get(position).getTitle());
		viewHolder.setText(R.id.item_list_title_sub, sList.get(position).getSubTitle());

		viewHolder.getItemView().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != itemClickListener)
				{
					itemClickListener.onItemClick(v, position);
				}
			}
		});
	}

	@Override
	public int getItemRes()
	{
		return R.layout.item_main;
	}

	private OnItemClickListener itemClickListener;

	public void setOnItemClickListener(OnItemClickListener itemClickListener)
	{
		this.itemClickListener = itemClickListener;
	}

	public interface OnItemClickListener
	{
		void onItemClick(View v, int position);
	}
}
