package com.viewpager.tab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.view.viewpager.tab.R;
import com.yline.base.BaseFragment;
import com.yline.utils.LogUtil;

/**
 * 中间的Fragment视图
 */
public class ShowFragment extends BaseFragment {
	private static final String KEY_TEXT = "key_text";
	
	public static ShowFragment getInstance(String showText) {
		ShowFragment fragment = new ShowFragment();
		Bundle bundle = new Bundle();
		bundle.putString(KEY_TEXT, showText);
		fragment.setArguments(bundle);
		return new ShowFragment();
	}
	
	private TextView showTv;
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		
		LogUtil.v("");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		LogUtil.v("");
		
		return inflater.inflate(R.layout.fragment_show, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		LogUtil.v("");
		initView(view);
		initData();
	}
	
	private void initView(View view) {
		showTv = view.findViewById(R.id.show_text);
	}
	
	private void initData() {
		Bundle arguments = getArguments();
		if (null != arguments) {
			String text = arguments.getString(KEY_TEXT);
			showTv.setText(text);
		}
	}
	
	/**
	 * 按理论来的应该要分出去，这里就算了
	 *
	 * @param showStr 内容
	 */
	public void setShowStr(String showStr) {
		showTv.setText(showStr);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		LogUtil.v("");
	}
}
