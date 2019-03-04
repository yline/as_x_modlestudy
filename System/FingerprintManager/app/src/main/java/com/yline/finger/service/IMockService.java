package com.yline.finger.service;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 与后台的接口
 *
 * @author yline 2019/3/1 -- 15:35
 */
public interface IMockService {
    /**
     * 使用指纹校验，理论上，私钥要分开的。这里就省了
     *
     * @param goodsInfo  商品信息
     * @param encryptStr 商品加密信息
     * @param vectorStr  偏移量
     */
    boolean verifyByFingerWithDecrypt(String goodsInfo, String encryptStr, String vectorStr);

    /**
     * 使用指纹 校验
     *
     * @param goodsInfo      商品信息
     * @param userId         用户ID
     * @param signatureValue 指纹对商品信息的签名后，的信息
     */
    boolean verifyByFinger(String goodsInfo, String userId, String signatureValue);

    /**
     * 使用支付密码 校验
     *
     * @param goodsInfo 商品信息
     * @param userId    用户ID
     * @param payPwd    支付密码
     */
    boolean verifyByPwd(String goodsInfo, String userId, String payPwd);

    /**
     * 发送公钥到服务器
     *
     * @param userId    用户Id
     * @param payPwd    用户支付密码
     * @param publicKey 公钥
     */
    boolean enroll(String userId, String payPwd, PublicKey publicKey);
}
