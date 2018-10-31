package com.kjtpay.ndk;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES对称加密
 * 直接调用的api引入了Base64转码加密
 * 原因：若无转码，则生成的字符串会产生乱码，乱码的字符串获取的byte是错误的
 *
 * @author yline 2018/8/28 -- 18:05
 */
public class AESUtils {
	/* 算法 */
	private static final String ALGORITHM = "AES";
	
	/* 算法/模式/补码方式 */
	private static final String METHOD = "AES/CTR/NoPadding";
	
	/* 使用CBC模式，需要一个向量iv，可增加加密算法的强度 */
	private static final String PARAMETER_SPEC = "1234567890123456";
	
	/**
	 * AES 加密
	 *
	 * @param srcBytes      原始数据（待加密的数据）
	 * @param keyBytes      秘钥，要求16位
	 * @param parameterSpec 偏移量
	 * @return 加密后的byte数组
	 */
	public static byte[] encrypt(byte[] srcBytes, byte[] keyBytes, byte[] parameterSpec) {
		return encryptInner(srcBytes, keyBytes, parameterSpec, METHOD);
	}
	
	/**
	 * Base64转码解密 + AES 解密
	 *
	 * @param sSrc 原始数据（待解密的数据）
	 * @param sKey 秘钥，要求16位
	 * @return 解密后的byte数组
	 */
	public static String decrypt(String sSrc, String sKey) {
		if (null == sSrc || null == sKey) {
			return null;
		}
		
		byte[] baseBytes = Base64.decode(sSrc, Base64.DEFAULT); // base64转码并解密
		byte[] decryptedBytes = decryptInner(baseBytes, sKey.getBytes(), PARAMETER_SPEC.getBytes(), METHOD); // AES解密
		return (null == decryptedBytes ? null : new String(decryptedBytes));
	}
	
	/**
	 * AES 解密
	 *
	 * @param srcBytes      原始数据（待解密的数据）
	 * @param keyBytes      秘钥，要求16位
	 * @param parameterSpec 偏移量
	 * @return 解密后的byte数组
	 */
	public static byte[] decrypt(byte[] srcBytes, byte[] keyBytes, byte[] parameterSpec) {
		return decryptInner(srcBytes, keyBytes, parameterSpec, METHOD);
	}
	
	/**
	 * 随机生成一个 AES 字符串
	 *
	 * @param length 16 * 8
	 * @return 字符流
	 */
	public static byte[] createAESKey(int length) {
		return createAESKeyInner(length);
	}
	
	/* ---------------------------- 内部实现 ---------------------------- */
	
	/**
	 * 随机生成一个 AES 字符串
	 *
	 * @param length 16 * 8
	 * @return 字符流
	 */
	private static byte[] createAESKeyInner(int length) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			generator.init(length);
			return generator.generateKey().getEncoded();
		} catch (NoSuchAlgorithmException e) {
			return "1234567887654321".getBytes();
		}
	}
	
	/**
	 * AES 加密
	 *
	 * @param srcBytes      原始数据（待加密的数据）
	 * @param keyBytes      秘钥，要求16位
	 * @param parameterSpec 偏移量
	 * @param method        加密方法
	 * @return 加密后的byte数组
	 */
	private static byte[] encryptInner(byte[] srcBytes, byte[] keyBytes, byte[] parameterSpec, String method) {
		if (null == srcBytes || null == keyBytes || keyBytes.length != 16 || null == parameterSpec) {
			return null;
		}
		
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			
			Cipher cipher = Cipher.getInstance(method);
			IvParameterSpec iv = new IvParameterSpec(parameterSpec);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			
			return cipher.doFinal(srcBytes);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * AES 解密
	 *
	 * @param srcBytes      原始数据（待解密的数据）
	 * @param keyBytes      秘钥，要求16位
	 * @param parameterSpec 偏移量
	 * @param method        解密方式
	 * @return 解密后的byte数组
	 */
	private static byte[] decryptInner(byte[] srcBytes, byte[] keyBytes, byte[] parameterSpec, String method) {
		if (null == srcBytes || null == keyBytes || keyBytes.length != 16 || null == parameterSpec) {
			return null;
		}
		
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			Cipher cipher = Cipher.getInstance(method);
			IvParameterSpec iv = new IvParameterSpec(parameterSpec);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			
			return cipher.doFinal(srcBytes);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
