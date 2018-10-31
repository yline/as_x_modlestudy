package com.kjtpay.ndk;

import android.util.Base64;

/**
 * 使用系统api
 *
 * @author yline 2018/8/28 -- 18:04
 */
public class Base64Utils {
	public static String encodeToString(byte[] data) {
		return Base64.encodeToString(data, Base64.NO_WRAP);
	}
	
	public static byte[] encode(byte[] data) {
		return Base64.encode(data, Base64.NO_WRAP);
	}
	
	public static byte[] decode(String data) {
		return Base64.decode(data, Base64.NO_WRAP);
	}
	
	public static byte[] decode(byte[] data) {
		return Base64.decode(data, Base64.NO_WRAP);
	}
}
