package com.viewpager.carousel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.view.viewpager.carousel.R;
import com.viewpager.carousel.widget.ADWidget;
import com.yline.application.BaseApplication;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_widget_ad);

		// 通过重写的方式,直接设定好参数
		ADWidget adWidget = new ADWidget()
		{
			@Override
			public int getViewPagerHeight()
			{
				return 360;
			}
		};
		adWidget.start(this, 8);
		adWidget.attach(linearLayout);
		adWidget.setListener(new ADWidget.OnPageListener()
		{
			@Override
			public void onPageClick(View v, int position)
			{
				BaseApplication.toast("position = " + position);
			}

			@Override
			public void onPageInstance(ImageView imageView, int position)
			{
				imageView.setImageResource(R.drawable.global_load_failed);
			}
		});
	}
}
