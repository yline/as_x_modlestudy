package com.viewpager.tab.TabLayoutActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.view.viewpager.tab.R;
import com.viewpager.tab.ShowFragment;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, TabLayoutActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private ViewPager viewPager;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private List<ShowFragment> fragmentList;
    private static final int CACHE_SIZE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        initView();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.tab_layout_viewpager);
    }

    private void initData() {
        fragmentList = new ArrayList<>();

        ShowFragment show1 = new ShowFragment();
        show1.setShowStr("show1");
        ShowFragment show2 = new ShowFragment();
        show2.setShowStr("show2");
        ShowFragment show3 = new ShowFragment();
        show3.setShowStr("show3");

        fragmentList.add(show1);
        fragmentList.add(show2);
        fragmentList.add(show3);

        // 这个属于放置数据的地方
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new CustomFragmentPageAdapter(fragmentManager, viewPager) {

            @Override
            public int getCount() {
                return 20; // destroyItem
            }

            @Override
            public Fragment getItem(int position) {
                ShowFragment show = fragmentList.get(position % CACHE_SIZE);
//                show.setShowStr("show" + (position % 3));
                return fragmentList.get(position % CACHE_SIZE);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "Tab" + position;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.v("onPageSelected" + position);
                fragmentList.get(position % CACHE_SIZE).setShowStr("select" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout_tab);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, android.R.color.black), ContextCompat.getColor(this, android.R.color.holo_red_light));
    }
}
