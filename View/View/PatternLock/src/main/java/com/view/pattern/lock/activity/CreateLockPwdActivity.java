package com.view.pattern.lock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.view.pattern.lock.R;
import com.view.pattern.lock.view.LockPatternHelper;
import com.view.pattern.lock.view.LockPatternView;
import com.yline.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateLockPwdActivity extends BaseActivity
{
	private static final String KEY_UI_STAGE = "uiStage";

	private static final String KEY_PATTERN_CHOICE = "chosenPattern";

	private LockPatternView mLockPatternView;

	private Button mFooterRightButton;

	private Button mFooterLeftButton;

	protected TextView tvHint;

	protected List<LockPatternView.Cell> mChosenPattern = null;

	private Stage mUiStage = Stage.Introduction;

	/**
	 * The patten used during the help screen to show how to draw a pattern.
	 */
	private final List<LockPatternView.Cell> mAnimatePattern = new ArrayList<LockPatternView.Cell>();

	/**
	 * Keep track internally of where the user is in choosing a pattern.
	 */
	protected enum Stage
	{
		Introduction(R.string.lock_pattern_recording_intro_header,
				android.R.string.cancel, View.VISIBLE,
				R.string.lock_pattern_continue_button_text, false,
				true),
		HelpScreen(R.string.lock_pattern_settings_help_how_to_record,
				android.R.string.unknownName, View.GONE,
				android.R.string.ok, true,
				false),
		ChoiceTooShort(R.string.lock_pattern_recording_incorrect_too_short,
				R.string.lock_pattern_retry_button_text, View.VISIBLE,
				R.string.lock_pattern_continue_button_text, false,
				true),
		FirstChoiceValid(R.string.lock_pattern_pattern_entered_header,
				R.string.lock_pattern_retry_button_text, View.VISIBLE,
				R.string.lock_pattern_continue_button_text, true,
				false),
		NeedToConfirm(R.string.lock_pattern_need_to_confirm,
				android.R.string.cancel, View.VISIBLE,
				R.string.lock_pattern_confirm_button_text, false,
				true),
		ConfirmWrong(R.string.lock_pattern_need_to_unlock_wrong,
				android.R.string.cancel, View.VISIBLE,
				R.string.lock_pattern_confirm_button_text, false,
				true),
		ChoiceConfirmed(R.string.lock_pattern_pattern_confirmed_header,
				android.R.string.cancel, View.VISIBLE,
				R.string.lock_pattern_confirm_button_text, true,
				false);

		Stage(int headerMessage, int leftHint, int leftVisibility, int rightHint, boolean rightEnable, boolean patternEnabled)
		{
			this.headerMessage = headerMessage;
			this.leftHint = leftHint;
			this.leftVisibility = leftVisibility;
			this.rightHint = rightHint;
			this.rightEnable = rightEnable;
			this.patternEnabled = patternEnabled;
		}

		final int headerMessage;

		final int leftHint;

		final int leftVisibility;

		final int rightHint;

		final boolean rightEnable;

		final boolean patternEnabled;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_create);

		initView();

		mAnimatePattern.add(LockPatternView.Cell.of(0, 0));
		mAnimatePattern.add(LockPatternView.Cell.of(0, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(1, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 2));

		if (savedInstanceState == null)
		{
			updateStage(Stage.HelpScreen);
		}
		else
		{
			// restore from previous state
			final String patternString = savedInstanceState.getString(KEY_PATTERN_CHOICE);
			if (patternString != null)
			{
				mChosenPattern = LockPatternHelper.getInstance().stringToPattern(patternString);
			}
			updateStage(Stage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
		}
	}

	private void initView()
	{
		mLockPatternView = (LockPatternView) this.findViewById(R.id.view_pattern_create);
		tvHint = (TextView) this.findViewById(R.id.tv_create_hint);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);

		mFooterLeftButton = (Button) this.findViewById(R.id.btn_reset);
		mFooterLeftButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mUiStage == Stage.ChoiceTooShort || mUiStage == Stage.FirstChoiceValid)
				{
					mChosenPattern = null;
					mLockPatternView.clearPattern();
					updateStage(Stage.Introduction);
				}
				else if (mUiStage == Stage.Introduction || mUiStage == Stage.NeedToConfirm || mUiStage == Stage.ConfirmWrong || mUiStage == Stage.ChoiceConfirmed)
				{
					finish();
				}
				else
				{
					throw new IllegalStateException(
							"left footer button pressed, but stage of " + mUiStage
									+ " doesn't make sense");
				}
			}
		});

		mFooterRightButton = (Button) this.findViewById(R.id.btn_right);
		mFooterRightButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mUiStage == Stage.FirstChoiceValid)
				{
					updateStage(Stage.NeedToConfirm);
				}
				else if (mUiStage == Stage.ChoiceConfirmed)
				{
					LockPatternHelper.getInstance().saveLockPattern(mChosenPattern);

					MainApplication.toast("设置成功");
					startActivity(new Intent(CreateLockPwdActivity.this, UnlockPwdActivity.class));
					finish();
				}
				else if (mUiStage == Stage.HelpScreen)
				{
					mLockPatternView.clearPattern();
					mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
					updateStage(Stage.Introduction);
				}
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
		if (mChosenPattern != null)
		{
			outState.putString(KEY_PATTERN_CHOICE, LockPatternHelper.getInstance().patternToString(mChosenPattern));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			if (mUiStage == Stage.HelpScreen)
			{
				updateStage(Stage.Introduction);
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU && mUiStage == Stage.Introduction)
		{
			updateStage(Stage.HelpScreen);
			return true;
		}
		return false;
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

			tvHint.setText(R.string.lock_pattern_recording_inprogress);
			mFooterLeftButton.setEnabled(false);
			mFooterRightButton.setEnabled(false);
		}

		@Override
		public void onPatternCleared()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		@Override
		public void onPatternDetected(List<LockPatternView.Cell> pattern)
		{
			if (pattern == null)
			{
				return;
			}

			// Log.i("way", "result = " + pattern.toString());
			if (mUiStage == Stage.NeedToConfirm
					|| mUiStage == Stage.ConfirmWrong)
			{
				if (mChosenPattern == null)
					throw new IllegalStateException(
							"null chosen pattern in stage 'need to confirm");
				if (mChosenPattern.equals(pattern))
				{
					updateStage(Stage.ChoiceConfirmed);
				}
				else
				{
					updateStage(Stage.ConfirmWrong);
				}
			}
			else if (mUiStage == Stage.Introduction
					|| mUiStage == Stage.ChoiceTooShort)
			{
				if (pattern.size() < LockPatternHelper.MIN_LOCK_PATTERN_SIZE)
				{
					updateStage(Stage.ChoiceTooShort);
				}
				else
				{
					mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
					updateStage(Stage.FirstChoiceValid);
				}
			}
			else
			{
				throw new IllegalStateException("Unexpected stage " + mUiStage
						+ " when " + "entering the pattern.");
			}
		}

		@Override
		public void onPatternCellAdded(List<LockPatternView.Cell> pattern)
		{

		}
	};

	private void updateStage(Stage stage)
	{
		mUiStage = stage;
		tvHint.setText(stage.headerMessage);

		mFooterLeftButton.setText(stage.leftHint);
		mFooterLeftButton.setVisibility(stage.leftVisibility);

		mFooterRightButton.setText(stage.rightHint);
		mFooterRightButton.setEnabled(stage.rightEnable);

		// same for whether the patten is enabled
		if (stage.patternEnabled)
		{
			mLockPatternView.enableInput();
		}
		else
		{
			mLockPatternView.disableInput();
		}

		mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);

		switch (mUiStage)
		{
			case Introduction:
				mLockPatternView.clearPattern();
				break;
			case HelpScreen:
				mLockPatternView.setPattern(LockPatternView.DisplayMode.Animate, mAnimatePattern);
				break;
			case ChoiceTooShort:
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				postClearPatternRunnable();
				break;
			case FirstChoiceValid:
				break;
			case NeedToConfirm:
				mLockPatternView.clearPattern();
				break;
			case ConfirmWrong:
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				postClearPatternRunnable();
				break;
			case ChoiceConfirmed:
				break;
		}
	}

	/**
	 * clear the wrong pattern unless they have started a new one already
	 */
	private void postClearPatternRunnable()
	{
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, CreateLockPwdActivity.class));
	}
}
