package com.kjtpay.ndk;

import android.util.Base64;

/**
 * 加密流程
 *
 * @author linjiang@kjtpay.com  2018/10/31 -- 10:21
 */
public class KjtEncryptUtils {
	private static final String SPLIT = "$";
	
	public static String encryptRequest(String data, String rsaPublicKey) {
		String version = "10101";
		byte[] aesKey = AESUtils.createAESKey(16 * 8);
		byte[] nonce = AESUtils.createAESKey(16 * 8); // Nonce 长度为16
		byte[] hmacKey = AESUtils.createAESKey(32 * 8); // Hmac 长度为32
		
		// 加密
		byte[] rsaPublicKeyByte = Base64.decode(rsaPublicKey, Base64.NO_WRAP);
		byte[] rsaHmac = RSAUtils.encrypt(hmacKey, rsaPublicKeyByte);
		byte[] rsaAes = RSAUtils.encrypt(aesKey, rsaPublicKeyByte);
		
		byte[] nonceData = AESUtils.encrypt(data.getBytes(), aesKey, nonce);
		byte[] hmacSign = HmacUtils.sign(nonceData, hmacKey);
		
		// 拼接
		return wrap(version, rsaHmac, rsaAes, nonce, nonceData, hmacSign);
	}
	
	private static String wrap(String version, byte[] rsaHmac, byte[] rsaAes, byte[] nonce, byte[] nonceData, byte[] hmacSign) {
		return version + SPLIT +
				Base64Utils.encodeToString(rsaHmac) + SPLIT +
				Base64Utils.encodeToString(rsaAes) + SPLIT +
				Base64Utils.encodeToString(nonce) + SPLIT +
				Base64Utils.encodeToString(nonceData) + SPLIT +
				Base64Utils.encodeToString(hmacSign);
	}
	
	public static String decryptRequest(String data, String rsaPrivateKey) {
		String[] dataArray = data.split("\\" + SPLIT);
		if (dataArray.length != 6) {
			return null;
		}
		
		String version = dataArray[0];
		String rsaHmacStr = dataArray[1];
		byte[] rsaHmac = Base64Utils.decode(rsaHmacStr);
		
		String rsaAesStr = dataArray[2];
		byte[] rsaAes = Base64Utils.decode(rsaAesStr);
		
		String nonceStr = dataArray[3];
		byte[] nonce = Base64Utils.decode(nonceStr);
		
		String nonceDataStr = dataArray[4];
		byte[] nonceData = Base64Utils.decode(nonceDataStr);
		
		String hmacSignStr = dataArray[5];
		
		byte[] rsaPrivateKeyByte = Base64.decode(rsaPrivateKey, Base64.NO_WRAP);
		byte[] hmacKey = RSAUtils.decrypt(rsaHmac, rsaPrivateKeyByte);
		byte[] aesKey = RSAUtils.decrypt(rsaAes, rsaPrivateKeyByte);
		
		byte[] hmacSign = HmacUtils.sign(nonceData, hmacKey);
		boolean verifySign = hmacSignStr.equals(Base64Utils.encodeToString(hmacSign));
		if (verifySign) {
			byte[] result = AESUtils.decrypt(nonceData, aesKey, nonce);
			return new String(result);
		} else {
			return null;
		}
	}
}
