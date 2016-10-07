package com.listview.difftype.instance;

import android.content.Context;
import android.view.ViewGroup;

import com.view.listview.R;
import com.yline.base.common.CommonListAdapter;

/**
 * 两种风格的type
 * @author yline 2016/10/7 --> 9:57
 * @version 1.0.0
 */
public class DiffAdapter extends CommonListAdapter<DiffBean>
{
	private static final int COM_MSG = 0;

	private static final int TO_MSG = 1;

	public DiffAdapter(Context context)
	{
		super(context);
	}

	@Override
	public int getItemViewType(int position)
	{
		if (sList.get(position).isComMsg())
		{
			return COM_MSG;
		}
		else
		{
			return TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	protected int getItemRes(int position)
	{
		if (sList.get(position).isComMsg())
		{
			return R.layout.item_chat_from;
		}
		else
		{
			return R.layout.item_chat_to;
		}
	}

	@Override
	protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		viewHolder.setText(R.id.tv_username, sList.get(i).getUserName());
		viewHolder.setText(R.id.tv_chatcontent, sList.get(i).getChatContent());
	}
}
