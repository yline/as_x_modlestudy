package com.view.valueanimator.helper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import com.view.valueanimator.R;
import com.view.valueanimator.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * <animator>  对应代码中的ValueAnimator
 * <objectAnimator>  对应代码中的ObjectAnimator
 * <set>  对应代码中的AnimatorSet
 *
 * @author yline 2016/10/2 --> 17:51
 * @version 1.0.0
 */
public class ObjectAnimatorHelper {
    /**
     * 透明变化的动画
     *
     * @param view
     */
    public void actionAlpha(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f);
        animator.setDuration(2000);
        animator.start();
    }

    /**
     * 透明变化的动画
     *
     * @param context
     * @param view
     */
    public void actionAlpha(Context context, View view) {
        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.anim_alpha);
        animator.setTarget(view);
        animator.start();
    }

    /**
     * 旋转变化的动画
     *
     * @param view
     */
    public void actionRotate(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setDuration(2000);
        animator.start();
    }

    /**
     * 旋转变化的动画
     *
     * @param context
     * @param view
     */
    public void actionRotate(Context context, View view) {
        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.anim_rotate);
        animator.setTarget(view);
        animator.start();
    }

    /**
     * 平移变化的动画
     *
     * @param view
     */
    public void actionTranslate(View view) {
        float curTranslationX = view.getTranslationX();        // 相对于左边的位置，初始 = 0
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", curTranslationX, -view.getRight(), curTranslationX);
        animator.setDuration(2000);
        animator.start();
    }

    /**
     * 平移变化的动画
     *
     * @param context
     * @param view
     */
    public void actionTranslate(Context context, View view) {
        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.anim_translate);
        animator.setTarget(view);
        animator.start();
    }

    /**
     * 比例变化的动画
     *
     * @param view
     */
    public void actionScale(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 3f, 1f);
        animator.setDuration(2000);
        animator.start();
    }

    /**
     * 多种动画混成一起
     *
     * @param view
     */
    public void actionAnimatorSet(View view) {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).after(moveIn);
        animSet.setDuration(5000);
        // 监听 事件
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                LogFileUtil.i(MainApplication.TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                LogFileUtil.i(MainApplication.TAG, "onAnimationRepeat");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                LogFileUtil.i(MainApplication.TAG, "onAnimationCancel");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LogFileUtil.i(MainApplication.TAG, "onAnimationEnd");
            }
        });
        animSet.start();
    }

    /**
     * 多种动画混成一起
     *
     * @param context
     * @param view
     */
    public void actionAnimatorSet(Context context, View view) {
        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.anim_set);
        animator.setTarget(view);
        animator.start();
    }

    /**
     * 比例变化的动画
     *
     * @param context
     * @param view
     */
    public void actionScale(Context context, View view) {
        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.anim_scale);
        animator.setTarget(view);
        animator.start();
    }
}
