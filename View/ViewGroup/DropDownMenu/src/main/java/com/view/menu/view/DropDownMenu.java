package com.view.menu.view;

import android.animation.ObjectAnimator;
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

import java.util.List;

public class DropDownMenu extends LinearLayout
{
	private static final int DURATION_ANIMATOR = 250;

	// 顶部菜单布局
	private LinearLayout tabMenuView;
	
	// 底部容器，包含popupMenuViews，maskView
	private PopupWindow popupWindow;

	// 底部容器具体View，contentView，maskView
	private LinearLayout popupView;

	// 可更换的内容
	private FrameLayout contentView;

	// 遮罩半透明View，点击可关闭DropDownMenu
	private View maskView;

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

		// 初始化PopupWindow
		popupWindow = new PopupWindow(context);
		popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

		// 初始化PopupView
		popupView = new LinearLayout(context);
		LinearLayout.LayoutParams popupParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popupView.setLayoutParams(popupParams);

		// 将其添加到popupWindow
		popupWindow.setContentView(popupView);

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

		contentView = new FrameLayout(getContext());
		contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		popupView.addView(contentView, 0);
		contentView.setVisibility(View.GONE);

		maskView = new View(getContext());
		maskView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		maskView.setBackgroundColor(maskColor);
		maskView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				closeMenu();
			}
		});
		popupView.addView(maskView, 1);
		maskView.setVisibility(View.GONE);

		for (int i = 0; i < arrayView.size(); i++)
		{
			arrayView.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			contentView.addView(arrayView.get(i), i);
		}
	}

	private void addTab(@NonNull List<String> tabTexts, int i)
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
				switchMenu(tab);
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
		if (current_tab_position != -1)
		{
			((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
			((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);

			// popupWindow.dismiss();

			ObjectAnimator popupAnimator = ObjectAnimator.ofFloat(popupView, "translationY", 0, -popupView.getHeight());
			popupAnimator.setDuration(DURATION_ANIMATOR);
			popupAnimator.start();
			/*
			ObjectAnimator maskAnimator = ObjectAnimator.ofFloat(maskView, "alpha", 1f, 0f);
			maskAnimator.setDuration(DURATION_ANIMATOR);
			maskAnimator.start();*/

			current_tab_position = -1;
		}
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
	 * @param target
	 */
	private void switchMenu(View target)
	{
		System.out.println(current_tab_position);
		for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2)
		{
			if (target == tabMenuView.getChildAt(i))
			{
				if (current_tab_position == i)
				{
					closeMenu();
				}
				else
				{
					if (current_tab_position == -1)
					{

						popupView.setVisibility(View.VISIBLE);
						ObjectAnimator popupAnimator = ObjectAnimator.ofFloat(popupView, "translationY", -popupView.getHeight(), 0);
						popupAnimator.setDuration(DURATION_ANIMATOR);
						popupAnimator.start();

						maskView.setVisibility(VISIBLE);
						ObjectAnimator maskAnimator = ObjectAnimator.ofFloat(maskView, "alpha", 0f, 1f);
						maskAnimator.setDuration(DURATION_ANIMATOR);
						maskAnimator.start();

						contentView.getChildAt(i / 2).setVisibility(View.VISIBLE);
					}
					else
					{
						contentView.getChildAt(i / 2).setVisibility(View.VISIBLE);
					}
					current_tab_position = i;
					((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
					((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
							getResources().getDrawable(menuSelectedIcon), null);
				}
			}
			else
			{
				((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
				((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
						getResources().getDrawable(menuUnselectedIcon), null);
				/*
				popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
				*/
			}
		}
	}

	public int dpTpPx(float value)
	{
		DisplayMetrics dm = getResources().getDisplayMetrics();
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
	}
}
