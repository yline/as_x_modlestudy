package com.view.animate.helper;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.view.animate.R;

/**
 * Created by yline on 2016/10/2.
 */
public class AnimationSetHelper
{
	/**
	 * 获取Animation组合
	 * 通过java代码的方式
	 * @return
	 */
	public AnimationSet getAnimationSet()
	{
		AnimationSet animationSet = new AnimationSet(true);
		Animation fAlpha = new AlphaAnimation(1.0f, 0.1f);
		Animation fRotate = new RotateAnimation(0f, 360f);
		Animation fScale = new ScaleAnimation(1.0f, 1.0f, 2.0f, 2.0f);
		Animation fTranslate = new TranslateAnimation(0.0f, 100.0f, 0.0f, 100.0f);
		animationSet.addAnimation(fAlpha);
		animationSet.addAnimation(fRotate);
		animationSet.addAnimation(fScale);
		animationSet.addAnimation(fTranslate);
		animationSet.setDuration(1000);

		return animationSet;
	}
	
	/**
	 * 获取Animation组合
	 * 通过xml代码的方式
	 * @return
	 */
	public AnimationSet getAnimationSet(Context context)
	{
		AnimationSet animationSet = new AnimationSet(true);
		Animation fAlpha = AnimationUtils.loadAnimation(context, R.anim.animator_alpha);
		Animation fRotate = AnimationUtils.loadAnimation(context, R.anim.animator_rotate);
		Animation fScale = AnimationUtils.loadAnimation(context, R.anim.animator_scale);
		Animation fTranslate = AnimationUtils.loadAnimation(context, R.anim.animator_translate);
		animationSet.addAnimation(fAlpha);
		animationSet.addAnimation(fRotate);
		animationSet.addAnimation(fScale);
		animationSet.addAnimation(fTranslate);
		animationSet.setDuration(1000);
		return animationSet;
	}
}
