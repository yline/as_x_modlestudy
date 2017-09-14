package com.sqlite.green.test;

import java.io.Serializable;

/**
 * 一个 简易 Model
 * @author yline 2017/9/14 -- 14:35
 * @version 1.0.0
 */
public class SimpleModel implements Serializable {
    private String Key;

    private String Value;

    public SimpleModel(String key, String value) {
        Key = key;
        Value = value;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
