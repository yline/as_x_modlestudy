package com.appother.subunit.welcome.style;

import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.appother.subunit.R;
import com.appother.subunit.activity.MainActivity;
import com.appother.subunit.activity.MainApplication;

public class MoveActionStyle extends StyleBase
{
	/** 闪烁时间 */
	private static final int SPLASH_TIME = 2000;

	@Override
	public void init(final Activity activity)
	{
		activity.setContentView(R.layout.activity_move_action);

		TextView title = (TextView) activity.findViewById(R.id.tv_logo);
		ImageView logo = (ImageView) activity.findViewById(R.id.iv_logo);

		Animation logoDown = AnimationUtils.loadAnimation(activity, R.anim.move_action_down);
		Animation logoUp = AnimationUtils.loadAnimation(activity, R.anim.move_action_up);
		logo.startAnimation(logoDown);
		title.startAnimation(logoUp);

		MainApplication.getHandler().postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				activity.startActivity(new Intent(activity, MainActivity.class));
				activity.finish();
			}
		}, SPLASH_TIME);
	}
}
