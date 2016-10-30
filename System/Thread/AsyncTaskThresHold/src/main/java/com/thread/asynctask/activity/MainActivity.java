package com.thread.asynctask.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thread.asynctask.ProgressBarAsyncTask;
import com.thread.asynctask.R;
import com.yline.log.LogFileUtil;

public class MainActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView tvAsyncClass = (TextView) findViewById(R.id.tv_async_class);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_async_class);
		findViewById(R.id.btn_async_class).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_async_class");
				ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(tvAsyncClass, progressBar);
				asyncTask.execute(10, 20);
			}
		});
		
		final TextView tvAsyncNew = (TextView) findViewById(R.id.tv_async_new);
		findViewById(R.id.btn_async_new).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_async_new");
				new AsyncTask<Integer, Integer, String>()
				{
					@Override
					protected String doInBackground(Integer[] params)
					{
						float i = 0;
						for (; i < params[1]; i++)
						{
							Log.i("config", "" + i * 100 / params[1]);
							try
							{  // 模拟 耗时的操作
								Thread.sleep(400);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							int process = (int) ((i + 1) * 100 / params[1]);
							publishProgress(process);  // 传值到 onProgressUpdate
						}
						return i + "";
					}

					@Override
					protected void onProgressUpdate(Integer... values)
					{
						int value = values[0];
						tvAsyncNew.setText("当前进度: " + value + "%");
					}

					@Override
					protected void onPostExecute(String s)
					{
						super.onPostExecute(s);
						tvAsyncNew.setText("进度结束！！！");
					}
				}.execute(10, 20);
				// .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); 修改成一下语句,就可以同时执行两个AsyncTask
			}
		});
	}

}
