package com.view.menu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.view.menu.R;
import com.yline.log.LogFileUtil;

import java.util.List;

public class DropDownMenu extends LinearLayout
{
	private static final int DURATION_ANIMATOR = 250;

	// 顶部菜单布局
	private LinearLayout tabMenuView;
	
	// 底部容器
	private PopupWindow popupWindow;

	// 底部容器具体View，contentView，maskView
	private LinearLayout popupView;

	// 可更换的内容; 遮罩半透明View，点击可关闭DropDownMenu
	private View tempContentView, maskView;

	private List<View> arrayViewList;

	// tabMenuView里面选中的tab位置，-1表示未选中
	private int current_tab_position = -1;

	// 分割线颜色
	private int dividerColor = 0xffcccccc;

	// tab选中颜色
	private int textSelectedColor = 0xff890c85;

	// tab未选中颜色
	private int textUnselectedColor = 0xff111111;

	// 遮罩颜色
	private int maskColor = 0x88888888;

	// tab字体大小
	private int menuTextSize = 14;

	// tab选中图标
	private int menuSelectedIcon = R.drawable.drop_down_selected_icon;

	// tab未选中图标
	private int menuUnselectedIcon = R.drawable.drop_down_unselected_icon;

	public DropDownMenu(Context context)
	{
		super(context, null);
	}

	public DropDownMenu(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		// setOrientation(VERTICAL);

		// 为DropDownMenu添加自定义属性
		int menuBackgroundColor = 0xffffffff;
		int underlineColor = 0xffcccccc;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
		underlineColor = a.getColor(R.styleable.DropDownMenu_xUnderlineColor, underlineColor);
		dividerColor = a.getColor(R.styleable.DropDownMenu_xDividerColor, dividerColor);
		textSelectedColor = a.getColor(R.styleable.DropDownMenu_xTextSelectedColor, textSelectedColor);
		textUnselectedColor = a.getColor(R.styleable.DropDownMenu_xTextUnselectedColor, textUnselectedColor);
		menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_xMenuBackgroundColor, menuBackgroundColor);
		maskColor = a.getColor(R.styleable.DropDownMenu_xMaskColor, maskColor);
		menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_xMenuTextSize, menuTextSize);
		menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_xMenuSelectedIcon, menuSelectedIcon);
		menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_xMenuUnselectedIcon, menuUnselectedIcon);
		a.recycle();

		// 初始化tabMenuView并添加到tabMenuView
		tabMenuView = new LinearLayout(context);
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		tabMenuView.setOrientation(LinearLayout.HORIZONTAL);
		tabMenuView.setBackgroundColor(menuBackgroundColor);
		tabMenuView.setLayoutParams(params);
		addView(tabMenuView, 0);

		// 为tabMenuView添加下划线
		View underLine = new View(getContext());
		underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
		underLine.setBackgroundColor(underlineColor);
		addView(underLine, 1);

		// 初始化PopupView
		popupView = new LinearLayout(getContext());
		LinearLayout.LayoutParams popupParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popupView.setOrientation(LinearLayout.VERTICAL);
		popupView.setLayoutParams(popupParams);

		// 初始化PopupWindow
		popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	/**
	 * 初始化DropDownMenu
	 *
	 * @param tabTexts  tab文字
	 * @param arrayView 具体内容
	 */
	public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> arrayView)
	{
		if (tabTexts.size() != arrayView.size())
		{
			throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
		}

		for (int i = 0; i < tabTexts.size(); i++)
		{
			addTab(tabTexts, i);
		}

		this.arrayViewList = arrayView;

		maskView = new View(getContext());
		maskView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		maskView.setBackgroundColor(maskColor);

		tempContentView = arrayViewList.get(0);
		popupView.addView(tempContentView, 0);
		popupView.addView(maskView, 1);
	}

	private void addTab(@NonNull List<String> tabTexts, final int i)
	{
		final TextView tab = new TextView(getContext());
		tab.setSingleLine();
		tab.setEllipsize(TextUtils.TruncateAt.END);
		tab.setGravity(Gravity.CENTER);
		tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
		tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
		tab.setTextColor(textUnselectedColor);
		tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
		tab.setText(tabTexts.get(i));
		tab.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
		//添加点击事件
		tab.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switchMenu(tab, i);
			}
		});
		tabMenuView.addView(tab);
		//添加分割线
		if (i < tabTexts.size() - 1)
		{
			View view = new View(getContext());
			view.setLayoutParams(new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
			view.setBackgroundColor(dividerColor);
			tabMenuView.addView(view);
		}
	}

	/**
	 * 改变tab文字
	 *
	 * @param text
	 */
	public void setTabText(String text)
	{
		if (current_tab_position != -1)
		{
			((TextView) tabMenuView.getChildAt(current_tab_position)).setText(text);
		}
	}

	/**
	 * 设置 tab是否可点击
	 *
	 * @param clickable
	 */
	public void setTabClickable(boolean clickable)
	{
		for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2)
		{
			tabMenuView.getChildAt(i).setClickable(clickable);
		}
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu()
	{
		if (current_tab_position != -1 && null != popupWindow)
		{
			((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
			((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);

			popupWindow.dismiss();
			current_tab_position = -1;
		}
	}

	/**
	 * 替换成某个位置的菜单
	 *
	 * @param position 新打开的菜单
	 */
	public void updateMenu(int position)
	{
		popupView.removeView(tempContentView);
		tempContentView = arrayViewList.get(position);
		popupView.addView(tempContentView, 0);

		((TextView) tabMenuView.getChildAt(position)).setTextColor(textSelectedColor);
		((TextView) tabMenuView.getChildAt(position)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuSelectedIcon), null);
	}

	/**
	 * DropDownMenu是否处于可见状态
	 *
	 * @return
	 */
	public boolean isShowing()
	{
		return current_tab_position != -1;
	}

	/**
	 * 切换菜单
	 *
	 * @param targetView
	 */
	private void switchMenu(View targetView, int position)
	{
		LogFileUtil.v("position = " + position + ",current_tab_position = " + current_tab_position);

		// 点击的位置 == 之前的位置
		if (position == current_tab_position)
		{
			closeMenu();
		}
		else
		{
			int last = current_tab_position; // 临时替换成当前位置
			for (int i = 0; i < arrayViewList.size(); i++)
			{
				if (i == position)
				{
					updateMenu(position);
				}
				else if (i == last)
				{
					((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
					((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
				}
				else
				{
					// do nothing
				}
			}
		}
	}

	public int dpTpPx(float value)
	{
		DisplayMetrics dm = getResources().getDisplayMetrics();
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
	}
}
