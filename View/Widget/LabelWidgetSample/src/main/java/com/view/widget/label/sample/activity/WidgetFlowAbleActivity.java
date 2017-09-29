package com.view.widget.label.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.view.widget.label.WidgetFlowAble;
import com.view.widget.label.sample.R;
import com.view.widget.label.widget.FlowLayout;
import com.view.widget.label.widget.LabelAdapter;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class WidgetFlowAbleActivity extends BaseAppCompatActivity
{
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, WidgetFlowAbleActivity.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_widget_flow_able);

		final WidgetFlowAble widgetFlowAble = new WidgetFlowAble(this, R.id.label_flow);
		widgetFlowAble.addAllDataList(StrConstant.getListThree(10));

		widgetFlowAble.setLabelGravity(FlowLayout.LabelGravity.CENTER); // 设置居中布局
		widgetFlowAble.setMaxCountEachLine(3);  // 每行最大个数

		// 设置选择最多和最少数，和自动点亮操作有冲突，因此会现象异常(暂时不处理)
		widgetFlowAble.setMinSelectCount(2); // 最少选择2个； 这个需要配合使用初始化
		// widgetFlowAble.setMaxSelectCount(3); // 最多选择3个 （这个和点亮所有 有冲突）

		// 配合默认选择两个
		widgetFlowAble.addSelectedPosition(0);
		widgetFlowAble.addSelectedPosition(1);

		widgetFlowAble.setClickable(true); // 默认也是可以点击的

		widgetFlowAble.setOnLabelClickListener(new LabelAdapter.OnLabelClickListener<String>()
		{
			@Override
			public boolean onLabelClick(FlowLayout container, View view, String string, int position)
			{
				SDKManager.toast("Click:" + string + ",position = " + position);
				return false;
			}
		});
		widgetFlowAble.setOnLabelSelectListener(new LabelAdapter.OnLabelSelectListener()
		{
			@Override
			public void onLabelSelected(Deque selectedDeque)
			{
				setTitle("Select:" + selectedDeque.toString());
			}
		});

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_flow_able);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				switch (tab.getPosition())
				{
					case 0:
						widgetFlowAble.addData(StrConstant.getStringByRandom(StrConstant.getStringArrayByRandom()));
						break;
					case 1:
						int length = widgetFlowAble.getDataSize();
						widgetFlowAble.removeData(length - 1);
						break;
					case 2:
						widgetFlowAble.addSelectedPosition(0);
						break;
					case 3:
						int lengthThree = widgetFlowAble.getDataSize();
						List<Integer> dataArray = new ArrayList<>();
						for (int i = 0; i < lengthThree; i++)
						{
							dataArray.add(i);
						}

						widgetFlowAble.addAllSelectedPosition(dataArray);
						break;
					case 4:
						int lengthFour = widgetFlowAble.getDataSize();
						widgetFlowAble.removeSelectedPosition(lengthFour - 1);
						break;
					case 5:
						widgetFlowAble.clearSelectedPosition();
						break;
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{

			}
		});
		tabLayout.addTab(tabLayout.newTab().setText("Add"));
		tabLayout.addTab(tabLayout.newTab().setText("Remove"));
		tabLayout.addTab(tabLayout.newTab().setText("点亮第1个"));
		tabLayout.addTab(tabLayout.newTab().setText("点亮所有"));
		tabLayout.addTab(tabLayout.newTab().setText("暗掉最后1个"));
		tabLayout.addTab(tabLayout.newTab().setText("暗掉所有"));
		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
	}
}
