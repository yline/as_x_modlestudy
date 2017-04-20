package com.nohttp.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nohttp.R;
import com.nohttp.helper.RecyclerListMultiAdapter;
import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.UIResizeUtil;
import com.yline.utils.UIScreenUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private LinearLayout llTitle;

	private ImageView ivTitle;

	private int leftMarginDistance;

	private AppBarLayout appBarLayout;

	private RecyclerListMultiAdapter mainRecyclerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		leftMarginDistance = UIScreenUtil.dp2px(this, 42);
		llTitle = (LinearLayout) findViewById(R.id.toolbar_root);
		ivTitle = (ImageView) findViewById(R.id.iv_toolbar_head);
		appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

		UIResizeUtil.build().setIsWidthAdapter(false).setLeftMargin(-leftMarginDistance).commit(llTitle);
		appBarLayout.addOnOffsetChangedListener(new OffSetChangedListener());

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_start_activity);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				outRect.set(10, 10, 10, 10);
			}
		});
		recyclerView.setNestedScrollingEnabled(true);

		mainRecyclerAdapter = new RecyclerListMultiAdapter();
		recyclerView.setAdapter(mainRecyclerAdapter);

		mainRecyclerAdapter.setDataList(this, R.array.activity_start_items, R.array.activity_start_items_des);
		mainRecyclerAdapter.setOnItemClickListener(new RecyclerListMultiAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(View v, int position)
			{
				onItemViewClick(position);
			}
		});
	}

	private class OffSetChangedListener implements AppBarLayout.OnOffsetChangedListener
	{

		@Override
		public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
		{
			int maxScroll = appBarLayout.getTotalScrollRange();
			float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
			ivTitle.setAlpha(percentage);

			int background = (int) (250 * percentage);
			llTitle.getBackground().mutate().setAlpha(background);

			int realSize = (int) (leftMarginDistance * percentage);
			llTitle.setTranslationX(realSize);
		}
	}

	private void onItemViewClick(int position)
	{
		switch (position)
		{
			case 0: // 最原始使用方法
				OriginalActivity.actionStart(this);
				break;
			case 1: // 各种请求方法演示(GET, POST, HEAD, PUT等等)
				MethodActivity.actionStart(this);
				break;
			case 2: // 请求图片
				ImageActivity.actionStart(this);
				break;
			case 3: // JsonObject, JsonArray
				JsonActivity.actionStart(this);
				break;
			case 4: // POST一段JSON、XML，自定义包体等
				PostBodyActivity.actionStart(this);
				break;
			case 5: // 自定义请求GsonJson Problem
				DefineRequestActivity.actionStart(this);
				break;
			case 6: // NoHttp缓存演示
				CacheActivity.actionStart(this);
				break;
			case 7:// 响应码302/303重定向演示
				RedirectActivity.actionStart(this);
				break;
		}
	}
}
