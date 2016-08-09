package com.slidemenu.tab.fragment;

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

import com.view.slidingmenu.tab.R;
import com.yline.base.BaseFragment;

/**
 * 问题:点击改变标签栏,还是会跳,有空debug进去调吧
 * 难点在 tabsChangeClickListener 的回调处理
 */
public class MainTabFragment extends BaseFragment
{
	private ViewHolder viewHolder;

	private static final int numberOfTab = 4;

	private int lineWidth;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main_tab, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initView(view);

		initTabLine();
		tabsChangeClick();
	}

	private void initView(View view)
	{
		viewHolder = new ViewHolder();

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

	/** 初始化 line */
	private void initTabLine()
	{
		lineWidth = getScreenWidth(getActivity()) / numberOfTab;

		ViewGroup.LayoutParams layoutParams = viewHolder.lineIv.getLayoutParams();
		layoutParams.width = lineWidth;
		viewHolder.lineIv.setLayoutParams(layoutParams);
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	public int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/** line 点击事件 */
	private void tabsChangeClick()
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
			if (getActivity() instanceof OnTabSelectedListener)
			{
				((OnTabSelectedListener) getActivity()).onTabSelected((Integer) v.getTag());
			}
		}
	}

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

	/**
	 * 设置标签字体颜色
	 *
	 * @param position 滑动结束,位置
	 */
	public void resetTextColor(int position)
	{
		final String GREEN = "#008000";

		viewHolder.oneTv.setTextColor(Color.BLACK);
		viewHolder.twoTv.setTextColor(Color.BLACK);
		viewHolder.threeTv.setTextColor(Color.BLACK);
		viewHolder.fourTv.setTextColor(Color.BLACK);

		switch (position)
		{
			case 0:
				viewHolder.oneTv.setTextColor(Color.parseColor(GREEN));
				break;
			case 1:
				viewHolder.twoTv.setTextColor(Color.parseColor(GREEN));
				break;
			case 2:
				viewHolder.threeTv.setTextColor(Color.parseColor(GREEN));
				break;
			case 3:
				viewHolder.fourTv.setTextColor(Color.parseColor(GREEN));
				break;
		}
	}

	/**
	 * 滑动时,改变横条的位置
	 *
	 * @param position       滑动时,位置
	 * @param positionOffset 滑动时,位置比例
	 */
	public void moveTabLine(int position, float positionOffset)
	{
		setLineMargin(viewHolder.lineIv, (int) ((position + positionOffset) * lineWidth));
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
	 * tab 标签 点击接口
	 */
	public interface OnTabSelectedListener
	{
		/** 改变标签位置 */
		void onTabSelected(int position);
	}
}
