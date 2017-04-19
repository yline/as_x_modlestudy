package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nohttp.R;
import com.nohttp.helper.RecyclerListMultiAdapter;

public class CacheActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cache_activity);

		RecyclerListMultiAdapter multiAdapter = new RecyclerListMultiAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				outRect.set(10, 10, 10, 10);
			}
		});
		recyclerView.setAdapter(multiAdapter);

		multiAdapter.setDataList(this, R.array.activity_cache_entrance, R.array.activity_cache_entrance_des);
		multiAdapter.setOnItemClickListener(new RecyclerListMultiAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(View v, int position)
			{
				switch (position)
				{
					case 0:// Http标准协议的缓存。
						CacheHttpActivity.actionStart(CacheActivity.this);
						break;
					case 1:// 请求网络失败后返回上次的缓存。
						CacheRequestFailedReadCacheActivity.actionStart(CacheActivity.this);
						break;
					case 2:// 没有缓存才去请求网络。
						CacheNoneCacheRequestNetWorkActivity.actionStart(CacheActivity.this);
						break;
					case 3:// 仅仅请求缓存。
						CacheOnlyReadCacheActivity.actionStart(CacheActivity.this);
						break;
					case 4:// 仅仅请求网络。
						CacheOnlyRequestNetworkActivity.actionStart(CacheActivity.this);
						break;
					default:
						break;
				}
			}
		});
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, CacheActivity.class));
	}
}
