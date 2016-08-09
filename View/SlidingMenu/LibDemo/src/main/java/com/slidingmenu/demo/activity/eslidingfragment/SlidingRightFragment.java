package com.slidingmenu.demo.activity.eslidingfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.view.slidingmenu.lib.demo.R;
import com.yline.base.BaseFragment;

public class SlidingRightFragment extends BaseFragment
{
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mView == null)
		{
			mView = inflater.inflate(R.layout.slidingmenu_eslidingfragment_right, container, false);
		}
		return mView;
	}
}
