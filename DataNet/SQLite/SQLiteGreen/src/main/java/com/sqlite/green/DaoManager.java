package com.sqlite.green;

import android.content.Context;

import com.sqlite.green.async.AsyncOperation;
import com.sqlite.green.common.IExecuteDao;
import com.sqlite.green.gen.DaoOpenHelper;
import com.sqlite.green.gen.DaoSession;
import com.sqlite.green.test.NetCacheModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据库 管理类
 *
 * @author yline 2017/9/8 -- 19:48
 * @version 1.0.0
 */
public class DaoManager {
    private ExecutorService mExecutorService;

    private DaoOpenHelper mDaoOpenHelper;

    private DaoSession mDaoSession;

    private DaoManager() {
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public static DaoManager getInstance() {
        return DaoManagerHolder.sInstance;
    }

    private static class DaoManagerHolder {
        private static final DaoManager sInstance = new DaoManager();
    }

    private DaoSession getDaoSession() {
        return mDaoSession;
    }

    private void setContext(Context context) {
        this.mDaoOpenHelper = new DaoOpenHelper(context, 1);
        this.mDaoSession = new DaoSession(mDaoOpenHelper.getWritableDatabase());
    }

    public ExecutorService getExecutorService() {
        return mExecutorService;
    }

    /**
     * 初始化 Dao 工具
     * 调用该工具前，一定要调用一次
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        DaoManager.getInstance().setContext(context);
    }

    public static IExecuteDao<String, NetCacheModel> getNetCacheModelDao() {
        return DaoManager.getInstance().getDaoSession().getNetCacheModelDao();
    }

    public static AsyncOperation<String, NetCacheModel> getNetCacheModelDaoAsync() {
        return DaoManager.getInstance().getDaoSession().getNetCacheModelDaoAsync();
    }

    public static IExecuteDao getValueModelDao() {
        return DaoManager.getInstance().getDaoSession().getValueModelDao();
    }
}
