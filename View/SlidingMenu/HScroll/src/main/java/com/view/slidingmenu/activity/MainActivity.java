package com.view.slidingmenu.activity;

import com.view.slidingmenu.fragment.HScrollContentFragment;
import com.view.slidingmenu.fragment.HScrollLeftFragment;
import com.view.slidingmenu.hscroll.R;
import com.view.slidingmenu.hscroll.SlideMenuHScrollZhy;
import com.yline.base.BaseFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;

public class MainActivity extends BaseFragmentActivity implements HScrollContentFragment.OnSlideMenuToggleClick
{
    private FragmentManager fragmentManager;
    
    private SlideMenuHScrollZhy slideMenuHscroll;
    
    private HScrollContentFragment contentFragment;
    
    private HScrollLeftFragment leftFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        fragmentManager = getSupportFragmentManager();
        initView();
        initData();
    }
    
    private void initView()
    {
        slideMenuHscroll = (SlideMenuHScrollZhy)findViewById(R.id.slide_hscroll);
    }
    
    private void initData()
    {
        contentFragment = new HScrollContentFragment();
        leftFragment = new HScrollLeftFragment();
        
        fragmentManager.beginTransaction().add(R.id.slide_hscroll_left, leftFragment, "left").commit();
        fragmentManager.beginTransaction().add(R.id.slide_hscroll_content, contentFragment, "content").commit();
    }
    
    @Override
    public void onClick(View view)
    {
        slideMenuHscroll.toggle();
    }
}
