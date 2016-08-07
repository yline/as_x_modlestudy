package com.dm.memorandum.uml.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.memorandum.R;
import com.dm.memorandum.uml.caretaker.Caretaker;
import com.dm.memorandum.uml.originator.CallOfDuty;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_memorandum).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CallOfDuty callOfDuty = new CallOfDuty();
				// 玩游戏
				callOfDuty.play();

				Caretaker caretaker = new Caretaker();
				// 游戏存档
				caretaker.archive(callOfDuty.createMemoto());
				// 退出游戏
				callOfDuty.quit();

				CallOfDuty newGame = new CallOfDuty();
				// 恢复游戏
				newGame.restore(caretaker.getMemoto());
			}
		});

	}

}
