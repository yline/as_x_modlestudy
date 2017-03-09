package com.sample.okhttp.activity;

import android.os.Bundle;
import android.view.View;

import com.sample.okhttp.R;
import com.sample.okhttp.bean.VNewsSingleBean;
import com.sample.okhttp.http.HttpHelper;
import com.sample.okhttp.http.XHttp;
import com.yline.base.BaseAppCompatActivity;

/**
 * Created by yline on 2017/2/7.
 */
public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// String httpUrl = "http://120.92.77.154/crest/index.php/api/example/users/N#887a19d10a6601b2";
				String httpUrl = "https://github.com/yissan/CalendarView";

				HttpHelper.doGet(httpUrl);
			}
		});

		findViewById(R.id.btn_test_manager).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new XHttp<VNewsSingleBean>().doRequest("new_tui", VNewsSingleBean.class);
			}
		});
	}
}
