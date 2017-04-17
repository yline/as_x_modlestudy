package com.status.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.status.R;
import com.yline.base.BaseFragment;

public class SimpleFragment extends BaseFragment
{
	private TextView mTvTitle;

	private View mFakeStatusBar;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragement_simple, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		mTvTitle = (TextView) view.findViewById(R.id.tv_title);
		mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
	}

	public void setTvTitleBackgroundColor(@ColorInt int color)
	{
		mTvTitle.setBackgroundColor(color);
		mFakeStatusBar.setBackgroundColor(color);
	}
}
