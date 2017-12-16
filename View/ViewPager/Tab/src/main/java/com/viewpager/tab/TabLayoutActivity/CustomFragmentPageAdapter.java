package com.viewpager.tab.TabLayoutActivity;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 FragmentAdapter
 *
 * @author yline 2017/12/16 -- 9:36
 * @version 1.0.0
 */
public abstract class CustomFragmentPageAdapter extends PagerAdapter {
    private int mCurrentPosition;
    private boolean isPrimaryAttached;
    private List<Fragment> mCacheFragmentList;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public CustomFragmentPageAdapter(FragmentManager fm, ViewPager viewPager) {
        mFragmentManager = fm;
        mCacheFragmentList = new ArrayList<>();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isPrimaryAttached = false;
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        LogUtil.v("instantiateItem item #" + itemId);
        Fragment fragment = getItem(position);
        if (!mCacheFragmentList.contains(fragment)) {
            mCurTransaction.add(container.getId(), fragment);
            mCacheFragmentList.add(fragment);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LogUtil.v("destroyItem item #" + getItemId(position) + ": f=" + object + " v=" + ((Fragment) object).getView());
    }

    /**
     * 在完成绘制之前，会调用一次
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Fragment fragment = getItem(position);
        final long itemId = getItemId(position);
        if (mCacheFragmentList.contains(fragment) && mCurrentPosition == position) {
            if (!isPrimaryAttached) {
                LogUtil.v("Attaching item #" + itemId + ": f=" + fragment);
                mCurTransaction.detach(fragment);
                mCurTransaction.attach(fragment);
                isPrimaryAttached = true;
            }
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    /**
     * Return a unique identifier for the item at the given position.
     * <p>
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }
}
