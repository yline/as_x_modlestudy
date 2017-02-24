package com.layout.label.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.layout.label.R;
import com.layout.label.view.labellayout.FlowLayout;
import com.layout.label.view.labellayout.LabelAdapter;
import com.layout.label.view.labellayout.LabelFlowLayout;

import java.util.Set;

public class SimpleFragment extends Fragment
{
	private String[] values = new String[]{"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello", "Android", "Weclome", "Button ImageView", "TextView", "Helloworld", "Android", "Weclome Hello", "Button Text", "TextView"};

	private LabelFlowLayout labelFlowLayout;

	private LabelAdapter labelAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_simple, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		init(view);

		// mFlowLayout.setMaxSelectCount(3); 设置最多可选择多少个
		// mAdapter.setSelectedList(1, 3, 5, 7, 8, 9); 默认选择哪几个

		setClickView();
	}

	private void init(View view)
	{
		labelFlowLayout = (LabelFlowLayout) view.findViewById(R.id.layout_flow_label);

		labelAdapter = new LabelAdapter<String>(values)
		{
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
		};

		labelFlowLayout.setAdapter(labelAdapter);
	}

	private void setClickView()
	{
		labelFlowLayout.setOnTagClickListener(new LabelFlowLayout.OnTagClickListener()
		{
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent)
			{
				// Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
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
}
