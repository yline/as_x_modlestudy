package com.kjtpay.ndk;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacUtils {
	private static final String ALGORITHM = "HmacSHA256";
	
	/**
	 * Hmac签名
	 *
	 * @param data    数据
	 * @param hmacKey hmac秘钥
	 * @return 加密后内容
	 */
	public static byte[] sign(byte[] data, byte[] hmacKey) {
		if (null == data || null == hmacKey) {
			return null;
		}
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(hmacKey, ALGORITHM);
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(secretKeySpec);
			return mac.doFinal(data);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
			return null;
		}
	}
}
