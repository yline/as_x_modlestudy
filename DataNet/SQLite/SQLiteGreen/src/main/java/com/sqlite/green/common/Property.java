package com.sqlite.green.common;

/**
 * 数据表 单个列 属性
 *
 * @author yline 2017/9/8 -- 19:44
 * @version 1.0.0
 */
public class Property {
    public final int ordinal;
    public final Class<?> type;
    public final String nickName;
    public final boolean primaryKey;
    public final String columnName;

    /**
     * @param ordinal    第几个
     * @param type       数据类型
     * @param nickName   昵称
     * @param primaryKey 是否 是 primaryKey
     * @param columnName 栏目名称
     */
    public Property(int ordinal, Class<?> type, String nickName, boolean primaryKey, String columnName) {
        this.ordinal = ordinal;
        this.type = type;
        this.nickName = nickName;
        this.primaryKey = primaryKey;
        this.columnName = columnName;
    }
}
