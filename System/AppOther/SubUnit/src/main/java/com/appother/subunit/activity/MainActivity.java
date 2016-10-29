package com.appother.subunit.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.appother.subunit.R;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
	private boolean isNotFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (isNotFirst)
		{
			finish();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			isNotFirst = !isNotFirst;
			MainApplication.toast("再按一次back返回到桌面");
			MainApplication.getHandler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					isNotFirst = !isNotFirst;
				}
			}, 2000);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
