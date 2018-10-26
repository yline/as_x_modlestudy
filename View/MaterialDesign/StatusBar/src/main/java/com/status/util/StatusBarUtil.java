package com.status.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.status.R;
import com.yline.utils.UIScreenUtil;

/**
 * 侵入式标题栏（状态栏）
 * 整体思路：
 * 1，通过Window清除原有标题栏 或直接设置颜色
 * 2，如果内容产生了偏移，则手动设置偏移量（Margin 或 padding 或 fitsSystemWindows）
 * 3，若步骤1中，原有标题栏被清除，则通过设置一个状态栏高度的View从而设置透明度
 *
 * @author yline 2018/10/26 -- 18:52
 */
public class StatusBarUtil {
	private static final int DEFAULT_ALPHA = 0xFF; // 设置全透明，表明底部颜色全部展示出来
	
	private static final int TAG_OFFSET_CONTENT = -123;
	
	private static final int ID_STATUS_BAR = R.id.statusbarutil_id_status_bar;
	
	private static final int ID_TRANSLUCENT = R.id.statusbarutil_id_translucent;
	
	/**
	 * 设置状态栏全透明
	 *
	 * @param activity 需要设置的activity
	 */
	public static void setTranslucent(Activity activity) {
		setTranslucentInner(activity, -1);
	}
	
	/**
	 * 使状态栏半透明
	 *
	 * @param activity 需要设置的activity
	 * @param alpha    状态栏透明度
	 */
	public static void setTranslucent(Activity activity, int alpha) {
		setTranslucentInner(activity, alpha);
	}
	
