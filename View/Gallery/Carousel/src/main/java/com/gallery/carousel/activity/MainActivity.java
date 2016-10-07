package com.gallery.carousel.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gallery.carousel.fragment.GalleryFragment;
import com.gallery.carousel.fragment.GalleryFragmentParams;
import com.view.gallery.test.R;
import com.yline.base.BaseFragmentActivity;

/**
 * 这一块,需要修改的地方还挺多
 * @author YLine 2016/8/9 --> 20:28
 * @version 1.0.0
 */
public class MainActivity extends BaseFragmentActivity implements GalleryFragment.OnCarouselGalleryItemClickListener
{
	private FragmentManager fragmentManager = getSupportFragmentManager();

	private GalleryFragment carouselFragment;

	private ListView listView;

	private int[] mImageSrc = {R.drawable.img1, R.drawable.img3, R.drawable.img4, R.drawable.img2, R.drawable.img5,};

	private static final String[] strs = new String[]{"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "nineth", "ten"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		GalleryFragmentParams params = new GalleryFragmentParams();
		params.setBeansId(mImageSrc);

		params.setGalleryLayout(getScreenWidth(getApplicationContext()) - 40, getScreenWidth(getApplicationContext()) * 3 / 5);
		params.setViewLayout(getScreenWidth(getApplicationContext()) / 2, getScreenWidth(getApplicationContext()) / 3);
		/*
		params.setViewSpace(-180);
        params.setZoomUnit(280);
        */
		carouselFragment = new GalleryFragment(params);
		fragmentManager.beginTransaction().add(R.id.fragment_main, carouselFragment).commit();

		// listView计算高度就可以了
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
	}

	/** 获取屏幕宽度 */
	private int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		MainApplication.toast("item = " + position);
	}

}
