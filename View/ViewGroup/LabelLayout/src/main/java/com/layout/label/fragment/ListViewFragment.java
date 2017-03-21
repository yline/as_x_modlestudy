package com.layout.label.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.layout.label.R;
import com.layout.label.view.labellayout.FlowLayout;
import com.layout.label.view.labellayout.LabelAdapter;
import com.layout.label.view.labellayout.LabelFlowLayout;
import com.yline.base.BaseFragment;
import com.yline.base.common.CommonListAdapter;
import com.yline.base.common.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据量太大会卡
 *
 * @author yline 2017/2/24 --> 18:00
 * @version 1.0.0
 */
public class ListViewFragment extends BaseFragment
{
	private ListView lvLabel;

	private List<List<String>> data;

	private CommonListAdapter commonListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_listview, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		lvLabel = (ListView) view.findViewById(R.id.lv_label);

		data = new ArrayList<>();
		initData(data);

		commonListAdapter = new ListAdapter(getContext());

		lvLabel.setAdapter(commonListAdapter);
		commonListAdapter.set(data);
	}

	private class ListAdapter extends CommonListAdapter<List<String>>
	{
		private Map<Integer, Set<Integer>> selectedMap = new HashMap<>();

		public ListAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_label_list;
		}

		@Override
		protected void setViewContent(final int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			LabelFlowLayout labelFlowLayout = viewHolder.get(R.id.layout_flow_label_list);

			LabelAdapter<String> labelAdapter = new LabelAdapter<String>(sList.get(i))
			{
				@Override
				public View getView(FlowLayout parent, int position, String s)
				{
					TextView tvLabel = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_label, parent, false);
					tvLabel.setText(s);
					return tvLabel;
				}
			};

			labelFlowLayout.setAdapter(labelAdapter);
			//重置状态
			labelAdapter.setSelectedList(selectedMap.get(i));

			labelFlowLayout.setOnSelectListener(new LabelFlowLayout.OnSelectListener()
			{
				@Override
				public void onSelected(Set<Integer> selectPosSet)
				{
					selectedMap.put(i, selectPosSet);
				}
			});
		}
	}

	private void initData(List<List<String>> data)
	{
		for (int i = 'A'; i < 'C'; i++)
		{
			List<String> itemData = new ArrayList<>();
			for (int j = 0; j < 4; j++)
			{
				itemData.add(i + "-" + j);
			}
			data.add(itemData);
		}
	}
}
