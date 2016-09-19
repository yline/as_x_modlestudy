package com.gridview.threshold.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gridview.threshold.GridViewBean;
import com.gridview.threshold.R;
import com.yline.base.BaseFragment;
import com.yline.base.common.CommonListAdapter;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class GridViewFragment extends BaseFragment
{
	private int column = 5;

	private GridView gridView;

	private GridViewAdapter gridViewAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_gridview, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		gridView = (GridView) view.findViewById(R.id.gv_thres);
		gridViewAdapter = new GridViewAdapter(getActivity(), new ArrayList<GridViewBean>());

		gridView.setNumColumns(column);
		gridView.setVerticalSpacing(20);
		gridView.setHorizontalSpacing(13);
		gridView.setAdapter(gridViewAdapter);
	}

	public void updateBeans(List<GridViewBean> list)
	{
		gridViewAdapter.updateBeans(list);
	}

	private class GridViewAdapter extends CommonListAdapter<GridViewBean>
	{
		public GridViewAdapter(Context context, List<GridViewBean> list)
		{
			super(context, list);
		}

		@Override
		protected int getItemRes()
		{
			return R.layout.item_grid;
		}

		public void updateBeans(List<GridViewBean> list)
		{
			if (null != list)
			{
				this.sList = list;
				notifyDataSetChanged();
			}
			else
			{
				LogFileUtil.v("GridViewAdapter", "list is null");
			}
		}

		@Override
		protected void setViewContent(final int position, ViewGroup parent, ViewHolder item)
		{
			item.get(R.id.ll_gtidview).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					MainApplication.toast(sList.get(position).getName());
				}
			});
			item.get(R.id.iv_gridview).setBackgroundResource(sList.get(position).getIcon());
			item.setText(R.id.tv_gridview, sList.get(position).getName());
		}

	}
}
