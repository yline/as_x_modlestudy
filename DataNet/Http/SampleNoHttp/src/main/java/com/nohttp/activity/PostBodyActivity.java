package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.helper.HttpListener;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.tools.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class PostBodyActivity extends CommonActivity
{
	private EditText mEdtPostBody;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_body);

		mEdtPostBody = (EditText) findViewById(R.id.edt_post_body);
		try
		{
			InputStream inputStream = getAssets().open("json.txt");
			String s = IOUtils.toString(inputStream);
			mEdtPostBody.setText(s);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// 提交JSON、XML、String、InputStream、ByteArray。
		findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				/**
				 * 这里要注意的是：
				 * 1. 请求方法一定是POST、PUT等可以直接写流出去的方法。
				 */
				Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_POSTBODY, RequestMethod.POST);
				request.add("name", "yanzhenjie");
				request.add("pwd", 123);

				//        1. 这里可以push任何数据上去（比如String、Json、XML、图片）。
				//        request.setDefineRequestBody(InputStream body, String contentType);

				//        2. 这里可以push任何string数据（json、xml等），并可以指定contentType。
				//        request.setDefineRequestBody(String body, String contentType);

				//        3. 下面的两个的contentType默认为application/json，传进去的数据要为json。
				//        request.setDefineRequestBodyForJson(JSONObject jsonBody);
				//        request.setDefineRequestBodyForJson(String jsonBody);

				//        4. 这里的contentType默认为application/xml。
				//        request.setDefineRequestBodyForXML(String xmlBody);

				// 这里我们用json多例子
				String jsonBody = mEdtPostBody.getText().toString();
				if (TextUtils.isEmpty(jsonBody))
				{
					IApplication.toast("请求输入提交的json。");
				}
				else
				{
					Logger.i("提交的数据：" + jsonBody);
					request.setDefineRequestBodyForJson(jsonBody);
					request(0, request, httpListener, false, true);
				}
			}
		});
	}

	private HttpListener<String> httpListener = new HttpListener<String>()
	{
		@Override
		public void onSucceed(int what, Response<String> response)
		{
			showMessageDialog("请求成功", response.get());
		}

		@Override
		public void onFailed(int what, Response<String> response)
		{
			showMessageDialog("请求失败", response.getException().getMessage());
		}
	};
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, PostBodyActivity.class));
	}
}
