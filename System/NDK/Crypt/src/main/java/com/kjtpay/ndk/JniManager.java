package com.kjtpay.ndk;

/**
 * 加密直接Java实现，C不会
 * 但是调用封装还是用C
 *
 * @author yline 2018/10/25 -- 16:24
 */
public class JniManager {
	static {
		System.loadLibrary("native-manager");
	}
	
	private JniManager() {
	}
	
	private static JniManager sInstance;
	
	public static JniManager getInstance() {
		if (null == sInstance) {
			synchronized (JniManager.class) {
				if (null == sInstance) {
					sInstance = new JniManager();
				}
			}
		}
		return sInstance;
	}
	
	/**
	 * 获取 RSA 的公钥
	 *
	 * @return RSA公钥
	 */
	public native String getRSAPublicKey();
	
	/**
	 * 获取 RSA 的公钥，提供给安全键盘使用
	 *
	 * @return RSA公钥
	 */
	public native String getRSABoardPublicKey();
	
	/**
	 * 获取 RSA 的公钥
	 *
	 * @return RSA公钥(测试使用)
	 */
	public native String getRSAPublicKeyTest();
}
