package com.layout.label.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.layout.label.R;
import com.yline.base.BaseFragment;

/**
 * 手动布局
 *
 * @author yline 2017/3/22 -- 10:12
 * @version 1.0.0
 */
public class GravityFragment extends BaseFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_gravity, container, false);
	}
}
