package com.slidemenu.tab.fragment;

import com.slidemenu.tab.R;
import com.yline.base.BaseFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainContentFragmentOne extends BaseFragment
{
    private View mViewOne;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        mViewOne = inflater.inflate(R.layout.fragment_content_one, container, false);
        return mViewOne;
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
