package com.viewpager.carousel.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.view.viewpager.carousel.R;
import com.yline.utils.LogUtil;
import com.yline.utils.UIResizeUtil;
import com.yline.utils.UIScreenUtil;
import com.yline.view.fresco.FrescoManager;
import com.yline.view.fresco.view.FrescoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 广告控件，支持定制 + 动态加载图片
 *
 * @author yline 2017/2/14 --> 15:08
 * @version 1.0.0
 */
public class ADView extends RelativeLayout {
	private LinearLayout linearLayout; // 指示器
	private ViewPager viewPager; // 内容
	private AdPagerAdapter<String> pagerAdapter; // 内容的适配器
	
	// 数据
	private final int pointSizeBefore; // 指示点，未选中大小
	private final int pointSizeAfter; // 指示点，选中大小
	private final int pointLeftMargin;
	private final int pointRightMargin;
	private final int pointTopMargin;
	private final int pointBottomMargin;
	private final int pointBgResource; // 指示点，内容
	
	private final boolean recycleEnable; // 是否循环播放
	private final boolean recycleAuto; // 是否自动循环播放
	private final int recycleAutoTime; // 每次自动切换的时间
	private final boolean recycleRight; // 自动播放，方向为右侧
	
	public ADView(Context context) {
		this(context, null);
	}
	
