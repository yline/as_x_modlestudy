package com.yline.coor.behavior.type2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yline.utils.LogUtil;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * 日志效果
 *
 * 刚刚进入时
 * 1，layoutDependsOn 调用多次，返回 true
 * 2，onDependentViewChanged 调用一次，height = 400.0, y = 0.0, dy = 400.0
 *
 * 往上滑动时，TextView 可见   // 需要 Type2TitleBehavior 实现对应的内容
 * 1，layoutDependsOn 和 onDependentViewChanged 每次都调用一次
 * 2，y 从 0-(-400), dy从400-0
 *
 * 往上滑动时，TextView 不可见  // 需要 Type2TitleBehavior 实现对应的内容
 * 不出现生命周期
 *
 * @author yline 2020/5/18 -- 16:14
 */
public class Type2RecyclerBehavior extends CoordinatorLayout.Behavior<View> {
    public Type2RecyclerBehavior() {
    }

    public Type2RecyclerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        boolean result = dependency instanceof TextView;
        LogUtil.v("recycler result = " + result);

        return result;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        float dependencyHeight = dependency.getHeight();
        float dependencyY = dependency.getTranslationY();

        float dy = dependencyHeight + dependencyY;
        dy = Math.max(0, dy);
        LogUtil.v("height = " + dependencyHeight + ", y = " + dependencyY + ", dy = " + dy);

        child.setY(dy);     // 初始值，就将 recyclerView 偏移到 text下面
        return true;
    }
}
