package com.sqlite.green.async;

/**
 * 异步操作，监听器返回的数据集合
 *
 * @author yline 2017/9/14 -- 17:26
 * @version 1.0.0
 */
public class AsyncOperationModel {
    public static final int DefaultRowId = -1024;

    private long rowId;

    private AsyncOperationType type;

    public AsyncOperationModel(AsyncOperationType type) {
        this(DefaultRowId, type);
    }

    public AsyncOperationModel(long rowId, AsyncOperationType type) {
        this.rowId = rowId;
        this.type = type;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public AsyncOperationType getType() {
        return type;
    }

    public void setType(AsyncOperationType type) {
        this.type = type;
    }
}
