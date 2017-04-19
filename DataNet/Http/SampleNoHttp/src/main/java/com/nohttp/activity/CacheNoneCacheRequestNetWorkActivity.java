package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.helper.HttpListener;
import com.nohttp.helper.RecyclerListSingleAdapter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

public class CacheNoneCacheRequestNetWorkActivity extends CommonActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache_none_cache_request_net_work);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cache_demo_activity);

		RecyclerListSingleAdapter singleAdapter = new RecyclerListSingleAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				outRect.set(10, 10, 10, 10);
			}
		});
		recyclerView.setAdapter(singleAdapter);

		singleAdapter.setDataList(this, R.array.activity_cache_item);
		singleAdapter.setOnItemClickListener(new RecyclerListSingleAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(View v, int position)
			{
				if (0 == position)
				{// 请求String。
					requestString();
				}
				else if (1 == position)
				{// 请求图片。
					requestImage();
				}
			}
		});
	}

	/**
	 * 请求String。
	 */
	private void requestString()
	{
		Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD);
		request.add("name", "yanzhenjie");
		request.add("pwd", 123);
		request.setCacheKey("CacheKeyNoneCacheRequestNetworkString");//
		// 这里的key是缓存数据的主键，默认是url，使用的时候要保证全局唯一，否则会被其他相同url数据覆盖。
		request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
		//设置为NONE_CACHE_REQUEST_NETWORK表示先去读缓存，如果没有缓存才请求服务器。
		request(0, request, stringHttpListener, false, true);
	}

	private HttpListener<String> stringHttpListener = new HttpListener<String>()
	{
		@Override
		public void onSucceed(int what, Response<String> response)
		{
			String string = response.isFromCache() ? "结果来自缓存" : "结果来自网络";
			showMessageDialog(string, response.get());
		}

		@Override
		public void onFailed(int what, Response<String> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};

	/**
	 * 请求Image。
	 */
	private void requestImage()
	{
		Request<Bitmap> request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE);
		request.setCacheKey("CacheKeyNoneCacheRequestNetworkImage");//
		// 这里的key是缓存数据的主键，默认是url，使用的时候要保证全局唯一，否则会被其他相同url数据覆盖。
		request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
		//设置为NONE_CACHE_REQUEST_NETWORK表示先去读缓存，如果没有缓存才请求服务器。
		request(0, request, imageHttpListener, false, true);
	}

	private HttpListener<Bitmap> imageHttpListener = new HttpListener<Bitmap>()
	{
		@Override
		public void onSucceed(int what, Response<Bitmap> response)
		{
			String string = response.isFromCache() ? "结果来自缓存" : "结果来自网络";
			showImageDialog(string, response.get());
		}

		@Override
		public void onFailed(int what, Response<Bitmap> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, CacheNoneCacheRequestNetWorkActivity.class));
	}
}
