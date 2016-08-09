package com.slidingmenu.demo.activity.eslidingfragment;

import com.yline.base.BaseFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlidingFragment extends BaseFragment
{
    private String mTitle = "Default";
    
    public SlidingFragment()
    {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (getArguments() != null)
        {
            mTitle = getArguments().getString("title");
        }
        
        TextView textView = new TextView(getActivity());
        textView.setTextSize(40);
        textView.setBackgroundColor(Color.parseColor("#ffffffff"));
        textView.setGravity(Gravity.CENTER);
        textView.setText(mTitle);
        return textView;
    }
    
}
