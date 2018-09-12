package com.view.pattern.lock.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.view.pattern.lock.MainApplication;
import com.view.pattern.lock.R;
import com.view.pattern.lock.view.LockPatternUtils;
import com.view.pattern.lock.view.LockPatternView;
import com.yline.base.BaseActivity;

import java.util.List;
import java.util.Locale;

/**
 * 解锁页面
 *
 * @author yline 2016/11/23 --> 21:00
 * @version 1.0.0
 */
public class PwdUnlockActivity extends BaseActivity {
	private LockPatternView mLockPatternView;
	
	private int failedNumberSinceLastTimeOut = 0;
	
	private CountDownTimer mCountdownTimer = null; // 锁定之后，倒计时
	
	private TextView tvHint;
	
	private Animation mShakeAnim;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_unlock);
		
		mLockPatternView = findViewById(R.id.pwd_unlock_pattern);
		mLockPatternView.setTactileFeedbackEnabled(true);
		
		tvHint = findViewById(R.id.pwd_unlock_hint);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (!LockPatternUtils.savedPatternExists()) {
			PwdCreateActivity.actionStart(this);
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCountdownTimer != null) {
			mCountdownTimer.cancel();
		}
		
		if (LockPatternUtils.savedPatternExists()) {
			LockPatternUtils.clearLock();
		}
	}
	
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};
	
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {
		@Override
		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}
		
		@Override
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}
		
		@Override
		public void onPatternDetected(List<LockPatternView.Cell> pattern) {
			if (null == pattern) {
				return;
			}
			
			if (LockPatternUtils.checkPattern(pattern)) {
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Normal);
				
				PwdCreateActivity.actionStart(PwdUnlockActivity.this);
				MainApplication.toast("输入正确");
				finish();
			} else {
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Error);
				if (pattern.size() >= LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
					failedNumberSinceLastTimeOut++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - failedNumberSinceLastTimeOut;
					if (retry >= 0) {
						if (retry == 0) {
							MainApplication.toast("设备被锁定");
						}
						tvHint.setText(String.format(Locale.CHINA, "剩余次数：%d", retry));
						tvHint.setTextColor(Color.RED);
						tvHint.startAnimation(mShakeAnim);
					}
				} else {
					MainApplication.toast("少于最少个数,请重新输入");
				}
				
				if (failedNumberSinceLastTimeOut >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
					// 倒计时,重新输入
					MainApplication.getHandler().postDelayed(attemptLockout, 2000);
				} else {
					// 开启清除线程
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}
		
		@Override
		public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
		
		}
	};
	
	private Runnable attemptLockout = new Runnable() {
		
		@Override
		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {
						tvHint.setText(secondsRemaining + "秒后重新输入");
					} else {
						tvHint.setText("请重新输入");
					}
				}
				
				@Override
				public void onFinish() {
					mLockPatternView.setEnabled(true);
					failedNumberSinceLastTimeOut = 0;
				}
			}.start();
		}
	};
}
