package com.viewpager.carousel.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.view.viewpager.carousel.R;
import com.viewpager.carousel.viewhelper.MainADHelper;
import com.yline.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements MainADHelper.OnPageClickListener
{
	private ListView listView;

	private static final String[] strS = new String[]{"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "nineth", "ten"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView()
	{
		listView = (ListView) findViewById(R.id.show_listview);
	}

	private void initData()
	{
		List<Integer> data = new ArrayList<>();
		data.add(R.drawable.img1);
		data.add(R.drawable.img2);
		data.add(R.drawable.img3);
		data.add(R.drawable.img4);
		data.add(R.drawable.img5);

		MainADHelper mainADHelper = new MainADHelper();
		mainADHelper.build().setResource(data).setRecycleRight(false).commit(this);
		mainADHelper.initPoint((LinearLayout) findViewById(R.id.ll_main_ad));
		mainADHelper.initViewPagerView((ViewPager) findViewById(R.id.viewpager_main_ad));
		mainADHelper.setListener(this);
		mainADHelper.startAutoRecycle();

		// listView计算高度就可以了
		listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strS));
	}

	@Override
	public void onPagerClick(View v, int position)
	{
		MainApplication.toast("position = " + position);
	}
}
