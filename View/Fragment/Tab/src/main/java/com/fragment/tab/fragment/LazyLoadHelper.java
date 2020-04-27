package com.fragment.tab.fragment;

import android.view.View;

/**
 * 这个完全可以作为一个  测试类，而且，不用管Fragment。要是能通过注解或遍历的方式，就更好了
 *
 * 懒加载，这东西，疑问：如何保证加载之后，一定会被展示出来呢。万一系统没调用生命周期呢
 * https://github.com/rain9155/WanAndroid/blob/master/app/src/main/java/com/example/hy/wanandroid/base/fragment/AbstractLazyLoadFragment.java
 *
 * @author yline 2020/4/27 -- 17:22
 */
public class LazyLoadHelper {
    private View mRootView;
    private boolean mIsVisibleToUser;
    private boolean mIsDataLoaded;

    public void onActivityCreated() {
        resetValues();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.mIsVisibleToUser = isVisibleToUser;

        if (null != mRootView && mIsVisibleToUser && !mIsDataLoaded) {
            if (null != onLazyStartCallback) {
                onLazyStartCallback.onStart(mRootView);
                mIsDataLoaded = true;
            }
        }
    }

    public void onViewCreated(View rootView, boolean isVisibleToUser) {
        this.mRootView = rootView;
        this.mIsVisibleToUser = isVisibleToUser;

        if (null != mRootView && mIsVisibleToUser && !mIsDataLoaded) {
            if (null != onLazyStartCallback) {
                onLazyStartCallback.onStart(mRootView);
                mIsDataLoaded = true;
            }
        }
    }

    public void onHiddenChanged(boolean hidden) {
    }

    public void onDestroyView(){}

    private void resetValues() {
        this.mRootView = null;
        this.mIsVisibleToUser = false;
        this.mIsDataLoaded = false;
    }



    private OnLazyStartCallback onLazyStartCallback;

    public interface OnLazyStartCallback {
        void onStart(View view);
    }

    public void setOnLazyStartCallback(OnLazyStartCallback onLazyStartCallback) {
        this.onLazyStartCallback = onLazyStartCallback;
    }
}
