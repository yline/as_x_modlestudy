package com.sample.https.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sample.https.R;
import com.sample.https.connection.get.GetUtil;
import com.sample.https.connection.post.PostUtil;
import com.yline.log.LogFileUtil;

/**
 * Https 请求,本地写好,未经过测试
 * 原因:后台没有搭建好
 * Blog:http://blog.csdn.net/iispring/article/details/51615631
 * PS:之后还有OkHttp,前提都是本地搭好后台
 * @author YLine 2016/8/7 --> 15:29
 * @version 1.0
 */
public class MainActivity extends Activity
{
	private static final String WEB_PROJECT_NAME = "WebHttps";
	
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
				LogFileUtil.v(IApplication.TAG, "ip = " + ip + ",className = " + className);

				GetUtil.doHttpsLocal(ip, WEB_PROJECT_NAME, className, new GetUtil.GetConnectionCallback()
				{

					@Override
					public void onSuccess(String result)
					{
						LogFileUtil.i(IApplication.TAG, "请求成功\nresult = " + result);
					}

					@Override
					public void onError(Exception e)
					{
						LogFileUtil.e(IApplication.TAG, "网络错误\n", e);
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
				LogFileUtil.v(IApplication.TAG, "ip = " + ip + ",className = " + className);

				PostUtil.doHttpsLocal(ip, WEB_PROJECT_NAME, className, new PostUtil.PostConnectionCallback()
				{

					@Override
					public void onSuccess(String result)
					{
						LogFileUtil.i(IApplication.TAG, "请求成功\nresult = " + result);
					}

					@Override
					public void onError(Exception e)
					{
						LogFileUtil.e(IApplication.TAG, "网络错误\n", e);
					}
				});
			}
		});
	}
}
