package com.multi.filter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.multi.filter.R;
import com.multi.filter.receiver.TestReceiver;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

/**
 * 不会分层
 */
public class MainActivity extends BaseAppCompatActivity
{
	private TestReceiver testReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initReceiver(MainActivity.this);

		findViewById(R.id.btn_send_one).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_send_one");
				sendBroadcast(new Intent(TestReceiver.ONE_1));
			}
		});
		findViewById(R.id.btn_send_two).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_send_two");
				sendBroadcast(new Intent(TestReceiver.TWO_1));
			}
		});
		findViewById(R.id.btn_send_three).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_send_three");
				sendBroadcast(new Intent(TestReceiver.THREE_1));
			}
		});
		findViewById(R.id.btn_send_four).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_send_four");
				sendBroadcast(new Intent(TestReceiver.FOUR_1));
			}
		});
	}

	/**
	 * 这里是用来测试,通过多次新建过滤器的方式,试试看是否会分层
	 * 结果:不会分层
	 * @param context
	 */
	private void initReceiver(Context context)
	{
		testReceiver = new TestReceiver();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TestReceiver.ONE_1);
		intentFilter.addAction(TestReceiver.ONE_2);
		context.registerReceiver(testReceiver, intentFilter);

		intentFilter = new IntentFilter();
		intentFilter.addAction(TestReceiver.TWO_1);
		intentFilter.addAction(TestReceiver.TWO_2);
		intentFilter.addAction(TestReceiver.TWO_3);
		context.registerReceiver(testReceiver, intentFilter);

		intentFilter = new IntentFilter();
		intentFilter.addAction(TestReceiver.THREE_1);
		intentFilter.addAction(TestReceiver.THREE_2);
		context.registerReceiver(testReceiver, intentFilter);

		intentFilter = new IntentFilter();
		intentFilter.addAction(TestReceiver.FOUR_1);
		context.registerReceiver(testReceiver, intentFilter);
	}
}