	/**
	 * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
	 *
	 * @param activity       需要设置的activity
	 * @param needOffsetView 需要向下偏移的 View
	 */
	public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
		setTranslucentForImageViewInner(activity, needOffsetView, -1);
	}
	
	/**
	 * 为头部是 ImageView 的界面设置状态栏透明
	 *
	 * @param activity       需要设置的activity
	 * @param needOffsetView 需要向下偏移的 View
	 * @param statusBarAlpha 状态栏透明度
	 */
	public static void setTranslucentForImageView(Activity activity, View needOffsetView, int statusBarAlpha) {
		setTranslucentForImageViewInner(activity, needOffsetView, statusBarAlpha);
	}
	
	/**
	 * 为 DrawerLayout 布局设置状态栏透明
	 *
	 * @param activity     需要设置的activity
	 * @param drawerLayout DrawerLayout
	 */
	public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
		setTranslucentForDrawerLayoutInner(activity, drawerLayout, -1);
	}
	
	/**
	 * 为 DrawerLayout 布局设置状态栏透明
	 *
	 * @param activity     需要设置的activity
	 * @param drawerLayout DrawerLayout
	 * @param alpha        状态栏透明度
	 */
	public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int alpha) {
		setTranslucentForDrawerLayoutInner(activity, drawerLayout, alpha);
	}
	
	/**
	 * 设置状态栏颜色
	 *
	 * @param activity 需要设置的 activity
	 * @param color    状态栏颜色值（RGB）
	 */
	public static void setColor(Activity activity, @ColorInt int color) {
		setColorInner(activity, color, DEFAULT_ALPHA);
	}
	
	/**
	 * 设置状态栏颜色
	 *
	 * @param activity 需要设置的activity
	 * @param color    状态栏颜色值（RGB）
	 * @param alpha    状态栏透明度（0xff）
	 */
	public static void setColor(Activity activity, @ColorInt int color, int alpha) {
		setColorInner(activity, color, alpha);
	}
	
	/**
	 * 为滑动返回界面设置状态栏颜色
	 *
	 * @param activity 需要设置的activity
	 * @param color    状态栏颜色值
	 */
	public static void setColorForCoordinatorLayout(Activity activity, @ColorInt int color) {
		setColorForCoordinatorLayoutInner(activity, color, DEFAULT_ALPHA);
	}
	
	/**
	 * 为滑动返回界面设置状态栏颜色
	 *
	 * @param activity 需要设置的activity
	 * @param color    状态栏颜色值
	 * @param alpha    状态栏透明度
	 */
	public static void setColorForCoordinatorLayout(Activity activity, @ColorInt int color, int alpha) {
		setColorForCoordinatorLayoutInner(activity, color, alpha);
	}
	
	/**
	 * 为DrawerLayout 布局设置状态栏颜色,纯色
	 *
	 * @param activity     需要设置的activity
	 * @param drawerLayout DrawerLayout
	 * @param color        状态栏颜色值
	 */
	public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
		setColorForDrawerLayoutInner(activity, drawerLayout, color, DEFAULT_ALPHA);
	}
	
	/**
	 * 为DrawerLayout 布局设置状态栏变色
	 *
	 * @param activity     需要设置的activity
	 * @param drawerLayout DrawerLayout
	 * @param color        状态栏颜色值
	 * @param alpha        状态栏透明度
	 */
	public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color, int alpha) {
		setColorForDrawerLayoutInner(activity, drawerLayout, color, alpha);
	}
	
	/* ------------------------------------------ 实现各个功能 ----------------------------------------- */
	
	/**
	 * 为DrawerLayout 布局设置状态栏变色
	 *
	 * @param activity     需要设置的activity
	 * @param drawerLayout DrawerLayout
	 * @param color        状态栏颜色值
	 * @param alpha        状态栏透明度
	 */
	private static void setColorForDrawerLayoutInner(Activity activity, DrawerLayout drawerLayout, @ColorInt int color, int alpha) {
		// 清除透明栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		// 设置偏移量
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
			View fakeStatusBarView = contentLayout.findViewById(ID_STATUS_BAR);
			if (fakeStatusBarView != null) {
				if (fakeStatusBarView.getVisibility() == View.GONE) {
					fakeStatusBarView.setVisibility(View.VISIBLE);
				}
				fakeStatusBarView.setBackgroundColor(color);
			} else {
				View statusBarView = new View(activity);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.getStatusHeight(activity));
				statusBarView.setLayoutParams(params);
				statusBarView.setBackgroundColor(calculateColor(color, 0xFF));
				statusBarView.setId(ID_STATUS_BAR);
				
				contentLayout.addView(statusBarView, 0);
			}
			
			// 设置padding top
			if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
				contentLayout.getChildAt(1)
						.setPadding(contentLayout.getPaddingLeft(), UIScreenUtil.getStatusHeight(activity) + contentLayout.getPaddingTop(),
								contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
			}
			// 设置内容属性
			ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
			drawerLayout.setFitsSystemWindows(false);
			contentLayout.setFitsSystemWindows(false);
			contentLayout.setClipToPadding(true);
			drawer.setFitsSystemWindows(false);
			
			addTranslucentView(activity, alpha);
		}
	}
	
	/**
	 * 为滑动返回界面设置状态栏颜色
	 *
	 * @param activity 需要设置的activity
	 * @param color    状态栏颜色值
	 * @param alpha    状态栏透明度
	 */
	private static void setColorForCoordinatorLayoutInner(Activity activity, @ColorInt int color, int alpha) {
		// 清除标题栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.setStatusBarColor(Color.TRANSPARENT);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		// 设置偏移量，PaddingTop方式实现
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ViewGroup contentView = activity.findViewById(android.R.id.content);
			View rootView = contentView.getChildAt(0);
			int statusBarHeight = UIScreenUtil.getStatusHeight(activity);
			
			if (rootView instanceof CoordinatorLayout) {
				final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView;
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
					coordinatorLayout.setFitsSystemWindows(false);
					contentView.setBackgroundColor(calculateColor(color, alpha));
					boolean isNeedRequestLayout = contentView.getPaddingTop() < statusBarHeight;
					if (isNeedRequestLayout) {
						contentView.setPadding(0, statusBarHeight, 0, 0);
						coordinatorLayout.post(new Runnable() {
							@Override
							public void run() {
								coordinatorLayout.requestLayout();
							}
						});
					}
				} else {
					coordinatorLayout.setStatusBarBackgroundColor(calculateColor(color, alpha));
				}
			} else {
				contentView.setPadding(0, statusBarHeight, 0, 0);
				contentView.setBackgroundColor(calculateColor(color, alpha));
			}
		}
	}
	
	/**
	 * 设置状态栏颜色
	 *
	 * @param activity 需要设置的activity
	 * @param color    状态栏颜色值（RGB）
	 * @param alpha    状态栏透明度（0xff）
	 */
	private static void setColorInner(Activity activity, @ColorInt int color, int alpha) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(calculateColor(color, alpha));
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = activity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			
			// 设置偏移量
			ViewGroup decorView = (ViewGroup) window.getDecorView();
			View fakeStatusBarView = decorView.findViewById(ID_STATUS_BAR);
			if (fakeStatusBarView != null) {
				if (fakeStatusBarView.getVisibility() == View.GONE) {
					fakeStatusBarView.setVisibility(View.VISIBLE);
				}
				fakeStatusBarView.setBackgroundColor(calculateColor(color, alpha));
			} else {
				View statusBarView = new View(activity);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.getStatusHeight(activity));
				statusBarView.setLayoutParams(params);
				statusBarView.setBackgroundColor(calculateColor(color, alpha));
				statusBarView.setId(ID_STATUS_BAR);
				
				decorView.addView(statusBarView);
			}
			
			fitsSystemWindows(activity);
		}
	}
	
	/**
	 * 为 DrawerLayout 布局设置状态栏透明
	 *
	 * @param activity     需要设置的activity
	 * @param drawerLayout DrawerLayout
	 * @param alpha        状态栏透明度
	 */
	private static void setTranslucentForDrawerLayoutInner(Activity activity, DrawerLayout drawerLayout, int alpha) {
		// 清除状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		// 布局下移
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
			// 内容布局不是 LinearLayout 时,设置padding top
			if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
				contentLayout.getChildAt(1).setPadding(0, UIScreenUtil.getStatusHeight(activity), 0, 0);
			}
			
			// 设置属性
			ViewGroup drawerChild = (ViewGroup) drawerLayout.getChildAt(1);
			drawerLayout.setFitsSystemWindows(false);
			contentLayout.setFitsSystemWindows(false);
			contentLayout.setClipToPadding(true);
			drawerChild.setFitsSystemWindows(false);
		}
		
		if (alpha >= 0) {
			addTranslucentView(activity, alpha);
		}
	}
	
	/**
	 * 设置状态栏透明
	 * 适用于：状态栏背景是某部分子内容的背景，将其他部分的内容，当做内容布局
	 *
	 * @param activity    需要设置的activity
	 * @param contentView 需要向下偏移的 View
	 * @param alpha       状态栏透明度
	 */
	private static void setTranslucentForImageViewInner(Activity activity, View contentView, int alpha) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.setStatusBarColor(Color.TRANSPARENT);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 相当于给contentView，设置MarginTop一个状态栏高度
			Object oldTag = contentView.getTag(TAG_OFFSET_CONTENT);
			if (null == oldTag || !(Boolean) oldTag) {
				ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
				if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
					ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
					params.setMargins(params.leftMargin, params.topMargin + UIScreenUtil.getStatusHeight(activity), params.rightMargin, params.bottomMargin);
					contentView.setTag(TAG_OFFSET_CONTENT, true);
				}
			}
			
			if (alpha >= 0) {
				addTranslucentView(activity, alpha);
			}
		}
	}
	
	/**
	 * 设置状态栏透明度
	 * 适用于：根布局颜色(图片)就是状态栏的背景，将根布局填充到状态栏
	 *
	 * @param activity 设置的Activity
	 * @param alpha    状态栏透明度
	 */
	private static void setTranslucentInner(Activity activity, int alpha) {
		// 清除状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			fitsSystemWindows(activity); // 相当于 设置PaddingTop一个状态栏高度
			if (alpha >= 0) {
				addTranslucentView(activity, alpha); // 给状态栏设置 遮盖的View，然后设置透明度
			}
		}
	}
	
	
	/**
	 * 让内容布局，适合屏幕，实际上效果类似于：将根布局paddingTop 状态栏的高度
	 *
	 * @param activity 上下文
	 */
	private static void fitsSystemWindows(Activity activity) {
		ViewGroup parent = activity.findViewById(android.R.id.content);
		for (int i = 0, count = parent.getChildCount(); i < count; i++) {
			View childView = parent.getChildAt(i);
			if (childView instanceof ViewGroup) {
				childView.setFitsSystemWindows(true);
				((ViewGroup) childView).setClipToPadding(true);
			}
		}
	}
	
	/**
	 * 添加半透明矩形条
	 *
	 * @param activity       需要设置的 activity
	 * @param statusBarAlpha 透明值（RGB）
	 */
	private static void addTranslucentView(Activity activity, int statusBarAlpha) {
		ViewGroup contentView = activity.findViewById(android.R.id.content);
		View fakeTranslucentView = contentView.findViewById(ID_TRANSLUCENT);
		if (fakeTranslucentView != null) {
			if (fakeTranslucentView.getVisibility() == View.GONE) {
				fakeTranslucentView.setVisibility(View.VISIBLE);
			}
			fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
		} else {
			View statusBarView = new View(activity);
			LinearLayout.LayoutParams params =
					new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIScreenUtil.getStatusHeight(activity));
			statusBarView.setLayoutParams(params);
			statusBarView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
			statusBarView.setId(ID_TRANSLUCENT);
			
			contentView.addView(statusBarView);
		}
	}
	
	/**
	 * alpha + rgb = argb 计算状态栏颜色
	 *
	 * @param color color值
	 * @param alpha alpha值[值越大，底色越不透明，表现出的透明度越高]
	 * @return 最终的状态栏颜色
	 */
	private static int calculateColor(@ColorInt int color, int alpha) {
		if (alpha == DEFAULT_ALPHA) {
			return color;
		} else {
			int red = Color.red(color);
			int green = Color.green(color);
			int blue = Color.blue(color);
			
			return Color.argb(alpha, red, green, blue);
		}
	}
}
