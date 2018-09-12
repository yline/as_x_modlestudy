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
		String patternString = patternToString(pattern);
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
		String newPatternString = patternToString(pattern);
		return newPatternString.equalsIgnoreCase(oldPatternString);
	}
	
	/**
	 * Deserialize a pattern. 将String转成Pattern
	 *
	 * @return The pattern.
	 */
	public static List<LockPatternView.Cell> stringToPattern(String string) {
		List<LockPatternView.Cell> result = new ArrayList<>();
		
		final byte[] bytes = string.getBytes();
		for (byte b : bytes) {
			result.add(LockPatternView.Cell.of(b / 3, b % 3));
		}
		return result;
	}
	
	/**
	 * Serialize a pattern. 将Pattern转成String
	 *
	 * @param pattern The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<LockPatternView.Cell> pattern) {
		if (pattern == null) {
			return "";
		}
		final int patternSize = pattern.size();
		
		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			LockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		return new String(res);
	}
}
