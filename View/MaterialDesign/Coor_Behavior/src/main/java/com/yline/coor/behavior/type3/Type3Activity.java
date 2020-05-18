package com.yline.coor.behavior.type3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.yline.base.BaseAppCompatActivity;
import com.yline.coor.behavior.R;
import com.yline.coor.behavior.common.SimpleFragment;
import com.yline.coor.behavior.type3.behavior.MainHeaderBehavior;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Type3Activity extends BaseAppCompatActivity implements MainHeaderBehavior.OnHeaderStateListener {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, Type3Activity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private MainHeaderBehavior mHeaderBehavior;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type3);

        initView();
    }

    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        fragmentList.add(SimpleFragment.create(20));
        fragmentList.add(SimpleFragment.create(30));
        fragmentList.add(SimpleFragment.create(40));

        titleList.add("tab20");
        titleList.add("tab30");
        titleList.add("tab40");

        mViewPager = findViewById(R.id.type3_view_pager);
        TabLayout tabLayout = findViewById(R.id.type3_tab_layout);

        TypePageAdapter typePageAdapter = new TypePageAdapter(getSupportFragmentManager());
        typePageAdapter.setData(fragmentList, titleList);

        mViewPager.setAdapter(typePageAdapter);
        mViewPager.setOffscreenPageLimit(titleList.size() - 1);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onHeaderClosed() {
        LogUtil.v("closed");
    }

    @Override
    public void onHeaderOpened() {
        LogUtil.v("opened");
    }

    @Override
    public void onBackPressed() {
        if (mHeaderBehavior != null && mHeaderBehavior.isClosed()) {
            mHeaderBehavior.openHeader();
        } else {
            super.onBackPressed();
        }
    }

    private static class TypePageAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        private List<String> mTitleList;

        public TypePageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        private void setData(List<Fragment> fragmentList, List<String> titleList) {
            this.mFragmentList = fragmentList;
            this.mTitleList = titleList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
