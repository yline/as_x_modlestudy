package com.view.menu.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.view.menu.R;
import com.view.menu.view.DropDownMenu;
import com.view.menu.viewhelper.MainHelper;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	private DropDownMenu dropDownMenu;

	private MainHelper mainHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
	}

	private void initView()
	{
		mainHelper = new MainHelper();
		dropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

		View contentView = LayoutInflater.from(this).inflate(R.layout.activity_main_content, null);
		mainHelper.initMenuView(this, dropDownMenu, contentView);
	}

	private void initData()
	{
		mainHelper.initMenuData();
	}

	@Override
	public void onBackPressed()
	{
		//退出activity前关闭菜单
		if (dropDownMenu.isShowing())
		{
			dropDownMenu.closeMenu();
		}
		else
		{
			super.onBackPressed();
		}
	}
}
