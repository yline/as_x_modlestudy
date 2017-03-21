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

public class MaxCountEachLineFragment extends BaseFragment
{
	private LabelFlowLayout labelFlowLayout;

	private LabelAdapter labelAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_max_count_each_line, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		labelFlowLayout = (LabelFlowLayout) view.findViewById(R.id.layout_flow_label_each_line);
		labelAdapter = new MaxCountEachLineLabelAdapter(Arrays.asList(DeleteData.data));
		labelFlowLayout.setAdapter(labelAdapter);

		labelFlowLayout.setMaxCountEachLine(3);
	}

	private class MaxCountEachLineLabelAdapter extends LabelAdapter<String>
	{

		public MaxCountEachLineLabelAdapter(List<String> data)
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
