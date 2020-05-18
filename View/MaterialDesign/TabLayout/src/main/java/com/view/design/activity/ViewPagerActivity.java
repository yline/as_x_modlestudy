package com.view.design.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.view.design.R;
import com.view.design.fragment.DeleteFragment1;
import com.view.design.fragment.DeleteFragment2;
import com.view.design.fragment.DeleteFragment3;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends BaseAppCompatActivity {
    private ViewPager viewPager;

    private TabLayout tabLayout;

    private static final String[] RES = {"tab1", "tab2", "tab3"};

    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        initView();

        fragmentList = new ArrayList<>();
        fragmentList.add(new DeleteFragment1());
        fragmentList.add(new DeleteFragment2());
        fragmentList.add(new DeleteFragment3());

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return RES.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return RES[position];
            }
        };

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainApplication.toast(RES[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); // GRAVITY_FILL  尽可能填充  || GRAVITY_CENTER 居中
        tabLayout.setTabMode(TabLayout.MODE_FIXED); // MODE_FIXED 固定Tab || MODE_SCROLLABLE 可滚动tabs
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, ViewPagerActivity.class));
    }
}
