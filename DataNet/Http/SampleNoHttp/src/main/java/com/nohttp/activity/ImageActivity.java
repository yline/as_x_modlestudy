package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.helper.HttpListener;
import com.nohttp.helper.RecyclerListSingleAdapter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

public class ImageActivity extends CommonActivity implements HttpListener<Bitmap>
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		RecyclerListSingleAdapter singleAdapter = new RecyclerListSingleAdapter();

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_image_activity);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				outRect.set(10, 10, 10, 10);
			}
		});
		recyclerView.setAdapter(singleAdapter);

		singleAdapter.setDataList(this, R.array.activity_image_item);
		singleAdapter.setOnItemClickListener(new RecyclerListSingleAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(View v, int position)
			{
				request(position);
			}
		});
	}

	private void request(int position)
	{
		Request<Bitmap> request = null;
		switch (position)
		{
			case 0:
				request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE);
				break;
			case 1:
				request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE, RequestMethod.POST);
				break;
			case 2:
				request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE, RequestMethod.PUT);
				break;
			case 3:
				request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE, RequestMethod.DELETE);
				break;
			case 4:
				request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE, RequestMethod.OPTIONS);
				break;
			default:
				break;
		}
		if (request != null)
		{
			request(0, request, this, false, true);
		}
	}

	@Override
	public void onSucceed(int what, Response<Bitmap> response)
	{
		int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
		if (responseCode == 200)
		{// 如果确定你们的服务器是get或者post，上面的不用判断
			showImageDialog(null, response.get());
		}
	}

	@Override
	public void onFailed(int what, Response<Bitmap> response)
	{
		showMessageDialog("请求失败", response.getException().getMessage());
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, ImageActivity.class));
	}
}
