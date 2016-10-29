package com.view.animate.helper;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.view.animate.R;

/**
 * Created by yline on 2016/10/2.
 */
public class AlphaAnimationHelper
{
	/**
	 * 获取透明方式变换的Animation
	 * 通过java代码方式
	 * @return
	 */
	public Animation getAnimation()
	{
		Animation animation = new AlphaAnimation(1.0f, 0.1f);
		animation.setDuration(1000);
		animation.setFillBefore(true); // 回到最初默认状态,默认为true
		return animation;
	}

	/**
	 * 获取透明方式变换的Animation
	 * 通过xml代码方式
	 * @param context
	 * @return
	 */
	public Animation getAnimation(Context context)
	{
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.animator_alpha);
		return animation;
	}
}
