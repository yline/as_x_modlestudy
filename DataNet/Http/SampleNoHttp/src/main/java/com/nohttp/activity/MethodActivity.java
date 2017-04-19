package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.helper.HttpListener;
import com.nohttp.helper.RecyclerListMultiAdapter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;
import java.util.Locale;

public class MethodActivity extends CommonActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_method);
		
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_method_activity);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				outRect.set(10, 10, 10, 10);
			}
		});
		
		RecyclerListMultiAdapter multiAdapter = new RecyclerListMultiAdapter();
		recyclerView.setAdapter(multiAdapter);
		
		multiAdapter.setDataList(this, R.array.activity_method_item, R.array.activity_method_item_des);
		multiAdapter.setOnItemClickListener(new RecyclerListMultiAdapter.OnItemClickListener()
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
		Request<String> request = null;
		switch (position)
		{
			case 0:
				showMessageDialog("有的方法为什么请求失败？",
						"在RFC2616中规定了Http1.1支持的请求方法，有些服务器、客户端(Android系统)不支持某些请求方法，所以请求失败是正常的，但是我们还是要遵守协议，推荐服务器和客户端都支持，这是非常好的设计和体验。");
				break;
			case 1:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.GET);
				break;
			case 2:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.POST);
				break;
			case 3:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.PUT);
				break;
			case 4:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.DELETE);
				break;
			case 5:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.HEAD);
				break;
			case 6:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.PATCH);
				break;
			case 7:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.OPTIONS);
				break;
			case 8:
				request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD, RequestMethod.TRACE);
				break;
			default:
				break;
		}
		
		if (request != null)
		{
			request.add("name", "yanzhenjie");// String类型
			request.add("pwd", 123);
			request.add("userAge", 20);// int类型
			request.add("userSex", '1');// char类型，还支持其它类型
			
			//        // 添加到请求队列
			request(0, request, httpListener, true, true);
		}
	}
	
	private HttpListener<String> httpListener = new HttpListener<String>()
	{
		
		@Override
		public void onSucceed(int what, Response<String> response)
		{
			if (response.getHeaders().getResponseCode() == 501)
			{
				showMessageDialog("请求成功", "服务器不支持的请求方法。");
			}
			else if (RequestMethod.HEAD == response.request().getRequestMethod())// 请求方法为HEAD时没有响应内容
			{
				showMessageDialog("请求成功", "请求方法为HEAD，没有响应内容。");
			}
			else if (response.getHeaders().getResponseCode() == 405)
			{
				List<String> allowList = response.getHeaders().getValues("Allow");
				String allow = "服务器仅仅支持请求方法：%1$s";
				if (allowList != null && allowList.size() > 0)
				{
					allow = String.format(Locale.getDefault(), allow, allowList.get(0));
				}
				showMessageDialog("请求成功", allow);
			}
			else
			{
				showMessageDialog("请求成功", response.get());
			}
		}
		
		@Override
		public void onFailed(int what, Response<String> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, MethodActivity.class));
	}
}
