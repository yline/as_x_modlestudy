package com.fragment.tab.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.view.fragment.tab.R;
import com.yline.utils.LogUtil;

/**
 * 中间的Fragment视图
 */
public class ShowFragment extends Fragment {
    private TextView showTv;

    private String showStr = "";

    @Override
    public void onAttach(Activity activity) {
        LogUtil.v("onAttach activity Start");
        super.onAttach(activity);
        LogUtil.v("onAttach activity End");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        LogUtil.v("onAttachFragment Start");
        super.onAttachFragment(childFragment);
        LogUtil.v("onAttachFragment End");
    }

    @Override
    public void onAttach(Context context) {
        LogUtil.v("onAttach Context Start");
        super.onAttach(context);
        LogUtil.v("onAttach Context End");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.v("onCreateView");
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.v("onViewCreated Start");
        super.onViewCreated(view, savedInstanceState);
        LogUtil.v("onViewCreated End");

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
     *
     * @param showStr
     */
    public void setShowStr(String showStr) {
        this.showStr = showStr;
    }
}
