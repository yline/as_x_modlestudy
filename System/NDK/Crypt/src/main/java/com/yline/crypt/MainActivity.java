package com.yline.crypt;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kjtpay.ndk.AESUtils;
import com.kjtpay.ndk.JniManager;
import com.kjtpay.ndk.KjtEncryptUtils;
import com.kjtpay.ndk.RSAUtils;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseTestActivity {
	private static final int AES_KEY_LENGTH = 16 * 8;
	// 私钥 - 自己生成
	private static final String RSA_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANCSIUCnkYe5bySgcDDYfnGXshlTS3IwTzFSl4JqA08Hwwll4q5eerDL8nTpKnoKVtHRCpRXIJPw0IShElY6TLfANtNH1Pf+tw+ldJlgUL7MiQs4xnP9oo0pd96zDhWal/k4PYhBAZk64pc0mexpC8Q/OGhznk1BR/WkrRSaY3+7AgMBAAECgYEAjScoa/o76m/bwRz3cIdD45p3RN5zQ99f6RBtSyx1+slU/IpAhCOawwXzm52lSpyurybbExN4D8c9R1U+9K5V9hd1/hpVLi9X8kh9Jw6VJXExotJ99LY6PYBAs4TqTwfE7oPP/Y+79u2wI240QIJkSwTEtIV4LyKdHQzRmLllSeECQQD9qgtyilj3f4HFC47xswdYrXKC3d/CWjTuD/YqM94LrEWYHeVWlFTnXX3Af+/YjjXQq2Go1Wbww1aHhrYR5MirAkEA0n3JrCpNLhKQfR64C74gJBEi5+Zm4AeOkkhyeRuT+53six3nFUqgRLnpUeM980V/X2a79usQ0GMCETorYIlFMQJAOHK2yW5wDeOaBTdlP/QPFnTCnsyxFpbsYG284fdY2lAjzI4akwG/Qx1S9puBzDcZUq5QtTmIBtvxTYd0zNaUsQJBAM7qqF1+F/C6fx8AG5wvghjyX4XnkCmaRCS44w76dTZbwDPhaVAc0/+7YgkFgdiq8NMvgobv/M9dBKM6s3lqd4ECQQCGfie3P1D40ZPGTgJImm/ly4leWloV7NjxMEBem1xIGGEHht+9bV4qexbTReAIHyYeMK2WGtzCq2oy6rYnJ/Cj";
	
	private byte[] AESKey;
	private String rsaPrivateKey;
	private String rsaPublicKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		KeyPair keyPair = RSAUtils.createKeyPair();
		//		rsaPrivateKey = RSAUtils.getPrivateKey(keyPair);
		//		rsaPublicKey = RSAUtils.getPublicKey(keyPair);
		
		rsaPublicKey = JniManager.getInstance().getRSAPublicKeyTest();
		rsaPrivateKey = RSA_PRIVATE_KEY;
		
		AESKey = AESUtils.createAESKey(AES_KEY_LENGTH);
		LogUtil.v("RSA: privateKey = " + rsaPrivateKey);
		LogUtil.v("RSA: publicKey = " + rsaPublicKey);
		LogUtil.v("AESKey:" + Arrays.toString(AESKey) + ", length = " + AESKey.length);
	}
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		final EditText rsaEditText = addEditText("RSA加密内容", "yline&杭州");
		final TextView rsaJavaText = addTextView("");
		addButton("RSA 加密", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String sourceContent = rsaEditText.getText().toString().trim();
				
				byte[] rsaPublicKeyByte = Base64.decode(rsaPublicKey, Base64.NO_WRAP);
				byte[] rsaEncrypt = RSAUtils.encrypt(sourceContent.getBytes(), rsaPublicKeyByte);
				
				byte[] rsaPrivateKeyByte = Base64.decode(rsaPrivateKey, Base64.NO_WRAP);
				byte[] rsaDecrypt = RSAUtils.decrypt(rsaEncrypt, rsaPrivateKeyByte);
				
				String rsaSign = RSAUtils.sign(sourceContent, rsaPrivateKey);
				boolean rsaVerifySign = RSAUtils.verifySign(sourceContent, rsaPublicKey, rsaSign);
				
				String format = "原文：%s\nJava加密后：%s\nJava解密后：%s\nJava签名：%s\nJava签名验证：%s\n";
				if (null == rsaDecrypt) {
					rsaDecrypt = "null".getBytes();
				}
				
				if (null == rsaEncrypt) {
					rsaEncrypt = "null".getBytes();
				}
				
				rsaJavaText.setText(String.format(format, sourceContent, Base64.encodeToString(rsaEncrypt, Base64.NO_WRAP), new String(rsaDecrypt), rsaSign, rsaVerifySign));
				LogUtil.v(String.format(format, sourceContent, Base64.encodeToString(rsaEncrypt, Base64.NO_WRAP), new String(rsaDecrypt), rsaSign, rsaVerifySign));
			}
		});
		
		// AES加密解密
		final EditText aesEditText = addEditText("AES加密内容", "yline&杭州");
		final TextView aesJavaText = addTextView("");
		addButton("AES", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String sourceContent = aesEditText.getText().toString().trim();
				
				byte[] aesEncrypt = AESUtils.encrypt(sourceContent.getBytes(), AESKey, "1234567890123456".getBytes());
				byte[] aesDecrypt = AESUtils.decrypt(aesEncrypt, AESKey, "1234567890123456".getBytes());
				
				String format = "原文：%s\nJava加密后：%s\nJava解密后：%s";
				aesJavaText.setText(String.format(format, sourceContent, Base64.encodeToString(aesEncrypt, Base64.NO_WRAP), new String(aesDecrypt)));
				LogUtil.v(String.format(format, sourceContent, Base64.encodeToString(aesEncrypt, Base64.NO_WRAP), new String(aesDecrypt)));
			}
		});
		
		// AES秘钥
		final TextView aesKeyText = addTextView("");
		addButton("随机生成 AES秘钥", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] key = AESUtils.createAESKey(AES_KEY_LENGTH);
				List<String> dataArray = new ArrayList<>();
				for (byte aKey : key) {
					dataArray.add(String.valueOf(aKey));
				}
				aesKeyText.setText(String.format(Locale.CHINA, "%s, length = %d", Arrays.toString(dataArray.toArray()), dataArray.size()));
				LogUtil.v(Arrays.toString(dataArray.toArray()));
			}
		});
		
		final EditText kjtEditText = addEditText("请求内容", "yline&杭州");
		final TextView kjtTextView = addTextView("");
		addButton("流程 - 加密", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sourceContent = kjtEditText.getText().toString().trim();
				
				String kjtEncrypt = KjtEncryptUtils.encryptRequest(sourceContent, rsaPublicKey);
				String kjtDecrypt = KjtEncryptUtils.decryptRequest(kjtEncrypt, rsaPrivateKey);
				
				String format = "原文：%s\n加密后：%s\n解密后：%s";
				kjtTextView.setText(String.format(format, sourceContent, kjtEncrypt, kjtDecrypt));
				LogUtil.v(String.format(format, sourceContent, kjtEncrypt, kjtDecrypt));
			}
		});
	}
}
