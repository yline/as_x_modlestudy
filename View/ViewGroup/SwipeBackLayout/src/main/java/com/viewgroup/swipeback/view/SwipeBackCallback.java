package com.viewgroup.swipeback.view;

/**
 * 滑动的实现方法
 *
 * @author yline 2017/10/26 -- 11:18
 * @version 1.0.0
 */
public interface SwipeBackCallback {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
