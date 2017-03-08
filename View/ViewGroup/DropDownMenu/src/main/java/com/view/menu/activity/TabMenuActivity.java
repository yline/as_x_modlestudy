package com.view.menu.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.view.menu.R;
import com.view.menu.view.TabDownMenuHelper;
import com.view.menu.view.TabMenuHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabMenuActivity extends AppCompatActivity
{
	private TabMenuHelper mainMenuHelper;

	private String headers[] = {"城市", "年龄", "性别", "星座"};

	private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州",
			"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州",
			"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};

	private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};

	private String sexs[] = {"不限", "男", "女"};

	private String constellations[] = {"不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_menu);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_down_menu);

		mainMenuHelper = new TabMenuHelper();

		// 初始化控件
		View cityView = mainMenuHelper.initCityView(this);
		View ageView = mainMenuHelper.initAgeView(this);
		View sexView = mainMenuHelper.initSexView(this);
		View constellationView = mainMenuHelper.initConstellationView(this);

		List<View> popupViews = new ArrayList<>();
		popupViews.add(cityView);
		popupViews.add(ageView);
		popupViews.add(sexView);
		popupViews.add(constellationView);

		TabDownMenuHelper tabDownMenu = new TabDownMenuHelper();
		tabDownMenu.setDropDownMenu(this, tabLayout, Arrays.asList(headers), popupViews);

		initMenuData();
	}

	public void initMenuData()
	{
		mainMenuHelper.setCityData(citys);
		mainMenuHelper.setAgeData(ages);
		mainMenuHelper.setSexData(sexs);
		mainMenuHelper.setConstellationData(constellations);
	}
}
