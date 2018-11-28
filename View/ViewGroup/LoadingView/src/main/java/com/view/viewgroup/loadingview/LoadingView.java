package com.view.viewgroup.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 加载 蒙版；通过静态方法，可以实现动态添加
 *
 * @author yline 2017/10/19 -- 9:16
 * @version 1.0.0
 */
public class LoadingView extends RelativeLayout {
    private View mLoadingLayout;
    private View mFailedLayout;
    private View.OnClickListener mOnRetryClickListener;
	
	public static LoadingView attachView(View rootView) {
		if (rootView instanceof ViewGroup) {
			LoadingView loadingView = new LoadingView(rootView.getContext());
			((ViewGroup) rootView).addView(loadingView, -1);
			return loadingView;
		}
		return null;
	}
	
	public static void detach(View rootView, LoadingView loadingView) {
		if (null != loadingView && rootView instanceof ViewGroup) {
			((ViewGroup) rootView).removeView(loadingView);
		}
	}

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this, true);

        mLoadingLayout = findViewById(R.id.loading_view_rl_loading);
        mFailedLayout = findViewById(R.id.loading_view_rl_state);

        // 点击重试
        mFailedLayout.findViewById(R.id.loading_view_tv_reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnRetryClickListener){
                    mOnRetryClickListener.onClick(v);
                }
            }
        });
    }

    public void setOnRetryClickListener(OnClickListener mOnRetryClickListener) {
        this.mOnRetryClickListener = mOnRetryClickListener;
    }

    public void loading() {
        setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(VISIBLE);
        mFailedLayout.setVisibility(GONE);
    }

    public void loadFailed() {
        loadFailed(-1, null, null);
    }

    public void loadFailed(int imageRes, String failedStr, String retryStr) {
        setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(GONE);
        mFailedLayout.setVisibility(VISIBLE);

        ImageView ivFailed = mFailedLayout.findViewById(R.id.loading_view_iv_state);
        if (-1 != imageRes) {
            ivFailed.setImageResource(imageRes);
        }

        TextView tvFailed = mFailedLayout.findViewById(R.id.loading_view_tv_state);
        if (null != failedStr) {
            tvFailed.setText(failedStr);
        }

        TextView tvRetry = mFailedLayout.findViewById(R.id.loading_view_tv_reload);
        if (null != retryStr) {
            tvRetry.setText(retryStr);
        }
    }

    public void loadSuccess() {
        setVisibility(GONE);
        mLoadingLayout.setVisibility(GONE);
        mFailedLayout.setVisibility(GONE);
    }
}
