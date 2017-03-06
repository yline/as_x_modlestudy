package com.view.menu.viewhelper;

import android.content.Context;
import android.view.View;

import com.view.menu.view.DropDownMenu;
import com.view.menu.view.DropDownMenuHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainHelper
{
	private DropDownMenuHelper mainMenuHelper;

	private String headers[] = {"城市", "年龄", "性别", "星座"};

	private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州",
			"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州",
			"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};

	private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};

	private String sexs[] = {"不限", "男", "女"};

	private String constellations[] = {"不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

	/**
	 * 装饰模式,对DropDownMenu进行装饰
	 *
	 * @param context
	 * @param dropDownMenu
	 */
	public void initMenuView(Context context, final DropDownMenu dropDownMenu, View contentView)
	{
		mainMenuHelper = new DropDownMenuHelper();

		// 初始化控件
		View cityView = mainMenuHelper.initCityView(context);
		View ageView = mainMenuHelper.initAgeView(context);
		View sexView = mainMenuHelper.initSexView(context);
		View constellationView = mainMenuHelper.initConstellationView(context);

		List<View> popupViews = new ArrayList<>();
		popupViews.add(cityView);
		popupViews.add(ageView);
		popupViews.add(sexView);
		popupViews.add(constellationView);

		mainMenuHelper.setOnDropMenuClickListener(new DropDownMenuHelper.OnDropMenuClickListener()
		{
			@Override
			public void onCityClick(int position)
			{
				dropDownMenu.setTabText(position == 0 ? headers[0] : citys[position]);
				dropDownMenu.closeMenu();
			}

			@Override
			public void onAgeClick(int position)
			{
				dropDownMenu.setTabText(position == 0 ? headers[1] : ages[position]);
				dropDownMenu.closeMenu();
			}

			@Override
			public void onSexClick(int position)
			{
				dropDownMenu.setTabText(position == 0 ? headers[2] : sexs[position]);
				dropDownMenu.closeMenu();
			}

			@Override
			public void onConstellationClick(int position)
			{
				dropDownMenu.setTabText(position == 0 ? headers[3] : constellations[position]);
				dropDownMenu.closeMenu();
			}
		});

		dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
	}

	public void initMenuData()
	{
		mainMenuHelper.setCityData(citys);
		mainMenuHelper.setAgeData(ages);
		mainMenuHelper.setSexData(sexs);
		mainMenuHelper.setConstellationData(constellations);
	}
}
