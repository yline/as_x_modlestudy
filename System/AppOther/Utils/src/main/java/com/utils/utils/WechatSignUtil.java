package com.utils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.yline.utils.LogUtil;
import com.yline.utils.crypt.MD5Utils;

/**
 * 微信签名工具
 *
 * @author yline 2019/3/6 -- 16:04
 */
public class WechatSignUtil {
    /**
     * 对包名进行签名
     *
     * @param packageName 包名
     * @return 签名结果
     */
    public static String sign(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            LogUtil.e("packageName is null");
            return null;
        }

        Signature[] signArray = getRawSignature(context, packageName);
        if (null != signArray) {
            byte[] signBytes = signArray[0].toByteArray();

            String md5 = MD5Utils.encrypt(signBytes, true);
            LogUtil.v("md5 = " + md5);
            return md5;
        }

        LogUtil.e("signArray is null");
        return null;
    }

    @SuppressLint("PackageManagerGetSignatures")
    private static Signature[] getRawSignature(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return info.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
