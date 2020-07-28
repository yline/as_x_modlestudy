package com.slidemenu.tab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.slidemenu.tab.slidingmenu.SlidingMenu;
import com.view.slidingmenu.tab.R;
import com.yline.base.BaseFragment;

/**
 * 侧栏
 */
public class MainTitleFragment extends BaseFragment
{
	private SlidingMenu mSlidingMenu;

	private Button mBtnTitleMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main_title, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initView(view);
		initMenu();

		mBtnTitleMenu.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				mSlidingMenu.toggle();
			}
		});
	}

	private void initView(View view)
	{
		mBtnTitleMenu = (Button) view.findViewById(R.id.btn_title_menu);
	}

	private void initMenu()
	{
		mSlidingMenu = new SlidingMenu(getActivity());
		mSlidingMenu.setMode(SlidingMenu.RIGHT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		mSlidingMenu.setShadowWidth(10);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// mSlidingMenu.setBehindWidth(480);
		mSlidingMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_WINDOW);
		mSlidingMenu.setMenu(R.layout.fragment_main_menu);
	}
}
