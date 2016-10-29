package com.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.utils.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * 调整 UI 视图大小, 选择那个方法依据的是 其对应的上一层框体
 * 比例为系统参数
 */
public class UIResizeUtil
{
	public static final int designWidth = 640; // 设计图宽度

	private static int AppWidth = 0; // 宽度适配

	// private static int AppHeight = 0;	// 高度适配

	public UIResizeUtil()
	{
		/** 实例化失败 */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void setLayout(View view, int width, int height)
	{
		if (view.getParent() instanceof FrameLayout)
		{
			setFrame(view, width, height);
		}
		else if (view.getParent() instanceof LinearLayout)
		{
			setLinear(view, width, height);
		}
		else if (view.getParent() instanceof RelativeLayout)
		{
			setRelative(view, width, height);
		}
		else
		{
			LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setLayoutAll parent window error");
		}
	}

	public static void setMargin(View view, int top, int bottom, int left, int right)
	{
		if (view.getParent() instanceof FrameLayout)
		{
			setFrameMargin(view, top, bottom, left, right);
		}
		else if (view.getParent() instanceof LinearLayout)
		{
			setLinearMargin(view, top, bottom, left, right);
		}
		else if (view.getParent() instanceof RelativeLayout)
		{
			setRelativeMargin(view, top, bottom, left, right);
		}
		else
		{
			LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setMarginAll parent window error");
		}
	}

	public static void setLinear(View view, int width, int height)
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			lp = new LinearLayout.LayoutParams(1, 1);
		}

		lp.width = width * getAppWidth() / designWidth;
		lp.height = height * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setLinearMargin(View view, int top, int bottom, int left, int right)
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setLinearMargin lp is null");
			return;
		}

		lp.topMargin = top * getAppWidth() / designWidth;
		lp.bottomMargin = bottom * getAppWidth() / designWidth;
		lp.leftMargin = left * getAppWidth() / designWidth;
		lp.rightMargin = right * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setRelative(View view, int width, int height)
	{
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			lp = new RelativeLayout.LayoutParams(1, 1);
		}

		lp.width = width * getAppWidth() / designWidth;
		lp.height = height * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setRelativeMargin(View view, int top, int bottom, int left, int right)
	{
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setRelativeMargin lp is null");
			return;
		}

		lp.topMargin = top * getAppWidth() / designWidth;
		lp.bottomMargin = bottom * getAppWidth() / designWidth;
		lp.leftMargin = left * getAppWidth() / designWidth;
		lp.rightMargin = right * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setFrame(View view, int width, int height)
	{
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			lp = new FrameLayout.LayoutParams(1, 1);
		}

		lp.width = width * getAppWidth() / designWidth;
		lp.height = height * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setFrameMargin(View view, int top, int bottom, int left, int right)
	{
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setFrameMargin lp is null");
			return;
		}

		lp.topMargin = top * getAppWidth() / designWidth;
		lp.bottomMargin = bottom * getAppWidth() / designWidth;
		lp.leftMargin = left * getAppWidth() / designWidth;
		lp.rightMargin = right * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setViewGroup(View view, int width, int height)
	{
		android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();

		if (null == lp)
		{
			lp = new android.view.ViewGroup.LayoutParams(1, 1);
		}

		lp.width = width * getAppWidth() / designWidth;
		lp.height = height * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	public static void setRadioGroup(View view, int width, int height)
	{
		android.widget.RadioGroup.LayoutParams lp = (android.widget.RadioGroup.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			lp = new android.widget.RadioGroup.LayoutParams(1, 1);
		}

		lp.width = width * getAppWidth() / designWidth;
		lp.height = height * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	@SuppressWarnings("deprecation")
	public static void setGallery(View view, int width, int height)
	{
		android.widget.Gallery.LayoutParams lp = (android.widget.Gallery.LayoutParams) view.getLayoutParams();

		if (null == lp)
		{
			lp = new android.widget.Gallery.LayoutParams(1, 1);
		}

		lp.width = width * getAppWidth() / designWidth;
		lp.height = height * getAppWidth() / designWidth;

		view.setLayoutParams(lp);
	}

	/**
	 * 获取屏幕宽度,策略为先从缓存中获取
	 *
	 * @return
	 */
	private static int getAppWidth()
	{
		if (AppWidth == 0)
		{
			AppWidth = getAbsoluteScreenWidth(MainApplication.getApplication());
			LogFileUtil.i(MainApplication.TAG, "UIResizeUtils -> getAppWidth width = " + AppWidth);
		}
		return AppWidth;
	}

	/**
	 * 获取当前屏幕的绝对宽度,(排除状态栏、底部栏、横竖屏等因素)
	 *
	 * @return
	 */
	public static int getAbsoluteScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		int widthPixels = displayMetrics.widthPixels;
		int heightPixels = displayMetrics.heightPixels;

		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
		{
			try
			{
				widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
				heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
			}
			catch (IllegalAccessException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth<17 IllegalAccessException", e);
			}
			catch (IllegalArgumentException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth<17 IllegalArgumentException", e);
			}
			catch (InvocationTargetException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth<17 InvocationTargetException", e);
			}
			catch (NoSuchMethodException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth<17 NoSuchMethodException", e);
			}
		}

		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17)
		{
			try
			{
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
				widthPixels = realSize.x;
				heightPixels = realSize.y;
			}
			catch (IllegalAccessException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth>=17 IllegalAccessException", e);
			}
			catch (IllegalArgumentException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth>=17 IllegalArgumentException", e);
			}
			catch (InvocationTargetException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth>=17 InvocationTargetException", e);
			}
			catch (NoSuchMethodException e)
			{
				LogFileUtil.e(MainApplication.TAG, "UIResizeUtil getAbsoluteScreenWidth>=17 NoSuchMethodException", e);
			}
		}

		return Math.min(widthPixels, heightPixels);
	}
}
