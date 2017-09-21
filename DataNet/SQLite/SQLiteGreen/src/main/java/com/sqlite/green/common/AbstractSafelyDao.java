package com.sqlite.green.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.sqlite.green.gen.DaoManager;

/**
 * IExecuteDao 的 日志 + 入口判断
 *
 * @author yline 2017/9/21 -- 10:00
 * @version 1.0.0
 */
public abstract class AbstractSafelyDao<Key, Model> extends AbstractDao<Key, Model> {

    public AbstractSafelyDao(SQLiteDatabase db, String tableName, Property[] allColumns, Property[] pkColumns) {
        super(db, tableName, allColumns, pkColumns);
    }

    @Override
    public Model load(Key key) {
        DaoManager.i("load", "key = " + key);

        if (null == key) {
            Log.e("load", "key is null");
            return null;
        }

        return super.load(key);
    }

    @Override
    public long insert(Model model) {
        DaoManager.i("insert", "model = " + model);

        if (null == model) {
            Log.e("insert", "model is null");
            return Error;
        }

        return super.insert(model);
    }

    @Override
    public void insertInTx(Iterable<Model> models) {
        DaoManager.i("insertInTx", "models = " + models);

        if (null == models) {
            Log.e("insertInTx", "models is null");
            return;
        }
        super.insertInTx(models);
    }

    @Override
    public void insertInTx(Iterable<Model> models, boolean cache) {
        DaoManager.i("insertInTx", "models = " + models + ", cache = " + cache);

        if (null == models){
            Log.e("insertInTx", "models is null(cache)");
            return;
        }
        super.insertInTx(models, cache);
    }

    @Override
    public long insertOrReplace(Model model) {
        DaoManager.i("insertOrReplace", "model = " + model);

        if (null == model){
            Log.e("insertOrReplace", "model is null");
            return Error;
        }
        return super.insertOrReplace(model);
    }

    @Override
    public void insertOrReplaceInTx(Iterable<Model> models) {
        DaoManager.i("insertOrReplaceInTx", "models = " + models);

        if (null == models){
            Log.e("insertOrReplaceInTx", "models is null");
            return;
        }
        super.insertOrReplaceInTx(models);
    }

    @Override
    public void insertOrReplaceInTx(Iterable<Model> models, boolean cache) {
        DaoManager.i("insertOrReplaceInTx", "models = " + models + ", cache = " + cache);

        if (null == models){
            Log.e("insertOrReplaceInTx", "models is null(cache)");
            return;
        }
        super.insertOrReplaceInTx(models, cache);
    }

    public abstract Key getKey(Model model);

    protected abstract Key readKey(Cursor cursor);

    protected abstract Model readModel(Cursor cursor);

    protected abstract boolean bindValues(SQLiteStatement stmt, Model model);
}
