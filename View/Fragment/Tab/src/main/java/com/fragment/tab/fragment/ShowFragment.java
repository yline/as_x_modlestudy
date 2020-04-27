package com.fragment.tab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.view.fragment.tab.R;

/**
 * 中间的Fragment视图
 */
public class ShowFragment extends Fragment {
    private LazyLoadHelper mLazyLoadHelper;

    private TextView showTv;
    private String showStr = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLazyLoadHelper().onActivityCreated();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getLazyLoadHelper().setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLazyLoadHelper().onViewCreated(view, getUserVisibleHint());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getLazyLoadHelper().onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLazyLoadHelper().onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    LazyLoadHelper getLazyLoadHelper() {
        if (null == mLazyLoadHelper) {
            mLazyLoadHelper = new LazyLoadHelper();
            mLazyLoadHelper.setOnLazyStartCallback(new LazyLoadHelper.OnLazyStartCallback() {
                @Override
                public void onStart(View view) {
                    onLazyLoaded(view);
                }
            });
        }
        return mLazyLoadHelper;
    }

    public void onLazyLoaded(View view) {
        //  初始化操作咯
        initView(view);
        initData();
    }

    private void initView(View view) {
        showTv = (TextView) view.findViewById(R.id.tv_show);
    }

    private void initData() {
        showTv.setText(showStr);
    }

    /**
     * 按理论来的应该要分出去，这里就算了
     */
    public void setShowStr(String showStr) {
        this.showStr = showStr;
    }
}
