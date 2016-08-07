package com.http.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.data.network.R;
import com.http.connection.get.util.GetConnectionUtil;
import com.http.connection.post.util.PostConnectionUtil;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	private static final String WEB_PROJECT_NAME = "WebHttp";

	private EditText mEtInputIp, mEtInputClass;

	private Button mBtnGetConnectionSubmit, mBtnPostConnectionSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView()
	{
		mEtInputIp = (EditText) findViewById(R.id.et_input_ip);
		mEtInputClass = (EditText) findViewById(R.id.et_input_class);
		mBtnGetConnectionSubmit = (Button) findViewById(R.id.btn_get_connection);
		mBtnPostConnectionSubmit = (Button) findViewById(R.id.btn_post_connection);
	}

	private void initData()
	{
		mBtnGetConnectionSubmit.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String ip = mEtInputIp.getText().toString().trim();
				String className = mEtInputClass.getText().toString().trim();
				className += "?json=\"json\""; // 加一个拼接
				LogFileUtil.v(MainApplication.TAG, "ip = " + ip + ",className = " + className);

				GetConnectionUtil.doLocal(ip, WEB_PROJECT_NAME, className, new GetConnectionUtil.GetConnectionCallback()
				{

					@Override
					public void onSuccess(String result)
					{
						LogFileUtil.i(MainApplication.TAG, "请求成功\nresult = " + result);
					}

					@Override
					public void onError(Exception e)
					{
						LogFileUtil.e(MainApplication.TAG, "网络错误\n", e);
					}
				});
			}
		});

		mBtnPostConnectionSubmit.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String ip = mEtInputIp.getText().toString().trim();
				String className = mEtInputClass.getText().toString().trim();
				className += "?json=\"json\""; // 加一个拼接
				LogFileUtil.v(MainApplication.TAG, "ip = " + ip + ",className = " + className);

				PostConnectionUtil.doLocal(ip, WEB_PROJECT_NAME, className, new PostConnectionUtil.PostConnectionCallback()
				{

					@Override
					public void onSuccess(String result)
					{
						LogFileUtil.i(MainApplication.TAG, "请求成功\nresult = " + result);
					}

					@Override
					public void onError(Exception e)
					{
						LogFileUtil.e(MainApplication.TAG, "网络错误\n", e);
					}
				});
			}
		});
	}

}
