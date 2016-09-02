package com.tabhost.viewpager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.base.BaseFragment;

public class TestFragment extends BaseFragment
{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        TextView tv = new TextView(getActivity());
        
        tv.setText(getClass().getSimpleName());
        tv.setGravity(Gravity.CENTER);
        
        return tv;
    }
    
}
