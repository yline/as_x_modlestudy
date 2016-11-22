package com.listview.eye.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.listview.eye.R;
import com.listview.eye.view.EyeView;
import com.listview.eye.view.PullDownListView;
import com.listview.eye.view.YProgressView;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
	final String TAG = "MainActivity";

	static String[] adapterData = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final PullDownListView pullDownListView = (PullDownListView) this.findViewById(R.id.pullDownListView);
		final YProgressView progressView = (YProgressView) this.findViewById(R.id.progressView);
		final EyeView eyeView = (EyeView) this.findViewById(R.id.eyeView);

		pullDownListView.setAdapter(mAdapter);

		pullDownListView.setOnPullHeightChangeListener(new PullDownListView.OnPullHeightChangeListener()
		{

			@Override
			public void onTopHeightChange(int headerHeight, int pullHeight)
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

				if (!pullDownListView.isRefreshing())
				{
					eyeView.setProgress(progress);
				}
			}

			@Override
			public void onBottomHeightChange(int footerHeight, int pullHeight)
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

				if (!pullDownListView.isRefreshing())
				{
					progressView.setProgress(progress);
				}
			}

			@Override
			public void onRefreshing(final boolean isTop)
			{
				if (isTop)
				{
					eyeView.startAnimate();
				}
				else
				{
					progressView.startAnimate();
				}

				new Handler().postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						pullDownListView.pullUp();
						if (isTop)
						{
							eyeView.stopAnimate();
						}
						else
						{
							progressView.stopAnimate();
						}
					}

				}, 3000);
			}
		});

		pullDownListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{

			}

		});
	}

	private BaseAdapter mAdapter = new BaseAdapter()
	{

		@Override
		public int getCount()
		{
			return adapterData.length;
		}

		@Override
		public Object getItem(int position)
		{
			return adapterData[position];
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView textView = new TextView(MainActivity.this);
			textView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(MainActivity.this,
					50)));
			textView.setText(adapterData[position]);
			textView.setTextSize(20);
			textView.setTextColor(0xff000000);
			textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			textView.setPadding(50, 0, 0, 0);

			return textView;
		}
	};

	public static int dp2px(Context context, int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
				.getDisplayMetrics());
	}
}
