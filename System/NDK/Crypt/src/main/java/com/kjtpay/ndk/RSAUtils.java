package com.kjtpay.ndk;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA数字签名 + 加密解密
 * <p>
 * 直接调用的api引入了Base64转码加密
 * 原因：若无转码，则生成的字符串会产生乱码，乱码的字符串获取的byte是错误的
 *
 * @author yline 2018/8/29 -- 15:37
 */
public class RSAUtils {
	/* 单次 RSA加密，明文最大大小 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	
	/* 单次 RSA解密，密文最大大小 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	
	/* 加密算法RSA */
	private static final String ALGORITHM = "RSA";
	
	/* 秘钥长度(公钥和私钥) */
	private static final int KEY_SIZE = 1024;
	
	/* 签名算法 */
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	
	/* 算法/模式/补码方式 */
	private static final String METHOD = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
	
	/**
	 * 耗时较长，测试约300ms左右
	 * 生成秘钥对，公钥和私钥
	 *
	 * @return 秘钥对
	 */
	public static KeyPair createKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取私钥（经过Base64转码加密过）
	 *
	 * @param keyPair 秘钥对
	 * @return 被Base64转码加密过的私钥
	 */
	public static String getPrivateKey(KeyPair keyPair) {
		if (null == keyPair) {
			return null;
		}
		
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		return (null == privateKeyBytes ? null : Base64.encodeToString(privateKeyBytes, Base64.NO_WRAP));
	}
	
	/**
	 * 获取公钥（经过Base64转码加密过）
	 *
	 * @param keyPair 密钥对
	 * @return 被Base64转码加密过的公钥
	 */
	public static String getPublicKey(KeyPair keyPair) {
		if (null == keyPair) {
			return null;
		}
		
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		return (null == publicKeyBytes ? null : Base64.encodeToString(publicKeyBytes, Base64.NO_WRAP));
	}
	
	/**
	 * 使用私钥，对信息，生成数字签名
	 *
	 * @param src        数据
	 * @param privateKey 私钥（被Base64加密过）
	 * @return 生成的数字签名（被Base64加密过）
	 */
	public static String sign(String src, String privateKey) {
		if (null == src || null == privateKey) {
			return null;
		}
		
		byte[] privateKeyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
		byte[] signBytes = signInner(src.getBytes(), privateKeyBytes, SIGNATURE_ALGORITHM);
		return (null == signBytes ? null : Base64.encodeToString(signBytes, Base64.NO_WRAP));
	}
	
	/**
	 * 使用私钥，对信息，生成数字签名
	 *
	 * @param sourceBytes     数据
	 * @param privateKeyBytes 私钥
	 * @param signAlgorithm   签名方式
	 * @return 生成的数字签名
	 */
	public static byte[] sign(byte[] sourceBytes, byte[] privateKeyBytes, String signAlgorithm) {
		return signInner(sourceBytes, privateKeyBytes, signAlgorithm);
	}
	
	/**
	 * 校验数字签名
	 *
	 * @param src       数据
	 * @param publicKey 公钥（被Base64加密过）
	 * @param sign      数字签名（被Base64加密过）
	 * @return true(校验成功)，false(校验失败)
	 */
	public static boolean verifySign(String src, String publicKey, String sign) {
		if (null == src || null == publicKey || null == sign) {
			return false;
		}
		
		byte[] publicKeyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
		byte[] signBytes = Base64.decode(sign, Base64.NO_WRAP);
		return verifySignInner(src.getBytes(), publicKeyBytes, signBytes, SIGNATURE_ALGORITHM);
	}
	
	/**
	 * 校验数字签名
	 *
	 * @param source         数据
	 * @param publicKeyBytes 公钥
	 * @param signBytes      数字签名
	 * @param signAlgorithm  签名方式
	 * @return true(校验成功)，false(校验失败)
	 */
	public static boolean verifySign(byte[] source, byte[] publicKeyBytes, byte[] signBytes, String signAlgorithm) {
		return verifySignInner(source, publicKeyBytes, signBytes, signAlgorithm);
	}
	
	/**
	 * 使用公钥进行 RSA加密
	 *
	 * @param sourceBytes    等待加密的原始数据
	 * @param publicKeyBytes 公钥
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(byte[] sourceBytes, byte[] publicKeyBytes) {
		return encryptInner(sourceBytes, publicKeyBytes, METHOD);
	}
	
	/**
	 * 使用私钥进行 RSA解密
	 *
	 * @param sourceBytes     原始数据，等待解密
	 * @param privateKeyBytes 私钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(byte[] sourceBytes, byte[] privateKeyBytes) {
		return decryptInner(sourceBytes, privateKeyBytes, METHOD);
	}
	
	/* ----------------------------------- 内部实现api ----------------------------------------- */
	
