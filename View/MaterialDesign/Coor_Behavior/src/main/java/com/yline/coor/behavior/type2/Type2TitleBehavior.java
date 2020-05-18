package com.yline.coor.behavior.type2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yline.utils.LogUtil;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 日志记录：
 * onInterceptTouchEvent -> onStartNestedScroll -> onNestedPreScroll（多个）
 *
 * @author yline 2020/5/18 -- 16:38
 */
public class Type2TitleBehavior extends CoordinatorLayout.Behavior<View> {
    //界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    //列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;

    //列表上一个全部可见的item位置
    private int lastPosition = -1;

    public Type2TitleBehavior() {
    }

    public Type2TitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.v("down, upReach = " + upReach + ", downReach = " + downReach);

                upReach = false;
                downReach = false;
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        int result = axes & ViewCompat.SCROLL_AXIS_VERTICAL;
        LogUtil.v("axes = " + axes + ", result = " + result);
        return result != 0;   // 只监听，垂直方向的滑动事件
    }

    // 处理监听到的滑动事件，实现整体滑动和列表单独滑动
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        if (target instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) target;
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (null == linearLayoutManager) {
                return;
            }

            // 列表第一个全部可见Item的位置
            int pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition(); // 第一个item完全可见
            LogUtil.v("dx = " + dx + ",dy = " + dy + ", pos = " + pos);

            if (pos == 0 && pos < lastPosition) {
                downReach = true;
            }

            if (pos == 0 && canScroll(child, dy)) {
                float finalY = child.getTranslationY() - dy;
                if (finalY < -child.getHeight()) {
                    finalY = -child.getHeight();
                    upReach = true;
                } else if (finalY > 0) {
                    finalY = 0;
                }

                child.setTranslationY(finalY);
                consumed[1] = dy;   // 这个应该是赋值，挺重要的
            }

            lastPosition = pos;
        }
    }

    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() == -child.getHeight() && !upReach) {
            return false;
        }

        return !downReach;
    }
}
