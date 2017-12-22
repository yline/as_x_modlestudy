package com.coordinator.collapsingtoolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.coordinator.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.adapter.AbstractPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollapsingToolbarActivity extends BaseAppCompatActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, CollapsingToolbarActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.collapsing_toolbar_tab_layout);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        tabLayout.setTabTextColors(ContextCompat.getColor(this, android.R.color.holo_red_light), ContextCompat.getColor(this, android.R.color.black));

        ViewPager viewPager = (ViewPager) findViewById(R.id.collapsing_toolbar_view_pager);
        tabLayout.setupWithViewPager(viewPager);

        AbstractPagerAdapter viewPagerAdapter = new AbstractPagerAdapter() {
            @Override
            public CharSequence getPageTitle(int position) {
                return StrConstant.getStringByRandom(StrConstant.getStringArrayByRandom()) + '\r' + position;
            }
        };
        viewPager.setAdapter(viewPagerAdapter);

        View oneView = LayoutInflater.from(this).inflate(R.layout.view_collapsing_toolbar, null);
        View twoView = LayoutInflater.from(this).inflate(R.layout.view_collapsing_toolbar, null);

        List<View> mViewList = new ArrayList<>();
        mViewList.add(oneView);
        mViewList.add(twoView);
        viewPagerAdapter.setViews(mViewList, true);

        initData((TextView) oneView.findViewById(R.id.view_collapsing_toolbar_tv), (TextView) twoView.findViewById(R.id.view_collapsing_toolbar_tv));
    }

    private void initData(TextView oneTextView, TextView twoTextView) {
        for (int i = 0; i < 55; i++) {
            oneTextView.append(i + "   " + StrConstant.getStringByRandom(StrConstant.getStringArrayByRandom()) + '\n');
            twoTextView.append(i + "   " + StrConstant.getStringByRandom(StrConstant.getStringArrayByRandom()) + '\n');
        }
    }
}
