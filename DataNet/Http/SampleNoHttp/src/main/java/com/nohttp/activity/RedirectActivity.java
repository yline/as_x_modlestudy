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
import com.nohttp.helper.RecyclerListSingleAdapter;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.IBasicRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RedirectHandler;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Locale;

public class RedirectActivity extends CommonActivity implements HttpListener<String>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redirect);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_redirect_activity);

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

		singleAdapter.setDataList(this, R.array.activity_redirect_item);
		singleAdapter.setOnItemClickListener(new RecyclerListSingleAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(View v, int position)
			{
				if (0 == position)
				{
					requestAllowRedirect();
				}
				else
				{
					requestDisAllowRedirect();
				}
			}
		});
	}


	/**
	 * 允许重定向的请求
	 */
	public void requestAllowRedirect()
	{
		final Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_REDIRECT_BAIDU);
		request.setRedirectHandler(new RedirectHandler()
		{
			@Override
			public IBasicRequest onRedirect(Headers responseHeaders)
			{
				// 允许重定向时这个方法会被调用
				// 1. 返回null，NoHttp会自动拷贝父请求的请求方法和代理自动请求，不会拷贝其他属性。
				// 2. 返回非null，会把这个新请求的数据交给父请求去解析。
				Request<String> redirectRequest = NoHttp.createStringRequest(responseHeaders.getLocation());
				redirectRequest.setRedirectHandler(this);// 为了防止嵌套重定向，这里可以每个子级请求都监听
				return redirectRequest;
			}

			// 是否不允许重定向。
			@Override
			public boolean isDisallowedRedirect(Headers responseHeaders)
			{
				// 返回false表示允许重定向
				return false;
			}
		});
		request(0, request, this, false, true);
	}

	/**
	 * 不允许重定向的请求
	 */
	private void requestDisAllowRedirect()
	{
		Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_REDIRECT_BAIDU);
		request.setRedirectHandler(new RedirectHandler()
		{
			@Override
			public IBasicRequest onRedirect(Headers responseHeaders)
			{
				// 不允许重定向时此方法不会被调用。
				return null;
			}

			@Override
			public boolean isDisallowedRedirect(Headers responseHeaders)
			{
				// 返回true代表不允许重定向。
				return true;
			}
		});
		request(0, request, this, false, true);
	}

	@Override
	public void onSucceed(int what, Response<String> response)
	{
		Headers headers = response.getHeaders();
		if (headers.getResponseCode() == 302 || headers.getResponseCode() == 301 || headers.getResponseCode() ==
				307)
		{
			String message = "被重定向到URL：%1$s。";
			message = String.format(Locale.getDefault(), message, headers.getLocation());
			showMessageDialog("请求成功", message);
		}
		else if (headers.getResponseCode() == 200)
		{
			showMessageDialog("请求成功", response.get());
		}
	}

	@Override
	public void onFailed(int what, Response<String> response)
	{
		showMessageDialog("请求失败", response.getException().getMessage());
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, RedirectActivity.class));
	}
}
