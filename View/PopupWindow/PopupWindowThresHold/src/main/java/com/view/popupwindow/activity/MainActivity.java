package com.view.popupwindow.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.view.popupwindow.R;
import com.yline.base.BaseAppCompatActivity;

import static com.view.popupwindow.R.layout.popupwindow_out;

public class MainActivity extends BaseAppCompatActivity
{
	private Button btnShow, btnHind, btnChange;

	private TextView tvAim;

	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow;

	private LinearLayout popupView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvAim = (TextView) findViewById(R.id.tv_action_aim);
		btnHind = (Button) findViewById(R.id.btn_action_hide);
		btnShow = (Button) findViewById(R.id.btn_action_show);
		btnChange = (Button) findViewById(R.id.btn_change_view);
		// 点击按钮弹出菜单

		btnShow.setOnClickListener(new View.OnClickListener()
		{ // 点击弹出
			@Override
			public void onClick(View v)
			{
				getPopupWindow();
				// 设置PopupWindow的出现位置	都是popupWindow.show...一共四个方法


				showPopupWindow();
			}
		});

		btnHind.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hidePopupWindow();
			}
		});

		btnChange.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				changePopupWindowView();
			}
		});
	}

	private void showPopupWindow()
	{
		if (null == popupWindow || !popupWindow.isShowing())
		{
			// 获取自定义的视图
			popupView = (LinearLayout) LayoutInflater.from(this).inflate(popupwindow_out, null);

			// 创建PopupWindow实例;这个后面加上true,就会导致其它位置不可点击
			popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

			// 设置动画效果	不设置的话，就闪现和闪出
			// popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

			// 手动设置点击其他地方消失
		/*popupView.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (popupWindow != null && popupWindow.isShowing())
				{
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});*/

			popupWindow.showAsDropDown(tvAim);
		}
	}

	private void changePopupWindowView()
	{
		if (null != popupWindow && popupWindow.isShowing())
		{
			View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_change, null);
			popupView.removeAllViews();
			popupView.addView(view);
			// popupWindow.update();
			// popupWindow.update(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			// popupWindow.setContentView(popupView);
			// popupWindow.update();
		}
	}

	private void hidePopupWindow()
	{
		if (null != popupWindow)
		{
			popupWindow.dismiss();
		}
	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow()
	{
	}

	/**
	 * 创建PopupWindow
	 */
	private void initPopupWindow()
	{

	}
}
