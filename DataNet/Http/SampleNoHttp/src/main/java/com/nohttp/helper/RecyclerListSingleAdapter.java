package com.nohttp.helper;

import android.content.Context;
import android.view.View;

import com.nohttp.R;
import com.yline.common.CommonRecyclerAdapter;
import com.yline.common.CommonRecyclerViewHolder;

import java.util.Arrays;
import java.util.List;

public class RecyclerListSingleAdapter extends CommonRecyclerAdapter<String>
{
	public void setDataList(Context context, int dataListId)
	{
		List<String> cacheDataTypes = Arrays.asList(context.getResources().getStringArray(dataListId));
		super.setDataList(cacheDataTypes);
	}

	@Override
	public void onBindViewHolder(CommonRecyclerViewHolder viewHolder, final int position)
	{
		viewHolder.setText(R.id.item_list_title, sList.get(position));
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
		return R.layout.item_single;
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
