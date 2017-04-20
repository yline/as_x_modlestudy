package com.nohttp.helper;

import android.content.Context;
import android.view.View;

import com.nohttp.R;
import com.yline.common.CommonRecyclerAdapter;
import com.yline.common.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecyclerListMultiAdapter extends CommonRecyclerAdapter<RecyclerListMultiBean>
{
	public void setDataList(Context context, int titlesId, int titlesDesId)
	{
		List<RecyclerListMultiBean> dataList = new ArrayList<>();
		String[] titles = context.getResources().getStringArray(titlesId);
		String[] titlesDes = context.getResources().getStringArray(titlesDesId);
		for (int i = 0; i < titles.length; i++)
		{
			dataList.add(new RecyclerListMultiBean(titles[i], titlesDes[i]));
		}
		this.setDataList(dataList);
	}

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
		return R.layout.item_multi;
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
