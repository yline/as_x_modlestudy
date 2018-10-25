package com.yline.crypt;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kjtpay.ndk.JniManager;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;
import com.yline.utils.crypt.AESUtils;
import com.yline.utils.crypt.RSAUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.KeyGenerator;

public class MainActivity extends BaseTestActivity {
	private String AESKey = "1234567887654321";
	private String rsaPrivateKey;
	private String rsaPublicKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		KeyPair keyPair = RSAUtils.createKeyPair();
		//		rsaPrivateKey = RSAUtils.getPrivateKey(keyPair);
		//		rsaPublicKey = RSAUtils.getPublicKey(keyPair);
		//
		//		AESKey = "1234567887654321";
		
		rsaPrivateKey = JniManager.getInstance().getRSAPrivateKey();
		rsaPublicKey = JniManager.getInstance().getRSAPublicKey();
		AESKey = JniManager.getInstance().getAESKey();
		LogUtil.v("RSA: privateKey = " + rsaPrivateKey);
		LogUtil.v("RSA: publicKey = " + rsaPublicKey);
		LogUtil.v("AESKey:" + AESKey);
	}
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		final EditText rsaEditText = addEditText("RSA加密内容", "yline&杭州");
		final TextView rsaJavaText = addTextView("");
		addButton("RSA 加密", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String sourceContent = rsaEditText.getText().toString().trim();
				
				String rsaEncrypt = RSAUtils.encrypt(sourceContent, rsaPublicKey);
				String rsaDecrypt = RSAUtils.decrypt(rsaEncrypt, rsaPrivateKey);
				
				String rsaSign = RSAUtils.sign(sourceContent, rsaPrivateKey);
				boolean rsaVerifySign = RSAUtils.verifySign(sourceContent, rsaPublicKey, rsaSign);
				
				String format = "原文：%s\nJava加密后：%s\nJava解密后：%s\nJava签名：%s\nJava签名验证：%s\n";
				rsaJavaText.setText(String.format(format, sourceContent, rsaEncrypt, rsaDecrypt, rsaSign, rsaVerifySign));
			}
		});
		
		final EditText aesEditText = addEditText("AES加密内容", "yline&杭州");
		final TextView aesJavaText = addTextView("");
		addButton("AES", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String sourceContent = aesEditText.getText().toString().trim();
				
				String aesEncrypt = AESUtils.encrypt(sourceContent, AESKey);
				String aesDecrypt = AESUtils.decrypt(aesEncrypt, AESKey);
				
				String format = "原文：%s\nJava加密后：%s\nJava解密后：%s";
				aesJavaText.setText(String.format(format, sourceContent, aesEncrypt, aesDecrypt));
			}
		});
		
		final TextView aesKeyText = addTextView("");
		addButton("AES秘钥", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] key = genAESKey(16 * 8);
				List<String> dataArray = new ArrayList<>();
				for (byte aKey : key) {
					dataArray.add(String.valueOf(aKey));
				}
				aesKeyText.setText(Arrays.toString(dataArray.toArray()));
			}
		});
	}
	
	public static byte[] genAESKey(int length) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(length);
			return generator.generateKey().getEncoded();
		} catch (NoSuchAlgorithmException e) {
			return "1234567887654321".getBytes();
		}
	}
}
