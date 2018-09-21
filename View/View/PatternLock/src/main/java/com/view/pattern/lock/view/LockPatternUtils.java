package com.view.pattern.lock.view;

import android.text.TextUtils;

import com.view.pattern.lock.MainApplication;
import com.yline.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

public class LockPatternUtils {
	/**
	 * The minimum number of dots in a valid pattern.
	 */
	public static final int MIN_LOCK_PATTERN_SIZE = 4;
	
	/**
	 * The maximum number of incorrect attempts before the user is prevented
	 * from trying again for {@link #FAILED_ATTEMPT_TIMEOUT_MS}.
	 */
	public static final int FAILED_ATTEMPTS_BEFORE_TIMEOUT = 5;
	
	/**
	 * How long the user is prevented from trying again after entering the wrong
	 * pattern too many times.
	 */
	public static final long FAILED_ATTEMPT_TIMEOUT_MS = 30000L;
	
	private static final String KEY_PATTERN = "key_pattern";
	
	/**
	 * Check to see if the user has stored a lock pattern.
	 *
	 * @return Whether a saved pattern exists.
	 */
	public static boolean savedPatternExists() {
		String oldPatternString = (String) SPUtil.get(MainApplication.getApplication(), KEY_PATTERN, null);
		return (!TextUtils.isEmpty(oldPatternString));
	}
	
	public static void clearLock() {
		saveLockPattern(null);
	}
	
	/**
	 * Save a lock pattern.
	 *
	 * @param pattern The new pattern to save.
	 */
	public static void saveLockPattern(List<LockPatternView.Cell> pattern) {
		String patternString = LockPatternView.patternToString(pattern);
		SPUtil.put(MainApplication.getApplication(), KEY_PATTERN, patternString);
	}
	
	/**
	 * Check to see if a pattern matches the saved pattern. If no pattern
	 * exists, always returns true.
	 *
	 * @param pattern The pattern to check.
	 * @return Whether the pattern matches the stored one.
	 */
	public static boolean checkPattern(List<LockPatternView.Cell> pattern) {
		String oldPatternString = (String) SPUtil.get(MainApplication.getApplication(), KEY_PATTERN, null);
		String newPatternString = LockPatternView.patternToString(pattern);
		return newPatternString.equalsIgnoreCase(oldPatternString);
	}
}
