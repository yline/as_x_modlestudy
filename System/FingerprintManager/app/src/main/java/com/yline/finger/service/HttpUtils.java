package com.yline.finger.service;

import com.yline.application.SDKManager;

import java.security.PublicKey;

/**
 * 模拟数据请求的封装
 *
 * @author yline 2019/3/1 -- 15:58
 */
public class HttpUtils {
    private static final String PAY_PWD = "123456"; // 支付密码
    private static final String GOODS_INFO = "彩虹"; // 商品
    private static final String USER_ID = "137****8709"; // 用户ID

    /**
     * 上传公钥【开通指纹支付】
     *
     * @param publicKey 公钥
     */
    public static void enroll(PublicKey publicKey, OnJsonCallback<String> callback) {
        IMockService service = new MockService();
        boolean result = service.enroll(USER_ID, PAY_PWD, publicKey);
        callback(callback, result);
    }

    /**
     * 使用密码，购买商品
     */
    public static void verifyByPwd(OnJsonCallback<String> callback) {
        IMockService service = new MockService();
        callback(callback, service.verifyByPwd(GOODS_INFO, USER_ID, PAY_PWD));
    }

    /**
     * 使用指纹，购买商品
     *
     * @param goodsInfo 商品信息
     * @param signValue 使用私钥对商品签名，后的信息
     */
    public static void verifyByFinger(String goodsInfo, String signValue, OnJsonCallback<String> callback) {
        IMockService service = new MockService();
        callback(callback, service.verifyByFinger(goodsInfo, USER_ID, signValue));
    }

    /**
     * 模拟回调[实际情况会给返回数据]
     *
     * @param result 是否成功
     */
    private static void callback(final OnJsonCallback<String> callback, final boolean result) {
        if (null != callback) {
            SDKManager.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (result) {
                        callback.onFailure("");
                    } else {
                        callback.onResponse("");
                    }
                }
            }, 1500);
        }
    }
}
