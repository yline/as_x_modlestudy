package com.view.animate.helper;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.view.animate.R;

/**
 * Created by yline on 2016/10/2.
 */
public class TranslateAnimationHelper
{
	/**
	 * 获取平移方式变换的Animation
	 * 通过java代码方式
	 * @return
	 */
	public Animation getAnimation()
	{
		Animation animation = new TranslateAnimation(0.0f, 100.0f, 0.0f, 100.0f);
		animation.setDuration(1000);
		return animation;
	}

	/**
	 * 获取平移方式变换的Animation
	 * 通过xml代码方式
	 * @param context
	 * @return
	 */
	public Animation getAnimation(Context context)
	{
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.animator_translate);
		return animation;
	}
}
