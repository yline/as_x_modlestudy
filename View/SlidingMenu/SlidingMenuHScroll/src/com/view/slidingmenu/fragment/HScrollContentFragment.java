package com.view.slidingmenu.fragment;

import com.view.slidingmenu.hscroll.R;
import com.yline.base.BaseFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HScrollContentFragment extends BaseFragment
{
    private TextView tv_show;
    
    private Button btn_click;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_hscroll_content, container, false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        
        initView(view);
        initData();
    }
    
    private void initView(View view)
    {
        tv_show = (TextView)view.findViewById(R.id.hscroll_content_tv);
        btn_click = (Button)view.findViewById(R.id.hscroll_content_btn);
    }
    
    private void initData()
    {
        tv_show.setText(show);
        btn_click.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (getActivity() instanceof OnSlideMenuToggleClick)
                {
                    ((HScrollContentFragment.OnSlideMenuToggleClick)getActivity()).onClick(v);
                }
            }
        });
    }
    
    public interface OnSlideMenuToggleClick
    {
        void onClick(View view);
    }
    
    private String show = "content 初始化";
    
    public String getShow()
    {
        return show;
    }
    
    public void setShow(String show)
    {
        this.show = show;
    }
}
