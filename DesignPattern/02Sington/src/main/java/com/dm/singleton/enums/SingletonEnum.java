package com.dm.singleton.enums;

import com.yline.utils.LogUtil;

/**
 * 即使反序列化,也不会重新生成对象
 */
public enum SingletonEnum {
    INSTANCE;

    public void doSome() {
        LogUtil.v("SingletonEnum -> doSome");
    }
}
