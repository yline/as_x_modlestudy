package com.yline.coor.behavior.type1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.yline.utils.LogUtil;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 日志效果：
 *
 * 刚刚进入时
 * 1，layoutDependsOn 调用多次，返回 true false 皆有多次
 * 2，onDependentViewChanged 调用一次，y = 427.0, height = 100.0, percent = 1.0
 *
 * 往上滑动时，未重合
 * 1，layoutDependsOn 每个日志 调用两次
 * 2，onDependentViewChanged 相应调用 1次，height不变，y 从 427-0，percent从 1-0
 *
 * 重合以后
 * 1，layoutDependsOn 不再被调用
 * 2，onDependentViewChanged 不再被调用
 *
 * 问题1：两个构造方法 是如何被调用的，调用的时机
 * 问题2：两个构造方法 返回值是如何被处理的
 *
 * @author yline 2020/5/18 -- 15:41
 */
public class Type1TitleBehavior extends CoordinatorLayout.Behavior<View> {
    private float totalY;  // 当列表顶部 和 title 重合时，列表滑动的 距离

    public Type1TitleBehavior() {
    }

    public Type1TitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean result = dependency instanceof RecyclerView;
        LogUtil.v("result = " + result);
        return result;
    }

    //被观察的view发生改变时回调
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float dependencyY = dependency.getY();
        float childHeight = child.getHeight();

        if (totalY == 0) {
            totalY = dependencyY - childHeight;
        }

        float currentY = dependencyY - childHeight;
        currentY = Math.max(0, currentY);

        float percent = currentY / totalY;
        LogUtil.v("y = " + dependencyY + ", height = " + childHeight + ", percent = " + percent);

        float moveY = percent * child.getHeight();
        child.setTranslationY(-moveY);

        child.setAlpha(1 - percent);
        return true;
    }
}