	public ADView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ADView);
		pointSizeBefore = a.getInteger(R.styleable.ADView_ad_point_size_before, 15);
		pointSizeAfter = a.getInteger(R.styleable.ADView_ad_point_size_after, 15);
		
		pointLeftMargin = a.getInteger(R.styleable.ADView_ad_point_left_margin, 10);
		pointRightMargin = a.getInteger(R.styleable.ADView_ad_point_right_margin, 0);
		pointTopMargin = a.getInteger(R.styleable.ADView_ad_point_top_margin, 0);
		pointBottomMargin = a.getInteger(R.styleable.ADView_ad_point_bottom_margin, 0);
		
		pointBgResource = a.getResourceId(R.styleable.ADView_ad_point_bg, R.drawable.widget_ad_point);
		
		recycleEnable = a.getBoolean(R.styleable.ADView_ad_recycle_enable, true);
		recycleAuto = a.getBoolean(R.styleable.ADView_ad_recycle_auto, true);
		recycleAutoTime = a.getInteger(R.styleable.ADView_ad_recycle_auto_duration, 3500);
		recycleRight = a.getBoolean(R.styleable.ADView_ad_recycle_right, true);
		
		float viewPagerAspectRatio = a.getFloat(R.styleable.ADView_ad_viewpager_aspect_ratio, -1);
		a.recycle();
		
		LayoutInflater.from(context).inflate(R.layout.view_ad, this, true);
		linearLayout = findViewById(R.id.ll_widget_ad);
		
		viewPager = findViewById(R.id.viewpager_widget_ad);
		pagerAdapter = new AdPagerAdapter<>(context, recycleRight);
		viewPager.setAdapter(pagerAdapter);
		resizeViewPager(viewPager, viewPagerAspectRatio);
		
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			
			@Override
			public void onPageSelected(int position) {
				selectIndicatorPoint(linearLayout, position); // 指示点
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			
			}
		});
	}
	
	/**
	 * 设置ViewPager 宽高比
	 *
	 * @param aspectRatio 宽高比
	 */
	private void resizeViewPager(ViewPager viewPager, float aspectRatio) {
		if (aspectRatio > 0) {
			ViewGroup.LayoutParams params = viewPager.getLayoutParams();
			if (null == params) {
				int width = UIScreenUtil.getScreenWidth(getContext());
				int height = (int) (width * aspectRatio);
				params = new ViewGroup.LayoutParams(width, height);
			} else {
				if (params.width <= 0) {
					params.width = UIScreenUtil.getScreenWidth(getContext());
				}
				params.height = (int) (params.width / aspectRatio);
			}
			viewPager.setLayoutParams(params);
		}
	}
	
	public void setData(List<String> dataList) {
		setData(dataList, 0);
	}
	
	/**
	 * 设置数据，并更新界面
	 */
	public void setData(List<String> dataList, int startPosition) {
		if (null == dataList || dataList.isEmpty()) {
			return;
		}
		
		viewPager.setOffscreenPageLimit(dataList.size());
		viewPager.setCurrentItem(startPosition);
		
		initIndicatorPoint(getContext(), linearLayout, dataList.size());
		selectIndicatorPoint(linearLayout, startPosition);
		pagerAdapter.setDataList(dataList, recycleEnable);
		
		// 开始自动循环播放
		// 这个必须在ViewPager等初始化完成之后,才能开始
		if (recycleAuto) {
			AdHandler handler = new AdHandler(viewPager, recycleAutoTime, recycleRight);
			handler.sendEmptyMessageDelayed(AdHandler.AUTO, recycleAutoTime);
		}
	}
	
	/**
	 * 设置 监听器
	 *
	 * @param listener 监听器
	 */
	public void setOnPageListener(OnPageListener<String> listener) {
		pagerAdapter.setOnPageListener(listener);
	}
	
	/**
	 * 添加指示点
	 *
	 * @param context      本Activity
	 * @param parentLayout 指示点父框体(此处LinearLayout)
	 * @param count        指示点个数
	 */
	private void initIndicatorPoint(Context context, LinearLayout parentLayout, int count) {
		parentLayout.removeAllViews();
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = pointSizeBefore;
		layoutParams.height = pointSizeBefore;
		if (0 != pointLeftMargin) {
			layoutParams.leftMargin = pointLeftMargin;
		}
		if (0 != pointRightMargin) {
			layoutParams.rightMargin = pointRightMargin;
		}
		if (0 != pointTopMargin) {
			layoutParams.topMargin = pointTopMargin;
		}
		if (0 != pointBottomMargin) {
			layoutParams.bottomMargin = pointBottomMargin;
		}
		
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(pointBgResource);
			parentLayout.addView(imageView, layoutParams);
		}
	}
	
	/**
	 * 指示点状态
	 *
	 * @param parentLayout 指示点父框体(此处LinearLayout)
	 * @param position     当前的指示点
	 */
	private void selectIndicatorPoint(LinearLayout parentLayout, int position) {
		int count = parentLayout.getChildCount();
		
		position = recycleEnable ? position % count : position;
		for (int i = 0; i < count; i++) {
			if (i == position) {
				UIResizeUtil.build().setIsWidthAdapter(false)
						.setWidth(pointSizeAfter).setHeight(pointSizeAfter)
						.commit(parentLayout.getChildAt(i));
				parentLayout.getChildAt(i).setSelected(true);
			} else {
				UIResizeUtil.build().setIsWidthAdapter(false)
						.setWidth(pointSizeBefore).setHeight(pointSizeBefore)
						.commit(parentLayout.getChildAt(i));
				parentLayout.getChildAt(i).setSelected(false);
			}
		}
	}
	
	// View适配器
	private static class AdPagerAdapter<T> extends PagerAdapter {
		private static final int COUNT_MAX = 5040; // 10以内所有数的公倍数  5040
		private Context sContext;
		
		private boolean isRecycle; // 是否循环滚动
		private List<T> sDataList; // 数据集
		private boolean sIsRight;
		
		private OnPageListener<T> pageListener;
		
		private AdPagerAdapter(Context context, boolean isRight) {
			this.sContext = context;
			this.sDataList = new ArrayList<>();
			this.sIsRight = isRight;
		}
		
		private void setDataList(List<T> list, boolean isRecycle) {
			if (null != list && !list.isEmpty()) {
				this.sDataList = list;
				this.isRecycle = isRecycle;
				notifyDataSetChanged();
			}
		}
		
		private void setOnPageListener(OnPageListener<T> listener) {
			this.pageListener = listener;
		}
		
		@Override
		public int getCount() {
			if (isRecycle) {
				return COUNT_MAX;
			} else {
				return sDataList.size();
			}
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = isRecycle ? position % sDataList.size() : position;
			
			View view = LayoutInflater.from(sContext).inflate(R.layout.view_ad_item, null);
			
			final FrescoView frescoView = view.findViewById(R.id.view_ad_item_fresco);
			// FrescoManager.setImageUri(frescoView, (String) mDataList.get(position));
			if (null != pageListener) {
				pageListener.onPageInstance(frescoView, sDataList.get(position));
			}
			
			final int index = position;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != pageListener) {
						pageListener.onPageClick(frescoView, sDataList.get(index), index);
					}
				}
			});
			
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}
		
		@Override
		public void startUpdate(ViewGroup container) {
			super.startUpdate(container);
			if (isRecycle && container instanceof ViewPager) {
				ViewPager viewPager = (ViewPager) container;
				int position = viewPager.getCurrentItem();
				
				LogUtil.v("position = " + position + ", sIsRight = " + sIsRight);
				if (sIsRight) { // 避免重复调用
					if (position == COUNT_MAX - 1) {
						viewPager.setCurrentItem(0, false);
					}
				} else {
					if (0 == position) {
						viewPager.setCurrentItem(sDataList.size(), false);
					}
				}
			}
		}
	}
	
	public interface OnPageListener<T> {
		/**
		 * item 被点击
		 *
		 * @param v        当前控件
		 * @param t        数据类型
		 * @param position 位置(0,开始)
		 */
		void onPageClick(View v, T t, int position);
		
		/**
		 * item 图片初始化
		 *
		 * @param frescoView 展示控件
		 * @param t          数据类型
		 */
		void onPageInstance(FrescoView frescoView, T t);
	}
	
	// 自动播放
	private static class AdHandler extends Handler {
		private static final int AUTO = 100;
		
		private static final int Move = 0; // 用户正在滑动
		private static final int Up = 1; // 用户 松开
		private static final int Auto = 2; // 自动播放模式
		
		private int touchState;
		private final int autoTime;
		private final boolean isRight;
		private WeakReference<ViewPager> reference;
		
		/**
		 * @param viewPager 整体控件
		 * @param oneTime   自动播放一次的时间
		 */
		@SuppressLint("ClickableViewAccessibility")
		private AdHandler(ViewPager viewPager, int oneTime, boolean scrollRight) {
			autoTime = Math.max(oneTime, 1800);
			isRight = scrollRight;
			
			reference = new WeakReference<>(viewPager);
			viewPager.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_MOVE:
							touchState = Move;
							break;
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
							touchState = Up;
							break;
						default:
							break;
					}
					
					return false;
				}
			});
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			LogUtil.v("msg.what = " + msg.what + ", autoTime = " + autoTime);
			if (msg.what == AUTO) {
				if (touchState == Move) {
					touchState = Auto;
					sendEmptyMessageDelayed(AUTO, autoTime);
				} else if (touchState == Auto) {
					sendEmptyMessageDelayed(AUTO, autoTime);
					autoScroll();
				} else {
					touchState = Auto;
					sendEmptyMessageDelayed(AUTO, autoTime);
				}
			}
		}
		
		private void autoScroll() {
			ViewPager viewPager = reference.get();
			if (null != viewPager) {
				if (isRight) {
					viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
				} else {
					viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
				}
			}
		}
	}
}
