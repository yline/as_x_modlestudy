package com.listview.columns.instance;

import android.content.Context;
import android.view.ViewGroup;

import com.view.listview.R;
import com.yline.view.common.CommonListAdapter;
import com.yline.view.common.ViewHolder;

/**
 * 不同列数的listView;具体使用时,还需要对每个点击背景进行处理
 *
 * @author yline 2016/10/6 --> 16:36
 * @version 1.0.0
 */
public class ColumnsAdapter extends CommonListAdapter<TestBean>
{
	private int sCloumn;

	/**
	 * 行数
	 *
	 * @param context
	 * @param cloumn  如果超过范围,默认为1列
	 */
	public ColumnsAdapter(Context context, int cloumn)
	{
		super(context);
		this.sCloumn = cloumn;
	}

	public int getColumn()
	{
		return this.sCloumn;
	}

	@Override
	public int getCount()
	{
		int lines = super.getCount();
		if (2 == sCloumn || 3 == sCloumn)
		{
			return lines % sCloumn == 0 ? lines / sCloumn : lines / sCloumn + 1;
		}
		else
		{
			return lines;
		}
	}

	@Override
	protected int getItemRes(int position)
	{
		if (2 == sCloumn)
		{
			return R.layout.item_column_two;
		}
		else if (3 == sCloumn)
		{
			return R.layout.item_column_three;
		}
		else
		{
			return R.layout.item_column_one;
		}
	}

	@Override
	protected void onBindViewHolder(ViewGroup parent, ViewHolder viewHolder, int position)
	{
		if (2 == sCloumn)
		{
			setViewContentTwo(position, parent, viewHolder);
		}
		else if (3 == sCloumn)
		{
			setViewContentThree(position, parent, viewHolder);
		}
		else
		{
			setViewContentOne(position, parent, viewHolder);
		}
	}

	private void setViewContentOne(int i, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		viewHolder.setText(R.id.tv_id1, sList.get(i).getId() + "");
		viewHolder.setText(R.id.tv_name1, sList.get(i).getName());
	}

	private void setViewContentTwo(int i, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		int tempCount = 2 * i;

		viewHolder.setText(R.id.tv_id1, sList.get(tempCount).getId() + "");
		viewHolder.setText(R.id.tv_name1, sList.get(tempCount).getName());

		// 如果大小没有超过界限
		if (tempCount < sList.size() - 1)
		{
			viewHolder.setText(R.id.tv_id2, sList.get(tempCount + 1).getId() + "");
			viewHolder.setText(R.id.tv_name2, sList.get(tempCount + 1).getName());
		}
	}

	private void setViewContentThree(int i, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		int tempCount = 3 * i;

		viewHolder.setText(R.id.tv_id1, sList.get(tempCount).getId() + "");
		viewHolder.setText(R.id.tv_name1, sList.get(tempCount).getName());

		// 如果大小没有超过界限
		if (tempCount < sList.size() - 1)
		{
			viewHolder.setText(R.id.tv_id2, sList.get(tempCount + 1).getId() + "");
			viewHolder.setText(R.id.tv_name2, sList.get(tempCount + 1).getName());
		}

		if (tempCount < sList.size() - 2)
		{
			viewHolder.setText(R.id.tv_id3, sList.get(tempCount + 2).getId() + "");
			viewHolder.setText(R.id.tv_name3, sList.get(tempCount + 2).getName());
		}
	}
}
