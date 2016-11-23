package com.view.pattern.lock.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.view.pattern.lock.R;
import com.view.pattern.lock.view.LockPatternHelper;
import com.view.pattern.lock.view.LockPatternView;
import com.yline.base.BaseActivity;

import java.util.List;

/**
 * 解锁页面
 * @author yline 2016/11/23 --> 21:00
 * @version 1.0.0
 */
public class UnlockPwdActivity extends BaseActivity
{
	private LockPatternView mLockPatternView;

	private int failedNumberSinceLastTimeOut = 0;

	private CountDownTimer mCountdownTimer = null;

	private TextView tvHint;

	private Animation mShakeAnim;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_unlock);

		mLockPatternView = (LockPatternView) this.findViewById(R.id.view_pattern_lock);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		
		tvHint = (TextView) findViewById(R.id.tv_unlock_hint);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (!LockPatternHelper.getInstance().savedPatternExists())
		{
			GuideGesturePasswordActivity.actionStart(this);
			finish();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mCountdownTimer != null)
		{
			mCountdownTimer.cancel();
		}

		if (LockPatternHelper.getInstance().savedPatternExists())
		{
			LockPatternHelper.getInstance().clearLock();
		}
	}

	private Runnable mClearPatternRunnable = new Runnable()
	{
		public void run()
		{
			mLockPatternView.clearPattern();
		}
	};

	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener()
	{
		@Override
		public void onPatternStart()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		@Override
		public void onPatternCleared()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		@Override
		public void onPatternDetected(List<LockPatternView.Cell> pattern)
		{
			if (null == pattern)
			{
				return;
			}

			if (LockPatternHelper.getInstance().checkPattern(pattern))
			{
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);

				GuideGesturePasswordActivity.actionStart(UnlockPwdActivity.this);
				MainApplication.toast("输入正确");
				finish();
			}
			else
			{
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				if (pattern.size() >= LockPatternHelper.MIN_PATTERN_REGISTER_FAIL)
				{
					failedNumberSinceLastTimeOut++;
					int retry = LockPatternHelper.FAILED_ATTEMPTS_BEFORE_TIMEOUT - failedNumberSinceLastTimeOut;
					if (retry >= 0)
					{
						if (retry == 0)
						{
							MainApplication.toast("设备被锁定");
						}
						tvHint.setText("剩余次数：" + retry);
						tvHint.setTextColor(Color.RED);
						tvHint.startAnimation(mShakeAnim);
					}
				}
				else
				{
					MainApplication.toast("少于最少个数,请重新输入");
				}

				if (failedNumberSinceLastTimeOut >= LockPatternHelper.FAILED_ATTEMPTS_BEFORE_TIMEOUT)
				{
					// 倒计时,重新输入
					MainApplication.getHandler().postDelayed(attemptLockout, 2000);
				}
				else
				{
					// 开启清除线程
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		@Override
		public void onPatternCellAdded(List<LockPatternView.Cell> pattern)
		{

		}
	};
	
	private Runnable attemptLockout = new Runnable()
	{

		@Override
		public void run()
		{
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(LockPatternHelper.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000)
			{

				@Override
				public void onTick(long millisUntilFinished)
				{
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0)
					{
						tvHint.setText(secondsRemaining + "秒后重新输入");
					}
					else
					{
						tvHint.setText("请重新输入");
					}
				}

				@Override
				public void onFinish()
				{
					mLockPatternView.setEnabled(true);
					failedNumberSinceLastTimeOut = 0;
				}
			}.start();
		}
	};
}
