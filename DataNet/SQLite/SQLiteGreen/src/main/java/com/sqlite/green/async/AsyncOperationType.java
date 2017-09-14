package com.sqlite.green.async;

/**
 * 枚举所有的操作
 * @author yline 2017/9/14 -- 17:33
 * @version 1.0.0
 */
public enum AsyncOperationType {
    Insert, InsertInTxIterable, InsertInTxIterableCache,
    InsertOrReplace, InsertOrReplaceInTxIterable, InsertOrReplaceInTxIterableCache,
    DeleteAll
}
