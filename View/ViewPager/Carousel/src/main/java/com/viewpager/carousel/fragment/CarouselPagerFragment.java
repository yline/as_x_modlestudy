package com.viewpager.carousel.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.view.viewpager.carousel.R;


/**
 * 实现自动滑动,可以控制的量都在CarouselFragmentParams中控制
 * 缺陷:有时候循环的点的图片会消失不见,需要调节getRecycleCount中的返回量
 * <p>
 * 网络获取图片还没弄
 */
public class CarouselPagerFragment extends Fragment
{
	private ViewPager viewPager;

	private LinearLayout linearLayout;

	private CarouselPagerFragmentParams params;

	private UserTouchState touchState;

	private enum UserTouchState
	{
		/** 用户滑动 */
		OnMove,
		/** 用户松手 */
		OnUp,
		/** 自动播放 */
		OnAuto
	}

	public CarouselPagerFragment(CarouselPagerFragmentParams params)
	{
		this.params = params;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_carousel, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initView(view);
		initData();

		if (params.isAutoRecycle())
		{
			touchState = UserTouchState.OnAuto;
			initTimeThread();
		}
	}

	private void initView(View view)
	{
		viewPager = (ViewPager) view.findViewById(R.id.carousel_viewpager);
		linearLayout = (LinearLayout) view.findViewById(R.id.carousel_linear);
	}

	private ViewPagerAdapter adapter;

