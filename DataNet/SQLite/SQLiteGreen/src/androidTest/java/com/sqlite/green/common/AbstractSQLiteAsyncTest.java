package com.sqlite.green.common;

import android.util.Log;

import com.sqlite.green.SQLiteManager;
import com.sqlite.green.async.AsyncOperation;
import com.sqlite.green.async.AsyncOperationListener;
import com.sqlite.green.async.AsyncOperationModel;
import com.sqlite.green.async.AsyncOperationType;
import com.sqlite.green.gen.DaoManager;
import com.sqlite.green.test.NetCacheModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 异步数据库，操作
 *
 * @author yline 2017/9/14 -- 19:38
 * @version 1.0.0
 */
public abstract class AbstractSQLiteAsyncTest<Key extends String, Model extends NetCacheModel> {
    private String TAG = "xxx-";

    protected Random mRandom;

    private AsyncOperation operation;

    public AbstractSQLiteAsyncTest() {
        this.TAG += getClass().getSimpleName();
    }

    @Before
    public void setUp() throws Exception {
        mRandom = new Random();

        Log.i(TAG, "setUp: ");
        operation = DaoManager.getNetCacheModelDaoAsync();
    }

    @Test
    public void testInsertAndLoad() throws Exception {
        final Key key = createRandomPK();
        final Model model = createModel(key);

        SQLiteManager.insertAsync(model, new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.Insert) {
                    Log.i(TAG, "testInsertAndLoad: ");

                    Assert.assertEquals(key, SQLiteManager.getKey(model));

                    Model loadModel = (Model) SQLiteManager.load(key);
                    assertModel(model, loadModel);
                }
            }
        });
    }

    @Test
    public void testCount() throws Exception {
        final long countA = SQLiteManager.count();

        SQLiteManager.insertAsync(createModel(createRandomPK()), new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.Insert) {
                    long countB = SQLiteManager.count();
                    Log.i(TAG, "testCount: rowId = " + operationModel.getRowId() + ", countA = " + countA + ", countB = " + countB);

                    Assert.assertEquals(countA + 1, countB);
                }
            }
        });
    }

    @Test
    public void testDeleteAll() throws Exception {
        SQLiteManager.insert(createModel(createRandomPK()));

        SQLiteManager.deleteAllAsync(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.DeleteAll) {
                    Log.i(TAG, "testDeleteAll: ");

                    Assert.assertEquals(0, SQLiteManager.count());
                }
            }
        });
    }

    @Test
    public void testInsertInTx() throws Exception {
        SQLiteManager.deleteAllAsync(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                long count = SQLiteManager.count();
                Log.i(TAG, "testInsertInTx: count = " + count);
                Assert.assertEquals(0, count);
            }
        });

        // 如果插入的数据的Key重复，则会导致执行失败；不会抛异常
        final List<Model> modelList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            modelList.add(createModel(createRandomPK()));
        }

        final long teaTime = System.currentTimeMillis();

        SQLiteManager.insertInTxAsync(new ArrayList<NetCacheModel>(modelList), new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.InsertInTxIterable) {
                    Log.i(TAG, "testInsertInTx: teaTime = " + (System.currentTimeMillis() - teaTime));

                    Assert.assertEquals(modelList.size(), SQLiteManager.count());
                }
            }
        });
    }


    @Test
    public void testInsertOrReplaceInTx() throws Exception {
        SQLiteManager.deleteAll();

        final List<Model> listPartial = new ArrayList<>();
        final List<Model> listAll = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Model model = createModel(createRandomPK());
            if (i % 2 == 0) {
                listPartial.add(model);
            }
            listAll.add(model);
        }

        final long teaTime = System.currentTimeMillis();
        SQLiteManager.insertOrReplaceInTxAsync(new ArrayList<NetCacheModel>(listPartial), new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.InsertOrReplaceInTxIterable) {
                    Log.i(TAG, "testInsertOrReplaceInTx: SQLiteManager.count() = " + SQLiteManager.count() + ", listPartial.size() = " + listPartial.size());
                   // Assert.assertEquals(listPartial.size(), SQLiteManager.count());

                    Log.i(TAG, "testInsertOrReplaceInTx: first teaTime = " + (System.currentTimeMillis() - teaTime));
                    final long secondTeaTime = System.currentTimeMillis();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 第二次异步
                    SQLiteManager.insertOrReplaceInTxAsync(new ArrayList<NetCacheModel>(listAll), new AsyncOperationListener() {

                        @Override
                        public void onAsyncCompleted(AsyncOperationModel operationModel) {
                            if (operationModel.getType() == AsyncOperationType.InsertOrReplaceInTxIterable) {
                                Log.i(TAG, "testInsertOrReplaceInTx: SQLiteManager.count() = " + SQLiteManager.count() + ", listPartial.size() = " + listAll.size());
                                Log.i(TAG, "testInsertOrReplaceInTx: second teaTime = " + (System.currentTimeMillis() - secondTeaTime));

                                Assert.assertEquals(listAll.size(), SQLiteManager.count());
                            }
                        }
                    });
                }
            }
        });
    }

    protected abstract void assertModel(Model tom, Model joe);

    protected abstract Key createRandomPK();

    protected abstract Model createModel(Key pk);
}
