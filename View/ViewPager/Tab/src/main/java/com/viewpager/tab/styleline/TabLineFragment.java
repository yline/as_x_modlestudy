package com.viewpager.tab.styleline;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.view.viewpager.tab.R;
import com.viewpager.tab.activity.MainApplication;
import com.yline.base.BaseFragment;

/**
 * 难点在 tabsChangeClickListener 的回调处理
 */
public class TabLineFragment extends BaseFragment
{
	private static final int colorBefore = Color.BLACK;

	private static final int colorAfter = Color.GREEN;

	private static final int numberOfTab = 4;

	private int lineWidth;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_tabline, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initView(view);
		initData();

		setTabsClick();
	}

	private void initView(View view)
	{
		viewHolder.oneTv = (TextView) view.findViewById(R.id.tv_tab_one);
		viewHolder.twoTv = (TextView) view.findViewById(R.id.tv_tab_two);
		viewHolder.threeTv = (TextView) view.findViewById(R.id.tv_tab_three);
		viewHolder.fourTv = (TextView) view.findViewById(R.id.tv_tab_four);

		viewHolder.oneLl = (LinearLayout) view.findViewById(R.id.ll_tab_one);
		viewHolder.twoLl = (LinearLayout) view.findViewById(R.id.ll_tab_two);
		viewHolder.threeLl = (LinearLayout) view.findViewById(R.id.ll_tab_three);
		viewHolder.fourLl = (LinearLayout) view.findViewById(R.id.ll_tab_four);

		viewHolder.lineIv = (ImageView) view.findViewById(R.id.iv_tab_line);
	}

	private ViewHolder viewHolder = new ViewHolder();

	private class ViewHolder
	{
		private TextView oneTv;

		private TextView twoTv;

		private TextView threeTv;

		private TextView fourTv;

		private LinearLayout oneLl;

		private LinearLayout twoLl;

		private LinearLayout threeLl;

		private LinearLayout fourLl;

		private ImageView lineIv;
	}

	private void initData()
	{
		lineWidth = getScreenWidth(MainApplication.getApplication()) / numberOfTab;
		setViewGroupLayout(viewHolder.lineIv, lineWidth);

		setTextColor(0);
		setLineMargin(viewHolder.lineIv, 0 * lineWidth);
	}

	/** 获取屏幕宽度 */
	private int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/** 设置视图宽度 */
	private void setViewGroupLayout(View view, int width)
	{
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	/** line 点击事件 */
	private void setTabsClick()
	{
		tabsChangeClickListener tabListener = new tabsChangeClickListener();

		viewHolder.oneLl.setOnClickListener(tabListener);
		viewHolder.oneLl.setTag(0);
		viewHolder.twoLl.setOnClickListener(tabListener);
		viewHolder.twoLl.setTag(1);
		viewHolder.threeLl.setOnClickListener(tabListener);
		viewHolder.threeLl.setTag(2);
		viewHolder.fourLl.setOnClickListener(tabListener);
		viewHolder.fourLl.setTag(3);
	}

	private class tabsChangeClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			int position = (Integer) v.getTag();
			//	setTextColor(position);	// 改变字体颜色
			//	setLineMargin(viewHolder.lineIv, position * lineWidth);

			if (getActivity() instanceof OnTabSelectedListener)
			{
				((OnTabSelectedListener) getActivity()).onTabSelected(position);
			}
		}
	}

	private void setLineMargin(View view, int leftMargin)
	{
		if (view.getParent() instanceof FrameLayout)
		{
			setFrameLayoutMargin(view, leftMargin);
		}
		else if (view.getParent() instanceof LinearLayout)
		{
			setLinearLayoutMargin(view, leftMargin);
		}
		else if (view.getParent() instanceof RelativeLayout)
		{
			setRelativeLayoutMargin(view, leftMargin);
		}
		else
		{
			Log.e("tabhost", "父窗体不在范围内");
		}
	}

	private void setLinearLayoutMargin(View view, int leftMargin)
	{
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.leftMargin = leftMargin;
		view.setLayoutParams(params);
	}

	private void setFrameLayoutMargin(View view, int leftMargin)
	{
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
		params.leftMargin = leftMargin;
		view.setLayoutParams(params);
	}

	private void setRelativeLayoutMargin(View view, int leftMargin)
	{
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
		params.leftMargin = leftMargin;
		view.setLayoutParams(params);
	}

	/**
	 * 设置标签字体颜色
	 *
	 * @param position 滑动结束,位置
	 */
	public void setTextColor(int position)
	{
		viewHolder.oneTv.setTextColor(colorBefore);
		viewHolder.twoTv.setTextColor(colorBefore);
		viewHolder.threeTv.setTextColor(colorBefore);
		viewHolder.fourTv.setTextColor(colorBefore);

		switch (position)
		{
			case 0:
				viewHolder.oneTv.setTextColor(colorAfter);
				break;
			case 1:
				viewHolder.twoTv.setTextColor(colorAfter);
				break;
			case 2:
				viewHolder.threeTv.setTextColor(colorAfter);
				break;
			case 3:
				viewHolder.fourTv.setTextColor(colorAfter);
				break;
		}
	}

	/**
	 * @param position       滑动时,位置(采取的是退一法)
	 * @param positionOffset 滑动时,偏移量
	 */
	public void moveTabLine(int position, float positionOffset)
	{
		setLineMargin(viewHolder.lineIv, (int) (lineWidth * (position + positionOffset)));
	}

	/**
	 * tab 标签 点击接口
	 */
	public interface OnTabSelectedListener
	{
		/**
		 * 改变标签位置
		 *
		 * @param position (0-3)
		 */
		void onTabSelected(int position);
	}
}
