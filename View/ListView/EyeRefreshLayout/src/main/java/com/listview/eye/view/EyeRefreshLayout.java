package com.listview.eye.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.listview.eye.R;

/**
 * 这个是有问题的
 */
public class EyeRefreshLayout extends RelativeLayout implements OnScrollListener
{
	private int maxPullTopHeight;

	private int maxPullBottomHeight;

	private int refreshingTopHeight;

	private int refreshingBottomHeight;

	private boolean isTop;

	private boolean isBottom;

	private boolean isRefreshing;

	private boolean isAnimation;

	private RelativeLayout layoutHeader;

	private RelativeLayout layoutFooter;

	private int mCurrentY = 0;

	private boolean pullTag = false;

	private OnScrollListener mOnScrollListener;

	public void setOnScrollListener(OnScrollListener listener)
	{
		this.mOnScrollListener = listener;
	}

	public EyeRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public boolean isRefreshing()
	{
		return this.isRefreshing;
	}

	public void setListView(ListView listView)
	{
		this.mListView = listView;
	}

	private ListView mListView = new ListView(getContext())
	{
		int lastY = 0;

		@Override
		public boolean onTouchEvent(MotionEvent ev)
		{
			if (isAnimation || isRefreshing)
			{
				return super.onTouchEvent(ev);
			}
			RelativeLayout parent = (RelativeLayout) mListView.getParent();

			int currentY = (int) ev.getRawY();
			switch (ev.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					lastY = (int) ev.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
				{
					boolean isToBottom = currentY - lastY >= 0 ? true : false;

					int step = Math.abs(currentY - lastY);
					lastY = currentY;

					if (isTop && mListView.getTop() >= 0)
					{
						if (isToBottom && mListView.getTop() <= maxPullTopHeight)
						{
							// MotionEvent event = MotionEvent.obtain(ev);
							ev.setAction(MotionEvent.ACTION_UP);
							super.onTouchEvent(ev);
							pullTag = true;

							if (mListView.getTop() > layoutHeader.getHeight())
							{
								step = step / 2;
							}
							if ((mListView.getTop() + step) > maxPullTopHeight)
							{
								mCurrentY = maxPullTopHeight;
								scrollTopTo(mCurrentY);
							}
							else
							{
								mCurrentY += step;
								scrollTopTo(mCurrentY);
							}
						}
						else if (!isToBottom && mListView.getTop() > 0)
						{
							ev.setAction(MotionEvent.ACTION_UP);
							super.onTouchEvent(ev);
							if ((mListView.getTop() - step) < 0)
							{
								mCurrentY = 0;
								scrollTopTo(mCurrentY);
							}
							else
							{
								mCurrentY -= step;
								scrollTopTo(mCurrentY);
							}
						}
						else if (!isToBottom && mListView.getTop() == 0)
						{
							if (!pullTag)
							{
								return super.onTouchEvent(ev);
							}
						}
						return true;
					}
					else if (isBottom && mListView.getBottom() <= parent.getHeight())
					{
						if (!isToBottom && (parent.getHeight() - mListView.getBottom()) <= maxPullBottomHeight)
						{
							ev.setAction(MotionEvent.ACTION_UP);
							super.onTouchEvent(ev);
							pullTag = true;
							if (parent.getHeight() - mListView.getBottom() > layoutFooter.getHeight())
							{
								step = step / 2;
							}

							if ((mListView.getBottom() - step) < (parent.getHeight() - maxPullBottomHeight))
							{
								mCurrentY = -maxPullBottomHeight;
								scrollBottomTo(mCurrentY);
							}
							else
							{
								mCurrentY -= step;
								scrollBottomTo(mCurrentY);
							}
						}
						else if (isToBottom && (mListView.getBottom() < parent.getHeight()))
						{
							if ((mListView.getBottom() + step) > parent.getHeight())
							{
								mCurrentY = 0;
								scrollBottomTo(mCurrentY);
							}
							else
							{
								mCurrentY += step;
								scrollBottomTo(mCurrentY);
							}
						}
						else if (isToBottom && mListView.getBottom() == parent.getHeight())
						{
							if (!pullTag)
							{
								return super.onTouchEvent(ev);
							}
						}
						return true;
					}
					break;
				}
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					pullTag = false;

					if (mListView.getTop() > 0)
					{
						if (mListView.getTop() > refreshingTopHeight)
						{
							animateTopTo(layoutHeader.getMeasuredHeight());
							isRefreshing = true;
							if (null != onRefreshListener)
							{
								int time = onRefreshListener.onStart();
								new Handler().postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										pullUp();
										onRefreshListener.onFinish();
									}
								}, time);
							}
						}
						else
						{
							animateTopTo(0);
						}

					}
					else if (mListView.getBottom() < parent.getHeight())
					{
						if ((parent.getHeight() - mListView.getBottom()) > refreshingBottomHeight)
						{
							animateBottomTo(-layoutFooter.getMeasuredHeight());
							isRefreshing = true;
							if (null != onLoadListener)
							{
								int time = onLoadListener.onStart();
								new Handler().postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										pullUp();
										onLoadListener.onFinish();
									}
								}, time);
							}
						}
						else
						{
							animateBottomTo(0);
						}
					}
			}
			return super.onTouchEvent(ev);
		}
	};

	private void scrollBottomTo(int y)
	{
		mListView.layout(mListView.getLeft(), y, mListView.getRight(), this.getMeasuredHeight() + y);
		if (null != onLoadListener)
		{
			onLoadListener.onLoad(layoutHeader.getHeight(), -y);
		}
	}

	private void animateBottomTo(final int y)
	{
		ValueAnimator animator = ValueAnimator.ofInt(mListView.getBottom() - this.getMeasuredHeight(), y);
		animator.setDuration(300);
		animator.addUpdateListener(new AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				int frameValue = (Integer) animation.getAnimatedValue();
				mCurrentY = frameValue;
				scrollBottomTo(frameValue);
				if (frameValue == y)
				{
					isAnimation = false;
				}
			}
		});
		isAnimation = true;
		animator.start();
	}

	private void scrollTopTo(int y)
	{
		mListView.layout(mListView.getLeft(), y, mListView.getRight(), this.getMeasuredHeight() + y);
		if (null != onRefreshListener)
		{
			onRefreshListener.onRefresh(layoutHeader.getHeight(), y);
		}
	}

	private void animateTopTo(final int y)
	{
		ValueAnimator animator = ValueAnimator.ofInt(mListView.getTop(), y);
		animator.setDuration(300);
		animator.addUpdateListener(new AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				int frameValue = (Integer) animation.getAnimatedValue();
				mCurrentY = frameValue;
				scrollTopTo(frameValue);
				if (frameValue == y)
				{
					isAnimation = false;
				}
			}
		});
		isAnimation = true;
		animator.start();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		refreshingTopHeight = layoutHeader.getMeasuredHeight();
		refreshingBottomHeight = layoutFooter.getMeasuredHeight();

		maxPullTopHeight = this.getMeasuredHeight();
		maxPullBottomHeight = this.getMeasuredHeight();
	}

	@Override
	public void onFinishInflate()
	{
		mListView.setBackgroundColor(0xffffffff);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mListView.setOnScrollListener(this);
		this.addView(mListView);

		layoutHeader = (RelativeLayout) this.findViewById(R.id.layoutHeader);
		layoutFooter = (RelativeLayout) this.findViewById(R.id.layoutFooter);

		super.onFinishInflate();
	}

	/** 拉回顶部 */
	public void pullUp()
	{
		isRefreshing = false;
		if (mListView.getTop() > 0)
		{
			animateTopTo(0);
		}
		else if (mListView.getBottom() < this.getHeight())
		{
			animateBottomTo(0);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (null != mOnScrollListener)
		{
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

		if (mListView.getCount() > 0)
		{
			if ((firstVisibleItem + visibleItemCount) == totalItemCount)
			{
				View lastItem = (View) mListView.getChildAt(visibleItemCount - 1);
				if (null != lastItem)
				{

					if (lastItem.getBottom() == mListView.getHeight())
					{
						Log.e("my", lastItem.getBottom() + "");
						isBottom = true;
					}
					else
					{
						isBottom = false;
					}
				}
			}
			else
			{
				isBottom = false;
			}
		}
		else
		{
			isBottom = false;
		}

		if (mListView.getCount() > 0)
		{
			if (firstVisibleItem == 0)
			{
				View firstItem = mListView.getChildAt(0);
				if (null != firstItem)
				{
					if (firstItem.getTop() == 0)
					{
						isTop = true;
					}
					else
					{
						isTop = false;
					}
				}
			}
			else
			{
				isTop = false;
			}
		}
		else
		{
			isTop = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (null != mOnScrollListener)
		{
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	public void setAdapter(ListAdapter adapter)
	{
		this.mListView.setAdapter(adapter);
	}

	public void setOnItemClickListener(OnItemClickListener listener)
	{
		this.mListView.setOnItemClickListener(listener);
	}
	/*
	public interface OnPullHeightChangeListener
	{
		void onTopHeightChange(int headerHeight, int pullHeight);

		void onBottomHeightChange(int footerHeight, int pullHeight);

		void onRefreshing(boolean isTop);
	}*/

	private OnRefreshListener onRefreshListener;

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		this.onRefreshListener = listener;
	}

	public interface OnRefreshListener
	{
		int onStart();

		void onRefresh(int headerHeight, int pullHeight);

		void onFinish();
	}

	private OnLoadListener onLoadListener;

	public void setOnLoadListener(OnLoadListener listener)
	{
		this.onLoadListener = listener;
	}

	public interface OnLoadListener
	{
		int onStart();

		void onLoad(int footerHeight, int pullHeight);

		void onFinish();
	}
}
