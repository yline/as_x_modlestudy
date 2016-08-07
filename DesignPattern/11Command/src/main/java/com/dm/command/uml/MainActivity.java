package com.dm.command.uml;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.command.R;
import com.dm.command.uml.command.ConcreteCommand;
import com.dm.command.uml.command.ICommand;
import com.dm.command.uml.invoker.Invoker;
import com.dm.command.uml.receiver.Receiver;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick btn_action");

				Receiver receiver = new Receiver();

				ICommand iCommand = new ConcreteCommand(receiver);

				Invoker invoker = new Invoker(iCommand);

				invoker.action();
			}
		});
	}
}
