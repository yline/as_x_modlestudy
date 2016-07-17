package com.view.slidingmenu.fragment;

import com.yline.base.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HScrollLeftFragment extends BaseFragment
{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        TextView tv_show = new TextView(getActivity());
        tv_show.setTextSize(40);
        tv_show.setText(show);
        return tv_show;
    }
    
    private String show = "left 初始化";
    
    public String getShow()
    {
        return show;
    }
    
    public void setShow(String show)
    {
        this.show = show;
    }
}
