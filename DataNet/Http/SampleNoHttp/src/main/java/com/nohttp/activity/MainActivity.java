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
import com.nohttp.helper.RecyclerListMultiBean;
import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.UIResizeUtil;
import com.yline.utils.UIScreenUtil;

import java.util.ArrayList;
import java.util.List;

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

		List<RecyclerListMultiBean> dataList = new ArrayList<>();
		String[] titles = getResources().getStringArray(R.array.activity_start_items);
		String[] titlesDes = getResources().getStringArray(R.array.activity_start_items_des);
		for (int i = 0; i < titles.length; i++)
		{
			dataList.add(new RecyclerListMultiBean(titles[i], titlesDes[i]));
		}
		mainRecyclerAdapter.setDataList(dataList);

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
		}

		/*Intent intent = null;
		switch (position)
		{
			case 1:// 各种请求方法演示(GET, POST, HEAD, PUT等等)
				intent = new Intent(this, MethodActivity.class);
				break;
			case 2:// 请求图片
				intent = new Intent(this, ImageActivity.class);
				break;
			case 3:// JsonObject, JsonArray
				intent = new Intent(this, JsonActivity.class);
				break;
			case 4:// POST一段JSON、XML，自定义包体等
				intent = new Intent(this, PostBodyActivity.class);
				break;
			case 5:// 自定义请求FastJson
				intent = new Intent(this, DefineRequestActivity.class);
				break;
			case 6:// NoHttp缓存演示
				intent = new Intent(this, CacheActivity.class);
				break;
			case 7:// 响应码302/303重定向演示
				intent = new Intent(this, RedirectActivity.class);
				break;
			case 8:// 文件上传
				intent = new Intent(this, UploadFileActivity.class);
				break;
			case 9: // 文件下载
				intent = new Intent(this, DownloadActivity.class);
				break;
			case 10:// 如何取消请求
				intent = new Intent(this, CancelActivity.class);
				break;
			case 11:// 同步请求
				intent = new Intent(this, SyncActivity.class);
				break;
			case 12:// 通过代理服务器请求
				intent = new Intent(this, ProXYActivity.class);
				break;
			case 13:// https请求
				intent = new Intent(this, HttpsActivity.class);
				break;
			default:
				break;
		}
		if (intent != null)
		{
			startActivity(intent);
		}*/
	}
}
