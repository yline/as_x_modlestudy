package com.viewpager.carousel.fragment;


import android.content.Context;
import android.widget.ImageView;

import com.view.viewpager.carousel.R;

/**
 * 单位全都是px
 * <p/>
 * 图片设置多接几个种类(设置多种,获取一种就行)
 */
public class CarouselPagerFragmentParams
{
	// 指示点大小
	private int pointWidthBefore = 15;

	private int pointHeightBefore = 15;

	private int pointWidthAfter = 20;

	private int pointHeightAfter = 20;

	// 指示点之间的间距
	private int pointLeft = 15;

	private int pointRight = 0;

	private int pointTop = 0;

	private int pointBottom = 0;

	// 图片大小
	private int viewWidth = -1; // match

	private int viewHeight = -2; // wrap

	// 自动播放
	private boolean isRecycle = true; // 是否轮回

	private boolean isAutoRecycle = true; // 是否自动播放(必须轮回才能自动播放)

	private boolean isAutoRecycleToRight = true; // 向右滑动

	private int timeAutoRecycle = 4500; // 自动播放

	private int timeUpRecycle = 8000; // 用户手离开之后暂停的时间(滑动长时间),滑动短时间则范围是:（8000-5000/3,8000+5000/3）

	// 资源有关
	private int pointStartPosition = 0; // 开始数

	private int pointCount = 5; // 总数,默认,设置图片资源的时候赋值

	private int pointResId = R.drawable.carousel_layout_page; // 点资源  selected

	private int[] resInt; // 图片资源

	public void setViewLayout(int width, int height)
	{
		this.viewWidth = width;
		this.viewHeight = height;
	}

	public void setResInt(int[] resInt)
	{
		this.resInt = resInt;
		this.pointCount = resInt.length;
	}

	public void setPointLayoutBefore(int width, int height)
	{
		this.pointWidthBefore = width;
		this.pointHeightBefore = height;
	}

	public void setPointLayoutAfter(int width, int height)
	{
		this.pointWidthAfter = width;
		this.pointHeightAfter = height;
	}

	public void setPointLayoutMarginHorizontal(int left, int right)
	{
		this.pointLeft = left;
		this.pointRight = right;
	}

	public void setPointLayoutMarginAll(int left, int right, int top, int bottom)
	{
		this.pointLeft = left;
		this.pointRight = right;
		this.pointTop = top;
		this.pointBottom = bottom;
	}

	/**
	 * 图片循环设置
	 *
	 * @param isRecycle     是否循环
	 * @param isAutoRecycle 是否自动播放
	 */
	public void setRecycleSetting(boolean isRecycle, boolean isAutoRecycle)
	{
		this.isRecycle = isRecycle;
		this.isAutoRecycle = isAutoRecycle;
	}

	/**
	 * 图片循环设置
	 *
	 * @param isRecycle            是否循环
	 * @param isAutoRecycle        是否自动播放
	 * @param isAutoRecycleToRight 循环方向(true 右)
	 */
	public void setRecycleSetting(boolean isRecycle, boolean isAutoRecycle, boolean isAutoRecycleToRight)
	{
		this.isRecycle = isRecycle;
		this.isAutoRecycle = isAutoRecycle;
		this.isAutoRecycleToRight = isAutoRecycleToRight;
	}

	public void setAutoRecycleToRight(boolean isAutoRecycleToRight)
	{
		this.isAutoRecycleToRight = isAutoRecycleToRight;
	}

	public void setPointStartPosition(int pointStartPosition)
	{
		this.pointStartPosition = pointStartPosition;
	}

	public void setPointResId(int pointResId)
	{
		this.pointResId = pointResId;
	}

	public void setTimeAutoRecycle(int timeAutoRecycle)
	{
		this.timeAutoRecycle = timeAutoRecycle;
	}

	public void setTimeUpRecycle(int timeUpRecycle)
	{
		this.timeUpRecycle = timeUpRecycle;
	}

	public ImageView getViewRes(Context context, int position)
	{
		ImageView imageView = new ImageView(context);

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageResource(resInt[position]);

		return imageView;
	}

	public int getRecycleCount()
	{
		if (isAutoRecycleToRight)
		{
			return pointCount * 3 + 2;
		}
		else
		{
			return pointCount * 3 - 1;
		}
	}

	public int getPointWidthBefore()
	{
		return pointWidthBefore;
	}

	public int getPointHeightBefore()
	{
		return pointHeightBefore;
	}

	public int getPointWidthAfter()
	{
		return pointWidthAfter;
	}

	public int getPointHeightAfter()
	{
		return pointHeightAfter;
	}

	public int getPointLeft()
	{
		return pointLeft;
	}

	public int getPointRight()
	{
		return pointRight;
	}

	public int getPointTop()
	{
		return pointTop;
	}

	public int getPointBottom()
	{
		return pointBottom;
	}

	public int getViewWidth()
	{
		return viewWidth;
	}

	public int getViewHeight()
	{
		return viewHeight;
	}

	public boolean isRecycle()
	{
		return isRecycle;
	}

	public boolean isAutoRecycle()
	{
		return isAutoRecycle;
	}

	public boolean isAutoRecycleToRight()
	{
		return isAutoRecycleToRight;
	}

	public int getTimeAutoRecycle()
	{
		return timeAutoRecycle;
	}

	public int getTimeUpRecycle()
	{
		return timeUpRecycle;
	}

	public int getPointStartPosition()
	{
		return pointStartPosition;
	}

	public int getPointCount()
	{
		return pointCount;
	}

	public int getPointResId()
	{
		return pointResId;
	}

	public int[] getResInt()
	{
		return resInt;
	}
}
