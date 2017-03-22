package com.view.design.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.view.design.R;
import com.view.design.fragment.DeleteFragment1;
import com.view.design.fragment.DeleteFragment2;
import com.view.design.fragment.DeleteFragment3;
import com.yline.base.BaseAppCompatActivity;

public class NullActivity extends BaseAppCompatActivity
{
	private TabLayout tabLayout;

	private FragmentManager fragmentManager = getSupportFragmentManager();

	private DeleteFragment1 deleteFragment1;

	private DeleteFragment2 deleteFragment2;

	private DeleteFragment3 deleteFragment3;

	private static final String[] RES = {"Tab1", "Tab2", "Tab3"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_null);

		initView();

		tabLayout.addTab(tabLayout.newTab().setText(RES[0]));
		tabLayout.addTab(tabLayout.newTab().setText(RES[1]));
		tabLayout.addTab(tabLayout.newTab().setText(RES[2]));

		deleteFragment1 = new DeleteFragment1();
		deleteFragment2 = new DeleteFragment2();
		deleteFragment3 = new DeleteFragment3();

		fragmentManager.beginTransaction().add(R.id.ll_null, deleteFragment1).add(R.id.ll_null, deleteFragment2).hide(deleteFragment2).add(R.id.ll_null, deleteFragment3).hide(deleteFragment3).commit();
		
		tabLayout.setSelectedTabIndicatorHeight(5); // 设置成0,就变成了取消滑动

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				int position = tab.getPosition();
				fragmentManager.beginTransaction().show(getFragment(position)).commit();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{
				int position = tab.getPosition();
				fragmentManager.beginTransaction().hide(getFragment(position)).commit();
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{

			}
		});
	}

	private Fragment getFragment(int position)
	{
		switch (position)
		{
			case 0:
				return deleteFragment1;
			case 1:
				return deleteFragment2;
			case 2:
				return deleteFragment3;
			default:
				return null;
		}
	}

	private void initView()
	{
		tabLayout = (TabLayout) findViewById(R.id.tab_layout_null);
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, NullActivity.class));
	}
}
