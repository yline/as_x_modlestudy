package com.view.slider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.view.slider.activity.MainApplication;
import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;

public class SideBarFragment extends BaseFragment implements SideBarView.OnLetterTouchedListener
{
	/** 显示时间 */
	private static final int TEXTVIEW_DISAPPEAR_TIME = 1500;

	private SideBarView mSideBarView;

	private TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_sidebar, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		mSideBarView = (SideBarView) view.findViewById(R.id.sidebar_list);
		mSideBarView.setOnLetterTouchedListener(this);

		mTextView = (TextView) view.findViewById(R.id.tv_list);
	}

	private Runnable runnable = new Runnable()
	{

		@Override
		public void run()
		{
			mTextView.setVisibility(View.GONE);
		}
	};

	@Override
	public void onTouched(int position, String str)
	{
		LogFileUtil.v(MainApplication.TAG, "onTouched position = " + ",str = " + str);

		mTextView.setText(position + "-" + str);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.removeCallbacks(runnable); // 解决闪烁的问题
		mTextView.postDelayed(runnable, TEXTVIEW_DISAPPEAR_TIME);
	}
}
