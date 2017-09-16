package com.sqlite.green;

import com.sqlite.green.async.AsyncOperation;
import com.sqlite.green.async.AsyncOperationListener;
import com.sqlite.green.gen.DaoManager;
import com.sqlite.green.test.NetCacheModel;

import java.util.List;

/**
 * 统一提供调用方法
 *
 * @author yline 2017/9/16 -- 11:20
 * @version 1.0.0
 */
public class SQLiteManager {
    public static NetCacheModel load(String httpUrl) {
        return DaoManager.getNetCacheModelDao().load(httpUrl);
    }

    public static List<NetCacheModel> loadAll() {
        return DaoManager.getNetCacheModelDao().loadAll();
    }

    public static long insert(NetCacheModel model) {
        return DaoManager.getNetCacheModelDao().insert(model);
    }

    public static void insertAsync(NetCacheModel model, AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.insert(model);
        operation.setOnOperationListener(operationListener);
    }

    public static void insertInTx(Iterable<NetCacheModel> models) {
        DaoManager.getNetCacheModelDao().insertInTx(models);
    }

    public static void insertInTxAsync(Iterable<NetCacheModel> models, AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.insertInTx(models);
        operation.setOnOperationListener(operationListener);
    }

    public static void insertInTx(Iterable<NetCacheModel> models, boolean cache) {
        DaoManager.getNetCacheModelDao().insertInTx(models, cache);
    }

    public static void insertInTxAsync(Iterable<NetCacheModel> models, boolean cache, AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.insertInTx(models, cache);
        operation.setOnOperationListener(operationListener);
    }

    public static long insertOrReplace(NetCacheModel model) {
        return DaoManager.getNetCacheModelDao().insertOrReplace(model);
    }

    public static void insertOrReplaceAsync(NetCacheModel model, AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.insertOrReplace(model);
        operation.setOnOperationListener(operationListener);
    }

    public static void insertOrReplaceInTx(Iterable<NetCacheModel> models) {
        DaoManager.getNetCacheModelDao().insertOrReplaceInTx(models);
    }

    public static void insertOrReplaceInTxAsync(Iterable<NetCacheModel> models, AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.insertOrReplaceInTx(models);
        operation.setOnOperationListener(operationListener);
    }

    public static void insertOrReplaceInTx(List<NetCacheModel> models, boolean cache) {
        DaoManager.getNetCacheModelDao().insertOrReplaceInTx(models, cache);
    }

    public static void insertOrReplaceInTxAsync(Iterable<NetCacheModel> models, boolean cache, AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.insertOrReplaceInTx(models, cache);
        operation.setOnOperationListener(operationListener);
    }

    public static long count() {
        return DaoManager.getNetCacheModelDao().count();
    }

    public static void deleteAll(){
        DaoManager.getNetCacheModelDao().deleteAll();
    }

    public static void deleteAllAsync(AsyncOperationListener operationListener) {
        AsyncOperation operation = DaoManager.getNetCacheModelDaoAsync();
        operation.deleteAll();
        operation.setOnOperationListener(operationListener);
    }

    public static String getKey(NetCacheModel model) {
        return DaoManager.getNetCacheModelDao().getKey(model);
    }
}
