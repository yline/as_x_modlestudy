package com.yline.custom.circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yline.custom.R;

/**
 * 网络请求 返回
 *
 * @author yline 2018/11/26 -- 11:40
 */
public class HttpLoadingView extends RelativeLayout {
	public HttpLoadingView(Context context) {
		this(context, null);
	}
	
	public HttpLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.http_loading_view, this);
	}
	
	private void initView(){
		
	    initViewClick();
	}
	
	private void initViewClick(){
	
	}
}
