package com.view.menu.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.view.menu.R;
import com.view.menu.widget.DropMenuWidget;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;
import com.yline.base.common.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabMenuActivity extends BaseAppCompatActivity
{
	private String headers[] = {"城市", "筛选"};

	private ProvinceListAdapter provinceListAdapter;

	private DropMenuWidget dropMenuWidget;

	private String provinceList[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州",
			"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州",
			"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_menu);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_widget_drop_menu);

		List<View> contentViewList = new ArrayList<>();

		View provinceView = initAreaView(this);
		contentViewList.add(provinceView);

		View areaView = initFilterView(this);
		contentViewList.add(areaView);

		dropMenuWidget = new DropMenuWidget();
		dropMenuWidget.start(this, Arrays.asList(headers), contentViewList);
		dropMenuWidget.attach(linearLayout);

		provinceListAdapter.set(Arrays.asList(provinceList));
	}

	private View initAreaView(Context context)
	{
		final ListView provinceView = new ListView(context)
		{
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
			{
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(400, MeasureSpec.AT_MOST);
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		};

		provinceView.setDividerHeight(0);
		provinceListAdapter = new ProvinceListAdapter(context);
		provinceView.setAdapter(provinceListAdapter);
		provinceView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				provinceListAdapter.setCheckItem(position);
			}
		});

		return provinceView;
	}

	private View initFilterView(Context context)
	{
		TextView filterView = new TextView(context);
		filterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		filterView.setText("Hokol");
		return filterView;
	}

	private class ProvinceListAdapter extends CommonListAdapter<String>
	{
		private int checkItemPosition = 0;

		public ProvinceListAdapter(Context context)
		{
			super(context);
		}

		public void setCheckItem(int position)
		{
			checkItemPosition = position;
			notifyDataSetChanged();
		}

		@Override
		protected int getItemRes(int i)
		{
			return android.R.layout.simple_list_item_1;
		}

		@Override
		protected void setViewContent(int position, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			TextView textView = viewHolder.get(android.R.id.text1);
			textView.setText(sList.get(position));
			if (checkItemPosition != -1)
			{
				if (checkItemPosition == position)
				{
					textView.setTextColor(sContext.getResources().getColor(android.R.color.holo_red_light));
					textView.setCompoundDrawablesWithIntrinsicBounds(null, null, sContext.getResources().getDrawable(R.drawable.delete_item_menu_checked), null);
				}
				else
				{
					textView.setTextColor(sContext.getResources().getColor(android.R.color.black));
					textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}
		}
	}


	public void onBackPressed()
	{
		//退出activity前关闭菜单
		if (dropMenuWidget.isOpened())
		{
			dropMenuWidget.closeMenu();
		}
		else
		{
			super.onBackPressed();
		}
	}
}
