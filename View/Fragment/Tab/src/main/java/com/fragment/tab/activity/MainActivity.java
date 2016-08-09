package com.fragment.tab.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.fragment.tab.fragment.ShowFragment;
import com.fragment.tab.styleline.TabLineFragment;
import com.view.fragment.tab.R;

public class MainActivity extends FragmentActivity implements TabLineFragment.OnTabSelectedListener
{
	private FragmentManager fragmentManager = getSupportFragmentManager();

	private TabLineFragment lineFragment;

	private ShowFragment show1;

	private ShowFragment show2;

	private ShowFragment show3;

	private ShowFragment show4;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initData();
	}

	private void initData()
	{
		lineFragment = new TabLineFragment();

		show1 = new ShowFragment();
		show1.setShowStr("show1");
		show2 = new ShowFragment();
		show2.setShowStr("show2");
		show3 = new ShowFragment();
		show3.setShowStr("show3");
		show4 = new ShowFragment();
		show4.setShowStr("show4");

		fragmentManager.beginTransaction().add(R.id.show_linear, lineFragment, "tabline").commit();
		fragmentManager.beginTransaction().add(R.id.content_frame, show1, "content").commit();
	}

	@Override
	public void onTabSelected(int position)
	{
		switch (position)
		{
			case 0:
				fragmentManager.beginTransaction().replace(R.id.content_frame, show1).commit();
				break;
			case 1:
				fragmentManager.beginTransaction().replace(R.id.content_frame, show2).commit();
				break;
			case 2:
				fragmentManager.beginTransaction().replace(R.id.content_frame, show3).commit();
				break;
			case 3:
				fragmentManager.beginTransaction().replace(R.id.content_frame, show4).commit();
				break;
		}
	}

}
