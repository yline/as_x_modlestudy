package com.gallery.carousel.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.gallery.carousel.view.GalleryView;
import com.gallery.carousel.view.utils.UILayoutUtils;
import com.view.gallery.test.R;
import com.yline.base.BaseFragment;


public class GalleryFragment extends BaseFragment
{
	private GalleryView gallery;

	private GalleryFragmentParams params;

	private LinearLayout linearLayout;

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

	public GalleryFragment(GalleryFragmentParams params)
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
		gallery = (GalleryView) view.findViewById(R.id.carousel_gallery);
		linearLayout = (LinearLayout) view.findViewById(R.id.carousel_linear);
	}

	private void initData()
	{
		if (params.isPoint())
		{
			initIndicatorPoint(getActivity(), linearLayout, params.getCount());
			selectIndicatorPoint(linearLayout, params.getStartPosition());
			gallery.setOnItemSelectedListener(new OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
				{
					position = params.isRecycle() ? position % params.getCount() : position;

					selectIndicatorPoint(linearLayout, position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		}
		// gallery
		UILayoutUtils.setViewGroup(gallery, params.getGalleryWidth(), params.getGalleryHeight());
		gallery.setSpacing(params.getViewSpace());
		gallery.setAdapter(new GalleryBaseAdapter());
		gallery.setSelection(params.getStartPosition());

		// 自定义的
		gallery.setDebug(params.isDebug());
		if (gallery.state == GalleryView.GalleryState.NORMAL)
		{
			// do nothing
		}
		else if (gallery.state == GalleryView.GalleryState.STACK)
		{
			gallery.setZoomUnit(params.getZoomUnit());
		}
		else if (gallery.state == GalleryView.GalleryState.ROTATEY)
		{
			gallery.setMaxDegY(params.getMaxDegY());
		}
		else
		{
			gallery.setMaxDegAll(params.getMaxDegX(), params.getMaxDegY(), params.getMaxDegZ());
		}

		// 点击事件
		gallery.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				position = params.isRecycle() ? position % params.getCount() : position;

				if (getActivity() instanceof OnCarouselGalleryItemClickListener)
				{
					((OnCarouselGalleryItemClickListener) getActivity()).onItemClick(parent, view, position, id);
				}
			}
		});
	}

	public interface OnCarouselGalleryItemClickListener
	{
		void onItemClick(AdapterView<?> parent, View view, int position, long id);
	}

	private Thread thread;

	private Handler handler;

	@SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
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

		gallery.setOnTouchListener(new OnTouchListener()
		{

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
							gallery.setSelection((int) (gallery.getSelectedItemId() + 1));
						}
						else
						{
							gallery.setSelection((int) (gallery.getSelectedItemId() - 1));
						}
						break;
				}
			}
		};
	}

	private class GalleryBaseAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return params.isRecycle() ? params.getCountRecycle() : params.getCount();
		}

		@Override
		public Object getItem(int position)
		{
			position = params.isRecycle() ? position % params.getCount() : position;

			return params.getBeans().get(position);
		}

		@Override
		public long getItemId(int position)
		{
			if (params.isRecycle())
			{
				if (position == 0)
				{
					gallery.setSelection(position + params.getCount(), false);
				}
				else if (position == params.getCountRecycle() - 1)
				{
					gallery.setSelection(position % params.getCount(), false);
				}
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			position = params.isRecycle() ? position % params.getCount() : position;

			convertView = params.getViewRes(getActivity(), position, convertView, R.layout.fragment_carousel_image, R.id.carousel_image_item);

			return convertView;
		}
	}

	/**
	 * 添加指示点
	 *
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

	private void initPointParamsNew(View view, GalleryFragmentParams carouselParam)
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
	 *
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

	private void setPointParamsLayoutAfter(View view, GalleryFragmentParams carouselParam)
	{
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();

		linearParams.width = carouselParam.getPointWidthAfter();
		linearParams.height = carouselParam.getPointHeightAfter();

		view.setLayoutParams(linearParams);
	}

	private void setPointParamsLayoutBefore(View view, GalleryFragmentParams carouselParam)
	{
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();

		linearParams.width = carouselParam.getPointWidthBefore();
		linearParams.height = carouselParam.getPointHeightBefore();

		view.setLayoutParams(linearParams);
	}
}
