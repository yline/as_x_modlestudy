package com.gallery.carousel.view;

import android.content.Context;
import android.graphics.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * 在构造方法中的变量无法通过set方式修改
 *
 * @author f21
 * @date 2016-2-6
 */
@SuppressWarnings("deprecation")
public class GalleryView extends Gallery
{
	private boolean isDebug = true;

	public GalleryState state = GalleryState.STACK;    // 这个只能通过修改内部修改

	public enum GalleryState
	{
		/** 正常情况 */
		NORMAL,
		/** 层叠效果 */
		STACK,
		/** 旋转Y轴 */
		ROTATEY,
		/** 旋转全部(还没见过) */
		ROTATEALL
	}

	//  scale = H / (H + h);  H = 576
	private int zoomUnit = 150;

	private float maxDegX = 0;    // 150左右即可

	private float maxDegY = 0;

	private float maxDegZ = 0;

	/** 变量,且不给外界设置的,仅仅中间变量 */
	private Camera camera;

	private int space = 0;

	private int parentCenterToParentLeft;

	public GalleryView(Context context)
	{
		this(context, null);
	}

	public GalleryView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public GalleryView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		if (state == GalleryState.NORMAL)
		{
			this.setStaticTransformationsEnabled(false);
		}
		else if (state == GalleryState.STACK)
		{
			camera = new Camera();
			this.setStaticTransformationsEnabled(true);
		}
		else
		{
			camera = new Camera();
			this.setStaticTransformationsEnabled(true);
		}
	}

	@Override
	public void setSpacing(int spacing)
	{
		space = spacing;
		super.setSpacing(spacing);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// 可以丢一些计算量在这里
		// gallery控件的中心与gallery控件最左边的距离  (不等于控件宽度的一半)
		parentCenterToParentLeft = (this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) / 2 + this.getPaddingLeft();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void setLayoutParams(android.view.ViewGroup.LayoutParams params)
	{
		super.setLayoutParams(params);
	}

	// 按照position来设置缩放和旋转参数
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t)
	{
		boolean returnSuper = super.getChildStaticTransformation(child, t);
		t.setTransformationType(Transformation.TYPE_MATRIX);    //

		if (state == GalleryState.NORMAL)
		{
			// do nothing
		}
		else if (state == GalleryState.STACK)
		{
			int left = child.getLeft();
			int childWidth = child.getWidth();
			float position = getChildCenterToParentCenter(left, childWidth) * 1.0f / childWidth;
			if (isDebug)
			{
				Log.v("tag", "left = " + left + "\t,position = " + position);
			}
			// 以下是为了3个层叠时的延时专门做的,具体的时候,使用被注释的
			if (Math.abs(position) < 0.5)
			{
				transformLayoutStack(childWidth / 2, child.getHeight() / 2, t, Math.abs(position) * zoomUnit);
			}
			else
			{
				transformLayoutStack(childWidth / 2, child.getHeight() / 2, t, Math.abs(1) * zoomUnit);
			}
			// transformLayoutStack(childWidth/2, child.getHeight()/2, t, Math.abs(position) * zoomUnit);
		}
		else if (state == GalleryState.ROTATEY)
		{
			int left = child.getLeft();
			int childWidth = child.getWidth();
			float position = getChildCenterToParentCenter(left, childWidth) * 1.0f / childWidth;
			if (isDebug)
			{
				Log.v("tag", "left = " + left + "\t,position = " + position);
			}
			float scaleDeg = position * child.getWidth() / (parentCenterToParentLeft * 2);
			transformLayoutRotateY(childWidth / 2, child.getHeight() / 2, t, -maxDegY * scaleDeg);
		}
		else
		{
			int left = child.getLeft();
			int childWidth = child.getWidth();
			float position = getChildCenterToParentCenter(left, childWidth) * 1.0f / childWidth;
			if (isDebug)
			{
				Log.v("tag", "left = " + left + "\t,position = " + position);
			}
			float scaleDeg = position * child.getWidth() / (parentCenterToParentLeft * 2);
			transformLayoutRotateAll(childWidth / 2, child.getHeight() / 2, t, -maxDegX * scaleDeg, -maxDegY * scaleDeg, -maxDegZ * scaleDeg);
		}
		return returnSuper;
	}

	private void transformLayoutStack(int halfOfChildWidth, int halfOfChildHeight, Transformation t, float zoom)
	{
		camera.save();

		camera.translate(0.0f, 0.0f, zoom);

		camera.getMatrix(t.getMatrix());
		t.getMatrix().preTranslate(-halfOfChildWidth, -halfOfChildHeight);
		t.getMatrix().postTranslate(halfOfChildWidth, halfOfChildHeight);
		camera.restore();
	}
	
	private void transformLayoutRotateY(int halfOfChildWidth, int halfOfChildHeight, Transformation t, float degY)
	{
		camera.save();
		camera.rotateX(degY);
		
		camera.getMatrix(t.getMatrix());
		t.getMatrix().preTranslate(-halfOfChildWidth, -halfOfChildHeight);
		t.getMatrix().postTranslate(halfOfChildWidth, halfOfChildHeight);
		camera.restore();
	}

	private void transformLayoutRotateAll(int halfOfChildWidth, int halfOfChildHeight, Transformation t, float degX, float degY, float degZ)
	{
		camera.save();

		camera.rotate(degX, degY, degZ);

		camera.getMatrix(t.getMatrix());
		t.getMatrix().preTranslate(-halfOfChildWidth, -halfOfChildHeight);
		t.getMatrix().postTranslate(halfOfChildWidth, halfOfChildHeight);
		camera.restore();
	}

	// 层叠的顺序
	@Override
	protected int getChildDrawingOrder(int childCount, int i)
	{
		if (state == GalleryState.NORMAL)
		{
			return super.getChildDrawingOrder(childCount, i);
		}
		else
		{
			if (i < childCount / 2)
			{
				return i;
			}
			else
			{
				return childCount - i - 1 + childCount / 2;
			}
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		if (state == GalleryState.NORMAL)
		{
			return super.onFling(e1, e2, velocityX, velocityY);
		}
		else
		{
			int keyCode;
			if (e1.getX() < e2.getX())
			{
				keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			}
			else
			{
				keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			}
			return onKeyDown(keyCode, null);
		}
	}

	/**
	 * @return 控件中心之间的距离
	 */
	private int getChildCenterToParentCenter(int childLeft, int childWidth)
	{
		int distantBetweenBoth = getChildCenterToParentLeft(childLeft, childWidth) - parentCenterToParentLeft;
		if (distantBetweenBoth == 0)
		{    // 等于0
			return distantBetweenBoth;
		}
		else if (distantBetweenBoth < 0)
		{    // 左边负数
			return distantBetweenBoth + space;
		}
		else
		{    // 右边正数
			return distantBetweenBoth - space;
		}
	}

	/**
	 * 子控件中心到父控件最左边距离
	 */
	private int getChildCenterToParentLeft(int childLeft, int childWidth)
	{
		return childLeft + childWidth / 2;
	}
	
	public void setDebug(boolean isDebug)
	{
		this.isDebug = isDebug;
	}

	public void setZoomUnit(int zoomUnit)
	{
		this.zoomUnit = zoomUnit;
	}

	public void setMaxDegY(float maxDegY)
	{
		this.maxDegY = maxDegY;
	}

	public void setMaxDegAll(float maxDegX, float maxDegY, float maxDegZ)
	{
		this.maxDegX = maxDegX;
		this.maxDegY = maxDegY;
		this.maxDegZ = maxDegZ;
	}
	
}






