package com.floating.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * 头部悬停的效果，只支持LineaLayoutManager
 *
 * @author yline 2017/5/9 -- 12:38
 * @version 1.0.0
 */
public class FloatingItemDecoration extends RecyclerView.ItemDecoration
{
	private Map<Integer, String> keys = new HashMap<>();
	
	private int mTitleHeight;
	
	private Paint mTextPaint;
	
	private Paint mBackgroundPaint;
	
	private float mTextHeight;
	
	private float mTextBaselineOffset;
	
	private Context mContext;
	
	/**
	 * 滚动列表的时候是否一直显示悬浮头部
	 */
	private boolean showFloatingHeaderOnScrolling = true;
	
	public FloatingItemDecoration(Context context)
	{
		init(context);
	}
	
	public FloatingItemDecoration(Context context, @DrawableRes int drawableId)
	{
		init(context);
	}
	
	public FloatingItemDecoration(Context context, @ColorInt int color, @Dimension float dividerWidth, @Dimension float dividerHeight)
	{
		init(context);
	}
	
	private void init(Context mContext)
	{
		this.mContext = mContext;
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics()));
		mTextPaint.setColor(Color.WHITE);
		Paint.FontMetrics fm = mTextPaint.getFontMetrics();
		mTextHeight = fm.bottom - fm.top;// 计算文字高度
		mTextBaselineOffset = fm.bottom;
		
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAntiAlias(true);
		mBackgroundPaint.setColor(Color.GREEN);
	}
	
	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
	{
		super.onDraw(c, parent, state);
		drawVertical(c, parent);
	}
	
	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
	{
		super.onDrawOver(c, parent, state);
		if (!showFloatingHeaderOnScrolling)
		{
			return;
		}
		int firstVisiblePos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
		if (firstVisiblePos == RecyclerView.NO_POSITION)
		{
			return;
		}
		String title = getTitle(firstVisiblePos);
		if (TextUtils.isEmpty(title))
		{
			return;
		}
		boolean flag = false;
		if (getTitle(firstVisiblePos + 1) != null && !title.equals(getTitle(firstVisiblePos + 1)))
		{
			// 说明是当前组最后一个元素，但不一定碰撞了
			View child = parent.findViewHolderForAdapterPosition(firstVisiblePos).itemView;
			if (child.getTop() + child.getMeasuredHeight() < mTitleHeight)
			{
				// 进一步检测碰撞
				c.save();// 保存画布当前的状态
				flag = true;
				c.translate(0, child.getTop() + child.getMeasuredHeight() - mTitleHeight);//负的代表向上
			}
		}
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();
		int top = parent.getPaddingTop();
		int bottom = top + mTitleHeight;
		c.drawRect(left, top, right, bottom, mBackgroundPaint);
		
		float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
		float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
		c.drawText(title, x, y, mTextPaint);
		if (flag)
		{
			//还原画布为初始状态
			c.restore();
		}
	}
	
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);
		int pos = parent.getChildViewHolder(view).getAdapterPosition();
		if (keys.containsKey(pos))
		{// 留出头部偏移
			outRect.set(0, mTitleHeight, 0, 0);
		}
	}
	
	/**
	 * *如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
	 *
	 * @param position
	 * @return
	 */
	private String getTitle(int position)
	{
		while (position >= 0)
		{
			if (keys.containsKey(position))
			{
				return keys.get(position);
			}
			position--;
		}
		return null;
	}
	
	private void drawVertical(Canvas c, RecyclerView parent)
	{
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();
		int top = 0;
		int bottom = 0;
		for (int i = 0; i < parent.getChildCount(); i++)
		{
			View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			if (keys.containsKey(params.getViewLayoutPosition()))
			{
				// 画头部
				top = child.getTop() - params.topMargin - mTitleHeight;
				bottom = top + mTitleHeight;
				c.drawRect(left, top, right, bottom, mBackgroundPaint);
				//  float x=child.getPaddingLeft()+params.leftMargin;
				float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
				float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
				//  Log.e(TAG, "drawVertical: "+bottom );
				c.drawText(keys.get(params.getViewLayoutPosition()), x, y, mTextPaint);
			}
		}
	}
	
	public void setShowFloatingHeaderOnScrolling(boolean showFloatingHeaderOnScrolling)
	{
		this.showFloatingHeaderOnScrolling = showFloatingHeaderOnScrolling;
	}
	
	public void setKeys(Map<Integer, String> keys)
	{
		this.keys.clear();
		this.keys.putAll(keys);
	}
	
	public void setTitleHeight(int titleHeight)
	{
		this.mTitleHeight = titleHeight;
	}
}
