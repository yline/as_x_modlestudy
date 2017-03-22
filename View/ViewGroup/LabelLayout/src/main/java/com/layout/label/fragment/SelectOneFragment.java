package com.layout.label.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.layout.label.R;
import com.layout.label.activity.DeleteData;
import com.layout.label.view.labellayout.FlowLayout;
import com.layout.label.view.labellayout.LabelAdapter;
import com.layout.label.view.labellayout.LabelFlowLayout;
import com.yline.base.BaseFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 设定，最大选择个数，和最小选择个数
 *
 * @author yline 2017/3/22 -- 13:20
 * @version 1.0.0
 */
public class SelectOneFragment extends BaseFragment
{
	private LabelFlowLayout labelFlowLayout;

	private LabelAdapter labelAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_select_one, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		labelFlowLayout = (LabelFlowLayout) view.findViewById(R.id.layout_flow_label_select_one);
		labelAdapter = new SelectOneAdapter(Arrays.asList(DeleteData.data));
		labelFlowLayout.setAdapter(labelAdapter);

		// labelFlowLayout.setMaxSelectCount(3); 设置最多可选择多少个
		// labelAdapter.setSelectedList(1, 3, 5, 7, 8, 9); 默认选择哪几个
		labelFlowLayout.setMaxSelectCount(2);
		labelFlowLayout.setMinSelectCount(2);
		labelAdapter.setSelectedList(0, 1, 3);

		labelFlowLayout.setOnTagClickListener(new LabelFlowLayout.OnTagClickListener()
		{
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent)
			{
				// return true;
				return false;
			}
		});

		labelFlowLayout.setOnSelectListener(new LabelFlowLayout.OnSelectListener()
		{
			@Override
			public void onSelected(Set<Integer> selectPosSet)
			{
				getActivity().setTitle("choose:" + selectPosSet.toString());
			}
		});
	}

	private class SelectOneAdapter extends LabelAdapter<String>
	{

		public SelectOneAdapter(List<String> data)
		{
			super(data);
		}

		@Override
		public View getView(FlowLayout parent, int position, String str)
		{
			TextView tvItem = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_label, labelFlowLayout, false);
			tvItem.setText(str);
			return tvItem;
		}

		@Override
		public boolean setSelected(int position, String s)
		{
			// return s.equals("Android"); 默认选择哪些标签
			return super.setSelected(position, s);
		}
	}
}
