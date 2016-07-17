package com.slidemenu.tab.activity;

import java.util.ArrayList;
import java.util.List;

import com.slidemenu.tab.R;
import com.slidemenu.tab.fragment.MainContentFragmentFour;
import com.slidemenu.tab.fragment.MainContentFragmentOne;
import com.slidemenu.tab.fragment.MainContentFragmentThree;
import com.slidemenu.tab.fragment.MainContentFragmentTwo;
import com.slidemenu.tab.fragment.MainTabFragment;
import com.slidemenu.tab.fragment.MainTitleFragment;
import com.yline.base.BaseFragmentActivity;
import com.yline.log.LogFileUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class MainActivity extends BaseFragmentActivity implements MainTabFragment.OnTabSelectedListener
{
    private FragmentManager fragmentManager = getSupportFragmentManager();
    
    private MainTabFragment tabFragment;
    
    private List<Fragment> mFragmentList;
    
    private ViewPager mViewPager;
    
    private FragmentPagerAdapter mViewPagerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        addTitleFragment();
        addTabFragment();
        
        addContentFragment();
        setFragmentAdapter();
    }
    
    /** 标题 */
    private void addTitleFragment()
    {
        MainTitleFragment titleFragment = new MainTitleFragment();
        fragmentManager.beginTransaction().add(R.id.ll_title, titleFragment).commit();
    }
    
    /** 标签 */
    private void addTabFragment()
    {
        tabFragment = new MainTabFragment();
        fragmentManager.beginTransaction().add(R.id.ll_tab, tabFragment).commit();
    }
    
    /** 内容 */
    private void addContentFragment()
    {
        mViewPager = (ViewPager)findViewById(R.id.vp_content);
        
        mFragmentList = new ArrayList<Fragment>();
        
        MainContentFragmentOne tab_1 = new MainContentFragmentOne();
        MainContentFragmentTwo tab_2 = new MainContentFragmentTwo();
        MainContentFragmentThree tab_3 = new MainContentFragmentThree();
        MainContentFragmentFour tab_4 = new MainContentFragmentFour();
        
        mFragmentList.add(tab_1);
        mFragmentList.add(tab_2);
        mFragmentList.add(tab_3);
        mFragmentList.add(tab_4);
    }
    
    private void setFragmentAdapter()
    {
        mViewPagerAdapter = new fragmentViewPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new tabOnPageChangeListener());
    }
    
    private class fragmentViewPagerAdapter extends FragmentPagerAdapter
    {
        
        public fragmentViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        
        @Override
        public Fragment getItem(int arg0)
        {
            return mFragmentList.get(arg0);
        }
        
        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }
    }
    
    private class tabOnPageChangeListener implements ViewPager.OnPageChangeListener
    {
        
        @Override
        public void onPageScrollStateChanged(int position)
        {
            
        }
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPx)
        {
            tabFragment.moveTabLine(position, positionOffset);
        }
        
        @Override
        public void onPageSelected(int position)
        {
            tabFragment.resetTextColor(position);
        }
    }
    
    @Override
    public void onTabSelected(int position)
    {
        LogFileUtil.v(MainApplication.TAG, "onTabSelected position = " + position);
        mViewPager.setCurrentItem(position);
    }
}
