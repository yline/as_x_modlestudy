package com.sqlite.green.async;

/**
 * 异步数据库操作时，监听事件
 * @author yline 2017/9/14 -- 17:36
 * @version 1.0.0
 */
public interface AsyncOperationListener {
    void onAsyncCompleted(AsyncOperationModel operationModel);
}
