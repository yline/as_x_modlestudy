package com.slidingmenu.demo.activity.eslidingfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.lib.slidingmenu.SlidingMenu;
import com.lib.slidingmenu.sdk.SlidingFragmentActivity;
import com.slidingmenu.demo.activity.MainApplication;
import com.view.slidingmenu.lib.demo.R;

import java.util.ArrayList;
import java.util.List;

public class ESlidingFragmentActivity extends SlidingFragmentActivity
{
	private ImageButton iv_left, iv_right;

	private ViewPager mViewPager;

	private FragmentPagerAdapter mAdapter;

	private List<Fragment> mTabs = new ArrayList<Fragment>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MainApplication.addAcitivity(this);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eslidingfragment);

		initRightMenu();
		initViewPager();

		initView();
		iv_left.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				getSlidingMenu().showMenu();
			}
		});

		iv_right.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				getSlidingMenu().showSecondaryMenu();
			}
		});
	}

	/**
	 * 初始化，ViewPager
	 */
	private void initViewPager()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		for (int i = 0; i < 3; i++)
		{
			SlidingFragment tab1 = new SlidingFragment();
			Bundle args1 = new Bundle();
			args1.putString("title", "tab " + i);
			tab1.setArguments(args1);
			mTabs.add(tab1);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mTabs.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
	}

	/**
	 * 初始化，两边的，Menu
	 */
	private void initRightMenu()
	{
		Fragment leftMenuFragment = new SlidingLeftFragment();
		setBehindContentView(R.layout.slidingmenu_eslidingfragment_leftfragment);
		getSupportFragmentManager().beginTransaction().replace(R.id.id_left_menu_frame, leftMenuFragment).commit();

		SlidingMenu menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		menu.setShadowWidth(10);
		menu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		menu.setBehindOffset(240);
		//	menu.setBehindWidth()
		menu.setFadeDegree(0.35f);
		// menu.setBehindScrollScale(1.0f);
		menu.setSecondaryShadowDrawable(R.drawable.slidingmenu_shadow);
		menu.setSecondaryMenu(R.layout.slidingmenu_eslidingfragment_rightfragment);

		Fragment rightMenuFragment = new SlidingRightFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.id_right_menu_frame, rightMenuFragment).commit();
	}

	private void initView()
	{
		iv_left = (ImageButton) findViewById(R.id.id_iv_left);
		iv_right = (ImageButton) findViewById(R.id.id_iv_right);
	}

	public static void actionStart(Context context)
	{
		Intent intent = new Intent();
		intent.setClass(context, ESlidingFragmentActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onDestroy()
	{
		MainApplication.removeActivity(this);
		super.onDestroy();
	}
}
