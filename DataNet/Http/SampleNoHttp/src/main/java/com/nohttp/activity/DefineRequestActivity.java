package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nohttp.R;
import com.nohttp.bean.GsonBean;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.helper.HttpListener;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.Locale;

/**
 * @author yline 2017/4/19 -- 15:16
 * @version 1.0.0
 */
public class DefineRequestActivity extends CommonActivity
{
	private TextView mTvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_define_request);

		mTvResult = (TextView) findViewById(R.id.tv_result);
		findViewById(R.id.btn_fast_json).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 这里用的demo自定义的FastJsonRequest解析服务器的json。
				Request<GsonBean> request = new GsonJsonRequest(Constants.URL_NOHTTP_JSONOBJECT, RequestMethod.GET);
				request.add("name", "yanzhenjie");
				request.add("pwd", 123);
				request(0, request, jsonHttpListener, false, true);
			}
		});

		findViewById(R.id.btn_java_bean).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 这里用的是demo自定义的JavaBeanRequest对象对请求，里面用fastjson解析服务器的数据。
				Request<GsonBean> request = new GsonJsonRequest(Constants.URL_NOHTTP_JSONOBJECT, RequestMethod.GET);
				request.add("name", "yanzhenjie");
				request.add("pwd", 123);
				request(0, request, zhenjieHttpListener, false, true);
			}
		});
	}

	/**
	 * 接受FastJson的相应结果。
	 */
	private HttpListener<GsonBean> jsonHttpListener = new HttpListener<GsonBean>()
	{
		@Override
		public void onSucceed(int what, Response<GsonBean> response)
		{
			GsonBean gsonBean = response.get();
			if (gsonBean.getError() == 1)
			{
				String method = response.request().getRequestMethod().toString();

				String result = String.format(Locale.getDefault(), "请求方法：%1$s\n请求地址：%2$s\n响应数据：%3$s\n业务状态码：%4$s。", method, gsonBean.getUrl(), gsonBean.getData(), gsonBean.getError() + "");

				mTvResult.setText(result);
			}
		}
		
		@Override
		public void onFailed(int what, Response<GsonBean> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};

	/**
	 * 接受JavaBean响应。
	 */
	private HttpListener<GsonBean> zhenjieHttpListener = new HttpListener<GsonBean>()
	{
		@Override
		public void onSucceed(int what, Response<GsonBean> response)
		{
			GsonBean gsonBean = response.get();
			mTvResult.setText(gsonBean.toString());
		}

		@Override
		public void onFailed(int what, Response<GsonBean> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};

	public class GsonJsonRequest extends RestRequest<GsonBean>
	{
		public GsonJsonRequest(String url)
		{
			this(url, RequestMethod.GET);
		}

		public GsonJsonRequest(String url, RequestMethod requestMethod)
		{
			super(url, requestMethod);
			setAccept(Headers.HEAD_VALUE_ACCEPT_APPLICATION_JSON);
		}

		@Override
		public void onPreExecute()
		{
			// TODO 这个方法会在真正请求前被调用，在这里可以做一些加密之类的工作。这个方法在子线程被调用。
		}

		@Override
		public GsonBean parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception
		{
			String result = StringRequest.parseResponseString(responseHeaders, responseBody);
			return new Gson().fromJson(result, GsonBean.class);
		}
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, DefineRequestActivity.class));
	}
}
