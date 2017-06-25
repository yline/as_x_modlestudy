package com.okhttp.php.activity;

import android.os.Bundle;
import android.view.View;

import com.okhttp.php.R;
import com.okhttp.php.application.AppStateManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.view.recycler.holder.ViewHolder;

public class EnterActivity extends BaseAppCompatActivity
{
	private ViewHolder viewHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);

		viewHolder = new ViewHolder(this);

		String savedIp = AppStateManager.getInstance().getServerIp();
		viewHolder.setText(R.id.et_enter_ip, savedIp);
		
		int savedPort = AppStateManager.getInstance().getServerPort();
		viewHolder.setText(R.id.et_enter_port, savedPort + "");

		viewHolder.setOnClickListener(R.id.btn_enter_ip, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String inputIp = viewHolder.getText(R.id.et_enter_ip);
				String portStr = viewHolder.getText(R.id.et_enter_port).toString().trim();
				int inputPort = Integer.parseInt(portStr);

				AppStateManager.getInstance().setServerIp(inputIp, inputPort);
				LogFileUtil.v("inputIp = " + inputIp + ", inputPort = " + inputPort);

				MainActivity.actionStart(EnterActivity.this);
			}
		});
		viewHolder.setOnClickListener(R.id.btn_enter_pass, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String inputIp = AppStateManager.getInstance().getServerIp();
				int inputPort = AppStateManager.getInstance().getServerPort();

				LogFileUtil.v("inputIp = " + inputIp + ", inputPort = " + inputPort);

				MainActivity.actionStart(EnterActivity.this);
			}
		});
	}
}
