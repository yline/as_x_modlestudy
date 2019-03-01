package com.yline.finger.service;

import com.yline.utils.LogUtil;

/**
 * 模拟接口请求返回，回调
 *
 * @author yline 2019/3/1 -- 15:43
 */
public abstract class OnJsonCallback<T> {
    public void onFailure(String msg) {
        LogUtil.v("msg = " + msg);
    }

    public abstract void onResponse(T t);
}
