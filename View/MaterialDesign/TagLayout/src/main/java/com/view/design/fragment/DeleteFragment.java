package com.view.design.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;

/**
 * @author yline 2017/2/24 --> 13:58
 * @version 1.0.0
 */
public class DeleteFragment extends BaseFragment
{
	private TextView tvShow;

	private static String content;

	public static DeleteFragment newInstance(String content)
	{
		DeleteFragment.content = content;
		return new DeleteFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view)
	{
		tvShow = (TextView) view.findViewById(android.R.id.text1);
		tvShow.setText(content);
	}

	/**
	 * 设置显示内容
	 *
	 * @param content
	 */
	public void setText(String content)
	{
		if (null != tvShow)
		{
			tvShow.setText(content);
		}
		else
		{
			LogFileUtil.v("tvShow is null");
		}
	}
}
