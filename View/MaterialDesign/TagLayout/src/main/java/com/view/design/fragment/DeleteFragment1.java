package com.view.design.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.base.BaseFragment;

/**
 * @author yline 2017/2/24 --> 13:58
 * @version 1.0.0
 */
public class DeleteFragment1 extends BaseFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		((TextView) view.findViewById(android.R.id.text1)).setText(this.getClass().getSimpleName());
	}
}
