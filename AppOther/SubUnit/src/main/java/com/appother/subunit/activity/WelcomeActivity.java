package com.appother.subunit.activity;

import android.os.Bundle;

import com.appother.subunit.welcome.style.MoveActionStyle;
import com.appother.subunit.welcome.style.StyleBase;
import com.yline.base.BaseActivity;

public class WelcomeActivity extends BaseActivity
{
	private StyleBase styleBase;

	private STYLE style = STYLE.move;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (style == STYLE.move)
		{
			styleBase = new MoveActionStyle();
		}

		styleBase.init(this);
	}

	public enum STYLE
	{
		move;
	}
}
