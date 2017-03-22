package com.layout.label.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.layout.label.R;
import com.layout.label.view.labellayout.LabelView;
import com.yline.base.BaseFragment;

public class SingleClickFragment extends BaseFragment
{
	private LabelView labelView;

	public SingleClickFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_single_click, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		labelView = (LabelView) view.findViewById(R.id.label_single_click);
		labelView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				labelView.setChecked(!labelView.isChecked());
			}
		});
	}
}
