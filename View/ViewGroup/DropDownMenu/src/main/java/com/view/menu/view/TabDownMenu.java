package com.view.menu.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.view.menu.R;

import java.util.ArrayList;
import java.util.List;

public class TabDownMenu
{
	// 默认位置
	private static final int CONTENT_VIEW_POSITION = 0;

	// 盖罩 颜色
	private static final int MASK_COLOR = 0x88888888;

	// 承载物
	private PopupWindow popupWindow;

	// window对应的view
	private LinearLayout popupView;

	// popupView 的两个内容
	private View contentView, maskView;

	// tab view
	private List<View> tabViewList;

	// content view
	private List<View> contentViewList;

	private boolean isOpened = false;

	/**
	 * @param context   上下文
	 * @param tabLayout tab标题
	 * @param header    标题信息
	 * @param viewList  pop 内容
	 */
	public void setDropDownMenu(final Context context, final TabLayout tabLayout, List<String> header, List<View> viewList)
	{
		if (header.size() != viewList.size())
		{
			throw new IllegalArgumentException("params not match, header.size() should be equal popupViews.size()");
		}

		initTabView(context, tabLayout, header);

		initPopupWindow(context);

		initContentViewList(context, viewList);
	}

	/**
	 * 初始化Header
	 *
	 * @param context
	 * @param tabLayout
	 */
	private void initTabView(final Context context, final TabLayout tabLayout, List<String> header)
	{
		this.tabViewList = new ArrayList<>();

		for (int i = 0; i < header.size(); i++)
		{
			View tabView = addTab(context, header.get(i));
			tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));

			tabView.setSelected(false);
			tabViewList.add(tabView);
		}
		tabLayout.setSelectedTabIndicatorHeight(0);

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				addMenu(tabLayout, tab.getPosition());
			}

			// 该方法比上面那个方法先执行
			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{
				// close menu
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{
				if (isOpened)
				{
					closeMenu(tab.getPosition());
				}
				else
				{
					addMenu(tabLayout, tab.getPosition());
				}
			}
		});
	}

	/**
	 * 添加 tab
	 *
	 * @param context
	 * @param text
	 * @return
	 */
	private View addTab(Context context, String text)
	{
		View tabView = LayoutInflater.from(context).inflate(R.layout.item_tab_menu, null);

		TextView textView = (TextView) tabView.findViewById(R.id.tv_tab_down_menu);
		textView.setText(text);

		return tabView;
	}

	private void initPopupWindow(Context context)
	{
		if (null == popupWindow)
		{
			popupView = new LinearLayout(context);
			popupView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			popupView.setOrientation(LinearLayout.VERTICAL);

			maskView = new View(context);
			maskView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			maskView.setBackgroundColor(MASK_COLOR);

			contentView = new View(context);

			popupView.addView(contentView);
			popupView.addView(maskView);

			popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	/**
	 * 添加 menu
	 *
	 * @param tabLayout
	 * @param position
	 */
	private void addMenu(TabLayout tabLayout, int position)
	{
		if (null != popupWindow)
		{
			contentView = contentViewList.get(position);
			popupView.removeViewAt(CONTENT_VIEW_POSITION);
			popupView.addView(contentView, CONTENT_VIEW_POSITION);

			tabViewList.get(position).setSelected(true);
			isOpened = true;

			if (!popupView.isShown())
			{
				popupWindow.showAsDropDown(tabLayout);
			}
		}
	}

	/**
	 * 关闭Menu,并更新状态
	 *
	 * @param position 更新状态位置
	 */
	public void closeMenu(int position)
	{
		if (null != popupWindow && popupWindow.isShowing())
		{
			popupWindow.dismiss();

			tabViewList.get(position).setSelected(false);
			isOpened = false;
		}
	}

	/**
	 * 初始化 contentView
	 *
	 * @param list
	 */
	private void initContentViewList(Context context, List<View> list)
	{
		List<View> views = new ArrayList<>();
		for (int i = 0; i < list.size(); i++)
		{
			TextView textView = new TextView(context);
			textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			textView.setText("position = " + i);
			views.add(textView);
		}

		for (int i = 0; i < list.size(); i++)
		{
			list.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}

		this.contentViewList = list;
	}
}









