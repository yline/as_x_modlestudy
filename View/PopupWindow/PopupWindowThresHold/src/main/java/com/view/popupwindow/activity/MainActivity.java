package com.view.popupwindow.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.view.popupwindow.R;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	private Button btnPopup;

	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 点击按钮弹出菜单
		btnPopup = (Button) findViewById(R.id.btn_popup);
		btnPopup.setOnClickListener(new View.OnClickListener()
		{ // 点击弹出
			@Override
			public void onClick(View v)
			{
				getPopupWindow();
				// 设置PopupWindow的出现位置	都是popupWindow.show...一共四个方法
				popupWindow.showAsDropDown(v);
			}
		});
	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow()
	{
		if (null != popupWindow)
		{
			popupWindow.dismiss();
		}
		else
		{
			initPopupWindow(MainActivity.this);
		}
	}

	/**
	 * 创建PopupWindow
	 */
	private void initPopupWindow(Context context)
	{
		// 获取自定义的视图
		View popupWindow_view = LayoutInflater.from(context).inflate(R.layout.popupwindow_out, null, false);
		// 创建PopupWindow实例
		popupWindow = new PopupWindow(popupWindow_view,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果	不设置的话，就闪现和闪出
		// popupWindow.setAnimationStyle(R.style.popwindow_AnimationFade);

		// 设置了背景，点击后退或者其他区域会退出，否则不会
		// 这现象解释：http://www.cnblogs.com/mengdd/p/3569127.html		并解释了 不消失向下传递点击事件的方法
		// popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));

		// 手动设置点击其他地方消失
		popupWindow_view.setOnTouchListener(new View.OnTouchListener()
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
		});
	}
}
