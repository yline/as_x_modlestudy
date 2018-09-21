package com.view.pattern.lock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.view.pattern.lock.MainApplication;
import com.view.pattern.lock.R;
import com.view.pattern.lock.view.LockPatternUtils;
import com.view.pattern.lock.view.LockPatternView;
import com.yline.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PwdCreateActivity extends BaseActivity {
	public static void actionStart(Context context) {
		context.startActivity(new Intent(context, PwdCreateActivity.class));
	}
	
	protected TextView tvHint;
	private Button mFooterLeftButton;
	private Button mFooterRightButton;
	private LockPatternView mLockPatternView;
	
	protected List<LockPatternView.Cell> mChosenPattern = null;
	
	private LockPatternView mShowPatternView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_create);
		
		initView();
		showUserHelper();
	}
	
	private void initView() {
		tvHint = findViewById(R.id.pwd_create_hint);
		
		mLockPatternView = findViewById(R.id.pwd_create_pattern);
		
		mShowPatternView = findViewById(R.id.pwd_create_show);
		List<LockPatternView.Cell> showPattern = new ArrayList<>();
		showPattern.add(LockPatternView.Cell.of(0, 0));
		showPattern.add(LockPatternView.Cell.of(0, 1));
		showPattern.add(LockPatternView.Cell.of(1, 1));
		showPattern.add(LockPatternView.Cell.of(1, 2));
		showPattern.add(LockPatternView.Cell.of(2, 2));
		mShowPatternView.setPattern(LockPatternView.DisplayMode.Normal, showPattern);
		
		mFooterLeftButton = findViewById(R.id.pwd_create_reset);
		mFooterRightButton = findViewById(R.id.pwd_create_right);
	}
	
	/**
	 * 展示用户帮助的界面
	 */
	private void showUserHelper() {
		tvHint.setText("如何绘制解锁图案");
		
		mFooterLeftButton.setVisibility(View.GONE);
		mFooterLeftButton.setOnClickListener(null);
		
		mFooterRightButton.setVisibility(View.VISIBLE);
		mFooterRightButton.setText("确定");
		mFooterRightButton.setEnabled(true);
		mFooterRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLockPatternView.clearPattern();
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Normal);
				showUserFirstDraw();
			}
		});
		
		List<LockPatternView.Cell> animatePattern = new ArrayList<>();
		animatePattern.add(LockPatternView.Cell.of(0, 0));
		animatePattern.add(LockPatternView.Cell.of(0, 1));
		animatePattern.add(LockPatternView.Cell.of(1, 1));
		animatePattern.add(LockPatternView.Cell.of(2, 1));
		animatePattern.add(LockPatternView.Cell.of(2, 2));
		mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Normal);
		mLockPatternView.setPattern(LockPatternView.DisplayMode.Animate, animatePattern);
		
		mLockPatternView.disableInput();
		mLockPatternView.setOnPatternListener(null);
	}
	
	private void showUserFirstDraw() {
		tvHint.setText("绘制解锁图案");
		mFooterLeftButton.setText("取消");
		mFooterLeftButton.setVisibility(View.VISIBLE);
		mFooterLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mFooterRightButton.setText("继续");
		mFooterRightButton.setVisibility(View.VISIBLE);
		mFooterRightButton.setEnabled(false);
		mFooterRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUserSecondDraw();
			}
		});
		
		mLockPatternView.enableInput();
		mLockPatternView.clearPattern();
		mLockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
			@Override
			public void onPatternStart() {
				tvHint.setText("完成后松开手指");
				mFooterLeftButton.setEnabled(false);
				mFooterRightButton.setEnabled(false);
			}
			
			@Override
			public void onPatternCleared() {
			}
			
			@Override
			public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
			}
			
			@Override
			public void onPatternDetected(List<LockPatternView.Cell> pattern) {
				/* 若绘制的图片没有的话 */
				if (pattern == null) {
					return;
				}
				
				mShowPatternView.setPattern(LockPatternView.DisplayMode.Normal, pattern);
				
				if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
					tvHint.setText("至少连接4个点，请重试");
					mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Error);
					postClearPatternRunnable();
					
					mFooterLeftButton.setEnabled(true);
				} else {
					/* 这里保存一份,首次绘制的Pattern */
					mChosenPattern = new ArrayList<>(pattern);
					
					tvHint.setText("图案已记录");
					
					mFooterRightButton.setEnabled(true);
					
					mLockPatternView.disableInput();
					mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Normal);
				}
			}
		});
	}
	
	private void showUserSecondDraw() {
		tvHint.setText("请再次绘制解锁图案");
		mFooterLeftButton.setEnabled(true);
		mFooterLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mFooterRightButton.setEnabled(false);
		mFooterRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LockPatternUtils.saveLockPattern(mChosenPattern);
				
				MainApplication.toast("设置成功");
				startActivity(new Intent(PwdCreateActivity.this, PwdUnlockActivity.class));
				finish();
			}
		});
		
		mLockPatternView.enableInput();
		mLockPatternView.clearPattern();
		mLockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
			@Override
			public void onPatternStart() {
				tvHint.setText("完成后松开手指");
				mFooterLeftButton.setEnabled(false);
				mFooterRightButton.setEnabled(false);
			}
			
			@Override
			public void onPatternCleared() {
			}
			
			@Override
			public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
			}
			
			@Override
			public void onPatternDetected(List<LockPatternView.Cell> pattern) {
				// 判断校验是否正确
				if (mChosenPattern.equals(pattern)) {
					tvHint.setText("确认保存新解锁图案");
					
					mFooterRightButton.setEnabled(true);
					mFooterRightButton.setText("确认");
					
					mLockPatternView.disableInput();
					mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Normal);
				} else {
					tvHint.setText("与上次输入不一致，请重试");
					
					mFooterLeftButton.setEnabled(true);
					
					mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Error);
					postClearPatternRunnable();
				}
			}
		});
	}
	
	/**
	 * clear the wrong pattern unless they have started a new one already
	 */
	private void postClearPatternRunnable() {
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}
	
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};
}
