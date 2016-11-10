package com.udp.demo.activity;

import android.os.Bundle;
import android.view.View;

import com.data.network.udp.instance.R;
import com.udp.demo.helper.UDPUser;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	private UDPUser mUDPUser;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mUDPUser = new UDPUser();
		
		findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_send");
				mUDPUser.sendMessage();
			}
		});

		findViewById(R.id.btn_receiver).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_receiver");
				mUDPUser.receiverMessage();
			}
		});
	}
}