	private void initData()
	{
		// 指示点
		initIndicatorPoint(getActivity(), linearLayout, params.getPointCount());
		selectIndicatorPoint(linearLayout, params.getPointStartPosition());
		// viewPager
		initViewPagerLayout(viewPager, params);

		adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(params.getPointStartPosition());

		viewPager.addOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				position = params.isRecycle() ? position % params.getPointCount() : position;
				// 指示点
				selectIndicatorPoint(linearLayout, position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				position = params.isRecycle() ? position % params.getPointCount() : position;
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});

	}

	private class ViewPagerAdapter extends PagerAdapter
	{

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return (view == object);
		}

		@Override
		public int getCount()
		{
			return params.isRecycle() ? params.getRecycleCount() : params.getPointCount();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			position = params.isRecycle() ? position % params.getPointCount() : position;

			View view = params.getViewRes(getActivity(), position);
			container.addView(view);

			// viewPager点击事件
			final int index = position;
			view.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (getActivity() instanceof onPagerClickListener)
					{
						((onPagerClickListener) getActivity()).onPagerClicked(v, index);
					}
				}
			});

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public void startUpdate(ViewGroup container)
		{
			super.startUpdate(container);
		}

		@Override
		public void finishUpdate(ViewGroup container)
		{
			super.finishUpdate(container);
			if (params.isRecycle())
			{
				int position = viewPager.getCurrentItem();
				if (position == 0)
				{
					viewPager.setCurrentItem(params.getPointCount(), false);
				}
				else if (position == params.getRecycleCount() - 2)
				{
					viewPager.setCurrentItem(position % params.getPointCount(), false);
				}
			}
		}
	}

	private Thread thread;

	private Handler handler;

	@SuppressLint("HandlerLeak")
	private void initTimeThread()
	{
		thread = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (true)
				{
					try
					{ // 做一个暂缓
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
						handler.obtainMessage(-1, e + "").sendToTarget();
						return;
					}

					if (touchState == UserTouchState.OnAuto)
					{
						try
						{
							Thread.sleep(params.getTimeAutoRecycle());
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
							handler.obtainMessage(-1, e + "").sendToTarget();
							return;
						}
						// 快速滑动之后
						if (touchState == UserTouchState.OnAuto)
						{
							handler.obtainMessage(1, "休眠成功").sendToTarget();
						}
						else if (touchState == UserTouchState.OnMove)
						{
							// do nothing
						}
						else
						{ // UserTouchState.OnUp
							try
							{
								Thread.sleep(params.getTimeUpRecycle() - params.getTimeAutoRecycle() / 3);
								handler.obtainMessage(1, "休眠成功").sendToTarget();
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
								handler.obtainMessage(-1, e + "").sendToTarget();
							}
							touchState = UserTouchState.OnAuto;
						}
					}
					else if (touchState == UserTouchState.OnMove)
					{
						// do nothing
					}
					else
					{ // UserTouchState.OnUp
						try
						{
							Thread.sleep(params.getTimeUpRecycle());
							handler.obtainMessage(1, "休眠成功").sendToTarget();
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
							handler.obtainMessage(-1, e + "").sendToTarget();
						}
						touchState = UserTouchState.OnAuto;
					}
				}
			}
		});
		thread.start();

		viewPager.setOnTouchListener(new OnTouchListener()
		{

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						touchState = UserTouchState.OnMove;
						break;
					case MotionEvent.ACTION_UP:
						touchState = UserTouchState.OnUp;
						break;
				}
				return false;
			}
		});

		handler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);

				switch (msg.what)
				{
					case -1:
						Log.e("log_carouseFragment", (String) msg.obj);
						break;
					case 1:
						if (params.isAutoRecycleToRight())
						{
							viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
						}
						else
						{
							viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
						}
						break;
				}
			}
		};
	}

	/**
	 * 设置viewPager大小
	 * @param view          viewPager
	 * @param carouselParam 参数
	 */
	private void initViewPagerLayout(ViewPager view, CarouselPagerFragmentParams carouselParam)
	{
		ViewGroup.LayoutParams pagerParam = view.getLayoutParams();

		pagerParam.width = carouselParam.getViewWidth();
		pagerParam.height = carouselParam.getViewHeight();

		view.setLayoutParams(pagerParam);
	}

	/**
	 * 添加指示点
	 * @param context   本Activity
	 * @param viewGroup 指示点父框体(此处LinearLayout)
	 * @param count     指示点个数
	 */
	private void initIndicatorPoint(Context context, ViewGroup viewGroup, int count)
	{
		for (int i = 0; i < count; i++)
		{
			View view = new View(context);
			initPointParamsNew(view, params);
			view.setBackgroundResource(params.getPointResId());
			viewGroup.addView(view);
		}
	}

	private void initPointParamsNew(View view, CarouselPagerFragmentParams carouselParam)
	{
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(1, 1);

		linearParams.leftMargin = carouselParam.getPointLeft();
		linearParams.rightMargin = carouselParam.getPointRight();
		linearParams.topMargin = carouselParam.getPointTop();
		linearParams.bottomMargin = carouselParam.getPointBottom();

		linearParams.width = carouselParam.getPointWidthBefore();
		linearParams.height = carouselParam.getPointHeightBefore();

		view.setLayoutParams(linearParams);
	}

	/**
	 * 指示点状态
	 * @param viewGroup 指示点父框体(此处LinearLayout)
	 * @param position  当前的指示点
	 */
	private void selectIndicatorPoint(ViewGroup viewGroup, int position)
	{
		for (int i = 0; i < viewGroup.getChildCount(); i++)
		{
			if (i == position)
			{
				viewGroup.getChildAt(i).setSelected(true);
				setPointParamsLayoutAfter(viewGroup.getChildAt(i), params);
			}
			else
			{
				viewGroup.getChildAt(i).setSelected(false);
				setPointParamsLayoutBefore(viewGroup.getChildAt(i), params);
			}
		}
	}

	private void setPointParamsLayoutAfter(View view, CarouselPagerFragmentParams carouselParam)
	{
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();

		linearParams.width = carouselParam.getPointWidthAfter();
		linearParams.height = carouselParam.getPointHeightAfter();

		view.setLayoutParams(linearParams);
	}

	private void setPointParamsLayoutBefore(View view, CarouselPagerFragmentParams carouselParam)
	{
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();

		linearParams.width = carouselParam.getPointWidthBefore();
		linearParams.height = carouselParam.getPointHeightBefore();

		view.setLayoutParams(linearParams);
	}

	/**
	 * viewPager 点击接口
	 */
	public interface onPagerClickListener
	{
		/**
		 * @param v        当前控件
		 * @param position 位置(0,开始)
		 */
		void onPagerClicked(View v, int position);
	}

}