	/**
	 * 使用私钥，对信息，生成数字签名
	 *
	 * @param sourceBytes     数据
	 * @param privateKeyBytes 私钥
	 * @param signAlgorithm   签名方式
	 * @return 生成的数字签名
	 */
	private static byte[] signInner(byte[] sourceBytes, byte[] privateKeyBytes, String signAlgorithm) {
		if (null == sourceBytes || null == privateKeyBytes) {
			return null;
		}
		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			PrivateKey signPrivateKey = keyFactory.generatePrivate(encodedKeySpec);
			
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initSign(signPrivateKey);
			signature.update(sourceBytes);
			
			return signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 校验数字签名
	 *
	 * @param source         数据
	 * @param publicKeyBytes 公钥
	 * @param signBytes      数字签名
	 * @param signAlgorithm  签名方式
	 * @return true(校验成功)，false(校验失败)
	 */
	private static boolean verifySignInner(byte[] source, byte[] publicKeyBytes, byte[] signBytes, String signAlgorithm) {
		if (null == source || null == publicKeyBytes || null == signBytes) {
			return false;
		}
		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			PublicKey signPublicKey = keyFactory.generatePublic(encodedKeySpec);
			
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initVerify(signPublicKey);
			signature.update(source);
			
			return signature.verify(signBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 使用公钥进行 RSA加密
	 *
	 * @param sourceBytes    等待加密的原始数据
	 * @param publicKeyBytes 公钥
	 * @param method         加密方式
	 * @return 加密后的数据
	 */
	private static byte[] encryptInner(byte[] sourceBytes, byte[] publicKeyBytes, String method) {
		if (null == sourceBytes || null == publicKeyBytes) {
			return null;
		}
		
		ByteArrayOutputStream baoStream = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			PublicKey newPublicKey = keyFactory.generatePublic(encodedKeySpec);
			
			Cipher cipher = Cipher.getInstance(method);
			cipher.init(Cipher.ENCRYPT_MODE, newPublicKey);
			
			baoStream = new ByteArrayOutputStream();
			int tempLength, offset = 0, sourceLength = sourceBytes.length;
			byte[] cacheBytes;
			
			while (sourceLength > offset) {
				if (sourceLength - offset >= MAX_ENCRYPT_BLOCK) {
					tempLength = MAX_ENCRYPT_BLOCK;
				} else {
					tempLength = sourceLength - offset;
				}
				
				cacheBytes = cipher.doFinal(sourceBytes, offset, tempLength);
				baoStream.write(cacheBytes, 0, cacheBytes.length);
				offset += tempLength;
			}
			
			return baoStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != baoStream) {
				try {
					baoStream.close();
				} catch (IOException e) {
					//
				}
			}
		}
	}
	
	/**
	 * 使用私钥进行 RSA解密
	 *
	 * @param sourceBytes     原始数据，等待解密
	 * @param privateKeyBytes 私钥
	 * @param method          解密方式
	 * @return 解密后的数据
	 */
	private static byte[] decryptInner(byte[] sourceBytes, byte[] privateKeyBytes, String method) {
		if (null == sourceBytes || null == privateKeyBytes) {
			return null;
		}
		
		ByteArrayOutputStream baoStream = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			PrivateKey newPrivateKey = keyFactory.generatePrivate(encodedKeySpec);
			
			Cipher cipher = Cipher.getInstance(method);
			cipher.init(Cipher.DECRYPT_MODE, newPrivateKey);
			
			baoStream = new ByteArrayOutputStream();
			int tempLength, offset = 0, sourceLength = sourceBytes.length;
			byte[] cacheTemp;
			
			while (sourceLength > offset) {
				if (sourceLength - offset >= MAX_DECRYPT_BLOCK) {
					tempLength = MAX_DECRYPT_BLOCK;
				} else {
					tempLength = sourceLength - offset;
				}
				
				cacheTemp = cipher.doFinal(sourceBytes, offset, tempLength);
				baoStream.write(cacheTemp, 0, cacheTemp.length);
				offset += tempLength;
			}
			
			return baoStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != baoStream) {
				try {
					baoStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
