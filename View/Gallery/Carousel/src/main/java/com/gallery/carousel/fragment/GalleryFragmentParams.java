package com.gallery.carousel.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gallery.carousel.utils.UILayoutUtils;
import com.view.gallery.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体参数需要的时候,自己添加set方法
 */
public class GalleryFragmentParams
{
	// 资源相关
	private int startPosition = 0; // 开始数

	private int count = 5; // 总数,设置图片资源的时候赋值

	private List<GalleryBean> beans; // 图片资源

	private boolean isPoint = true; // 是否需要指示点

	private int pointResId = R.drawable.carousel_layout_page; // 点资源  selected

	// 画廊大小
	private int galleryWidth = -1; // match

	private int galleryHeight = -2; // wrap

	// 画廊图片大小
	private int viewSpace = 0;

	private int viewWidth = -1;

	private int viewHeight = -2;

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

	// 自动播放
	private boolean isRecycle = true; // 是否轮回

	private boolean isAutoRecycle = true; // 是否自动播放(必须轮回才能自动播放)

	private boolean isAutoRecycleToRight = true; // 向右滑动

	private int timeAutoRecycle = 4500; // 自动播放

	private int timeUpRecycle = 8000; // 用户手离开之后暂停的时间(滑动长时间),滑动短时间则范围是:（8000-5000/3,8000+5000/3）

	private class GalleryBean
	{
		private int id;

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}
	}

	/** 仅仅在单兵时,使用 */
	public void setBeansId(int[] resInt)
	{
		List<GalleryBean> galleryBeans = new ArrayList<GalleryBean>();

		for (int i = 0; i < resInt.length; i++)
		{
			GalleryBean bean = new GalleryBean();
			bean.setId(resInt[i]);
			galleryBeans.add(bean);
		}

		this.setBeans(galleryBeans);
	}

	public void setBeans(List<GalleryBean> beans)
	{
		this.beans = beans;
		this.count = beans.size();
	}

	public void setGalleryLayout(int galleryWidth, int galleryHeight)
	{
		this.galleryWidth = galleryWidth;
		this.galleryHeight = galleryHeight;
	}

	public void setViewLayout(int viewWidth, int viewHeight)
	{
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
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

	public void setAutoRecycleToRight(boolean isAutoRecycleToRight)
	{
		this.isAutoRecycleToRight = isAutoRecycleToRight;
	}

	public void setPointStartPosition(int startPosition)
	{
		this.startPosition = startPosition;
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

	public void setViewSpace(int viewSpace)
	{
		this.viewSpace = viewSpace;
	}

	/**
	 * @param context
	 * @param position
	 * @param convertView
	 * @return
	 */
	public View getViewRes(Context context, int position, View convertView)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(context);

			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(beans.get(position).getId());
			UILayoutUtils.setGalleryByNew(imageView, this.getViewWidth(), this.getViewHeight());

			convertView = imageView;
			convertView.setTag(imageView);
		}
		else
		{
			imageView = (ImageView) convertView.getTag();
		}

		return convertView;
	}

	public View getViewRes(Context context, int position, View convertView, int resourceLayout, int resourceId)
	{
		View view;
		if (null == convertView)
		{
			view = LayoutInflater.from(context).inflate(resourceLayout, null);
			ImageView image = (ImageView) view.findViewById(resourceId);

			image.setImageResource(beans.get(position).getId());
			UILayoutUtils.setLinear(image, this.getViewWidth(), this.getViewHeight());

			convertView = view;
			convertView.setTag(view);
		}
		else
		{
			view = (View) convertView.getTag();
		}
		return convertView;
	}

	public int getCountRecycle()
	{
		return count * 4;
	}

	public int getCount()
	{
		return count;
	}

	public int getStartPosition()
	{
		return startPosition;
	}

	public List<GalleryBean> getBeans()
	{
		return beans;
	}

	public boolean isPoint()
	{
		return isPoint;
	}

	public int getPointResId()
	{
		return pointResId;
	}

	public int getGalleryWidth()
	{
		return galleryWidth;
	}

	public int getGalleryHeight()
	{
		return galleryHeight;
	}

	public int getViewSpace()
	{
		return viewSpace;
	}

	public int getViewWidth()
	{
		return viewWidth;
	}

	public int getViewHeight()
	{
		return viewHeight;
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

	private boolean isDebug = true;

	private int zoomUnit = 150;

	private float maxDegX = 0; // 150左右即可

	private float maxDegY = 150;

	private float maxDegZ = 0;

	public boolean isDebug()
	{
		return isDebug;
	}

	public void setDebug(boolean isDebug)
	{
		this.isDebug = isDebug;
	}

	public int getZoomUnit()
	{
		return zoomUnit;
	}

	public void setZoomUnit(int zoomUnit)
	{
		this.zoomUnit = zoomUnit;
	}

	public float getMaxDegX()
	{
		return maxDegX;
	}

	public void setMaxDegX(float maxDegX)
	{
		this.maxDegX = maxDegX;
	}

	public float getMaxDegY()
	{
		return maxDegY;
	}

	public void setMaxDegY(float maxDegY)
	{
		this.maxDegY = maxDegY;
	}

	public float getMaxDegZ()
	{
		return maxDegZ;
	}

	public void setMaxDegZ(float maxDegZ)
	{
		this.maxDegZ = maxDegZ;
	}

}
