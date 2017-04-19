package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.helper.HttpListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yline.log.LogFileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class JsonActivity extends CommonActivity
{
	private TextView mTvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json);

		mTvResult = (TextView) findViewById(R.id.tv_result);
		findViewById(R.id.btn_object_reqeust).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Request<JSONObject> request = NoHttp.createJsonObjectRequest(Constants.URL_NOHTTP_JSONOBJECT);
				request.add("name", "yanzhenjie");
				request.add("pwd", 123);
				request(0, request, objectListener, true, true);
			}
		});

		findViewById(R.id.btn_array_request).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Request<JSONArray> request = NoHttp.createJsonArrayRequest(Constants.URL_NOHTTP_JSONARRAY);
				request.add("name", "yanzhenjie");
				request.add("pwd", 123);
				request(1, request, arrayListener, true, true);
			}
		});
	}

	private HttpListener<JSONObject> objectListener = new HttpListener<JSONObject>()
	{
		@Override
		public void onSucceed(int what, Response<JSONObject> response)
		{
			JSONObject jsonObject = response.get();
			if (1 == jsonObject.optInt("error", -1))
			{
				String result = "请求方法：%1$s\\n请求地址：%2$s\\n响应数据：%3$s\\n业务状态码：%4$s。";
				result = String.format(Locale.getDefault(), result, response.request().getRequestMethod()
						.toString(), jsonObject.optString("url"), jsonObject.optString("data"), jsonObject
						.optString("error"));

				LogFileUtil.v(jsonObject.optString("data"));
				mTvResult.setText(result);
			}
		}

		@Override
		public void onFailed(int what, Response<JSONObject> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};

	private HttpListener<JSONArray> arrayListener = new HttpListener<JSONArray>()
	{
		@Override
		public void onSucceed(int what, Response<JSONArray> response)
		{
			JSONArray jsonArray = response.get();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				String string = jsonArray.optString(i);
				builder.append(string).append("\n");
			}
			LogFileUtil.v(builder.toString());

			mTvResult.setText(builder.toString());
		}

		@Override
		public void onFailed(int what, Response<JSONArray> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, JsonActivity.class));
	}
}
