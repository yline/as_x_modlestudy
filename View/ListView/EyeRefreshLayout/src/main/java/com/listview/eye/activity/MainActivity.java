package com.listview.eye.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.listview.eye.R;
import com.listview.eye.view.EyeRefreshLayout;
import com.listview.eye.view.EyeView;
import com.listview.eye.view.YProgressView;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
	private EyeRefreshLayout eyeRefreshLayout;

	private ListView listView;

	private YProgressView progressView;

	private EyeView eyeView;

	static String[] adapterData = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		eyeRefreshLayout.setOnRefreshListener(new EyeRefreshLayout.OnRefreshListener()
		{
			@Override
			public int onStart()
			{
				eyeView.startAnimate();
				return 3000;
			}

			@Override
			public void onRefresh(int headerHeight, int pullHeight)
			{
				float progress = (float) pullHeight / (float) headerHeight;

				if (progress < 0.5)
				{
					progress = 0.0f;
				}
				else
				{
					progress = (progress - 0.5f) / 0.5f;
				}

				if (progress > 1.0f)
				{
					progress = 1.0f;
				}

				if (!eyeRefreshLayout.isRefreshing())
				{
					eyeView.setProgress(progress);
				}
			}

			@Override
			public void onFinish()
			{
				eyeView.stopAnimate();
			}
		});

		eyeRefreshLayout.setOnLoadListener(new EyeRefreshLayout.OnLoadListener()
		{
			@Override
			public int onStart()
			{
				progressView.startAnimate();
				return 3000;
			}

			@Override
			public void onLoad(int footerHeight, int pullHeight)
			{
				float progress = (float) pullHeight / (float) footerHeight;

				if (progress < 0.5)
				{
					progress = 0.0f;
				}
				else
				{
					progress = (progress - 0.5f) / 0.5f;
				}

				if (progress > 1.0f)
				{
					progress = 1.0f;
				}

				if (!eyeRefreshLayout.isRefreshing())
				{
					progressView.setProgress(progress);
				}
			}

			@Override
			public void onFinish()
			{
				progressView.stopAnimate();
			}
		});
	}

	private void initView()
	{
		eyeRefreshLayout = (EyeRefreshLayout) this.findViewById(R.id.eyeRefreshLayout);
		progressView = (YProgressView) this.findViewById(R.id.progressView);
		eyeView = (EyeView) this.findViewById(R.id.eyeView);

		listView = (ListView) this.findViewById(R.id.view_list);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterData);
		listView.setAdapter(adapter);

		eyeRefreshLayout.setListView(listView);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, MainActivity.class));
	}
}
