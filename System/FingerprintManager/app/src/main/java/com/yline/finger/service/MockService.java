package com.yline.finger.service;

import android.util.Base64;

import com.yline.utils.LogUtil;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟服务器
 * 默认密码默认，都通过
 *
 * @author yline 2019/2/28 -- 9:54
 */
public class MockService implements IMockService {
    private final Map<String, PublicKey> mPublicKeyMap = new HashMap<>();
    private final List<String> mSoldList = new ArrayList<>(); // 已销售的商品

    @Override
    public boolean verifyByFinger(String goodsInfo, String userId, String signatureValue) {
        LogUtil.v("goodsInfo = " + goodsInfo + ", userId = " + userId + ", signatureValue = " + signatureValue);

        PublicKey publicKey = mPublicKeyMap.get(userId);
        if (null == publicKey) {
            LogUtil.v("指纹方式，销售失败，还未开通指纹支付");
            return false;
        } else {
            try {
                // 校验签名方式
                Signature verifySignature = Signature.getInstance("SHA256withECDSA");
                verifySignature.initVerify(publicKey); // 使用公钥初始化
                verifySignature.update(goodsInfo.getBytes()); // 校验商品信息

                // 校验签名成功
                byte[] signBytes = Base64.decode(signatureValue, Base64.NO_WRAP);
                if (verifySignature.verify(signBytes)) {
                    mSoldList.add(goodsInfo);
                    LogUtil.v("指纹方式，销售成功，总量 = " + mSoldList.size());
                    return true;
                } else {
                    LogUtil.v("指纹方式，销售失败，签名校验失败");
                    return false;
                }
            } catch (NoSuchAlgorithmException e) {
                LogUtil.e("NoSuchAlgorithmException", e);
            } catch (InvalidKeyException e) {
                LogUtil.e("InvalidKeyException", e);
            } catch (SignatureException e) {
                LogUtil.e("SignatureException", e);
            }
            return false;
        }
    }

    @Override
    public boolean verifyByPwd(String goodsInfo, String userId, String payPwd) {
        LogUtil.v("goodsInfo = " + goodsInfo + ", userId = " + userId + ", payPwd = " + payPwd);
        mSoldList.add(goodsInfo);
        LogUtil.v("密码方式，销售成功，总量 = " + mSoldList.size());
        return true;
    }

    @Override
    public boolean enroll(String userId, String payPwd, PublicKey publicKey) {
        LogUtil.v("userId = " + userId + ", payPwd = " + payPwd + ", publicKey = " + publicKey);
        if (null != publicKey && null != userId) {
            LogUtil.v(mPublicKeyMap.containsKey(userId) ? "初次上传公钥" : "覆盖上传公钥");
            mPublicKeyMap.put(userId, publicKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean enrollAndVerify(String userId, String payPwd, PublicKey publicKey, String goodsInfo) {
        LogUtil.v("userId = " + userId + ", payPwd = " + payPwd + ", publicKey = " + publicKey);
        if (null != publicKey && null != userId) {
            LogUtil.v(mPublicKeyMap.containsKey(userId) ? "初次上传公钥" : "覆盖上传公钥");
            mPublicKeyMap.put(userId, publicKey);

            mSoldList.add(goodsInfo);
            LogUtil.v("密码方式，销售成功，总量 = " + mSoldList.size());
            return true;
        }
        return false;
    }
}
