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

    public String getRSAPublicKey() {
        return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdFkhUJBKI9JDQzSTw5h387ujbu/e6M6fRl" +
                "1WMY7j/yUWtEpXVsmTrjbhGRr7SO4ddvIqK+Up4/pBqP2HmPJL4A0THkK9dfIn6aOTI5TqdSnJ" +
                "N8SSqBbaV6/ve3PMLph9cwhe0IavInqIFdJr4cvC9oYRoWAs52Ay/k78DBGtoJwIDAQAB";
    }

    /**
     * 获取 RSA 的公钥
     *
     * @return RSA公钥
     */
    public native String getRSAEncodePublicKey();

    /**
     * 获取 RSA 的公钥，提供给安全键盘使用
     *
     * @return RSA公钥
     */
    public native String getRSABoardPublicKey();
}
