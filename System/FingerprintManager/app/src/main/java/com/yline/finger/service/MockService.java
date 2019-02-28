package com.yline.finger.service;

import android.text.TextUtils;

import com.yline.utils.LogUtil;

import java.util.HashSet;
import java.util.Objects;

/**
 * 模拟服务器
 *
 * @author yline 2019/2/28 -- 9:54
 */
public class MockService {
    // 模拟数据库表，单个数据
    private static final HashSet<SingleValue> hashSet = new HashSet<>();
    private static int value = 0;

    public static SingleValue getSingleValue() {
        SingleValue[] valueArray = new SingleValue[hashSet.size()];
        hashSet.toArray(valueArray);
        if (valueArray.length == 0) {
            return new SingleValue("null", "null");
        }

        value++;
        return valueArray[value % hashSet.size()];
    }

    /**
     * 服务器，新增指纹签名
     *
     * @param value 指纹签名
     */
    public static void fingerAdd(String encrypt, String iv, OnCallback callback) {
        SingleValue value = new SingleValue(encrypt, iv);
        if (hashSet.contains(value)) {
            if (null != callback) {
                callback.onFailure("value 已存在");
            }
        } else {
            boolean isSuccess = hashSet.add(value);
            if (null != callback) {
                if (isSuccess) {
                    callback.onResponse();
                } else {
                    callback.onFailure("value 添加失败");
                }
            }
        }
        hashSet.add(value);
    }

    /**
     * 服务器，指纹校验签名
     *
     * @param value
     */
    public static void fingerVerify(String encrypt, String iv, OnCallback callback) {
        SingleValue value = new SingleValue(encrypt, iv);
        if (hashSet.contains(value)) {
            if (null != callback) {
                callback.onResponse();
            }
        } else {
            if (null != callback) {
                callback.onFailure("value 校验失败");
            }
        }
    }

    /**
     * 服务器，密码校验成功之后，新增指纹
     *
     * @param value 指纹签名
     * @param pwd   密码
     */
    public static void fingerAndPwdVerify(String encrypt, String iv, String pwd, OnCallback callback) {
        SingleValue value = new SingleValue(encrypt, iv);

        if (TextUtils.isEmpty(pwd) || pwd.length() < 11) { // 密码基本都给过[虚拟]
            boolean isSuccess = hashSet.add(value);
            if (null != callback) {
                if (isSuccess) {
                    callback.onResponse();
                } else {
                    LogUtil.e("value 添加失败");
                    callback.onResponse();
                }
            }
        } else {
            if (null != callback) {
                callback.onFailure("密码 校验失败");
            }
        }
    }

    public static class SingleValue {
        private String encryptStr;
        private String initVector;

        public SingleValue(String encryptStr, String initVector) {
            this.encryptStr = encryptStr;
            this.initVector = initVector;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SingleValue that = (SingleValue) o;
            return Objects.equals(encryptStr, that.encryptStr) &&
                    Objects.equals(initVector, that.initVector);
        }

        @Override
        public int hashCode() {
            return Objects.hash(encryptStr, initVector);
        }

        public String getEncryptStr() {
            return encryptStr;
        }

        public String getInitVector() {
            return initVector;
        }
    }

    public abstract static class OnCallback {
        void onFailure(String msg) {
            LogUtil.v("msg = " + msg);
        }

        public abstract void onResponse();
    }
}
