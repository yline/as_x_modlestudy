package com.sqlite.green.common;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.sqlite.green.DaoManager;
import com.sqlite.green.async.AsyncOperation;
import com.sqlite.green.async.AsyncOperationListener;
import com.sqlite.green.async.AsyncOperationModel;
import com.sqlite.green.async.AsyncOperationType;
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

    protected AsyncOperation<Key, Model> asyncOperation;

    public AbstractSQLiteAsyncTest() {
        this.TAG += getClass().getSimpleName();
    }

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoManager.init(appContext);

        asyncOperation = (AsyncOperation<Key, Model>) DaoManager.getNetCacheModelDaoAsync();
        mRandom = new Random();
    }

    @Test
    public void testInsertAndLoad() throws Exception {
        final Key key = createRandomPK();
        final Model model = createModel(key);

        asyncOperation.insert(model);
        asyncOperation.setOnOperationListener(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.Insert) {
                    Assert.assertEquals(key, asyncOperation.getExecuteDao().getKey(model));

                    Model loadModel = asyncOperation.getExecuteDao().load(key);
                    assertModel(model, loadModel);
                }
            }
        });
    }

    @Test
    public void testCount() throws Exception {
        final long countA = asyncOperation.getExecuteDao().count();

        asyncOperation.insert(createModel(createRandomPK()));
        asyncOperation.setOnOperationListener(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.Insert) {
                    long countB = asyncOperation.getExecuteDao().count();
                    Assert.assertEquals(countA + 1, countB);
                }
            }
        });
    }

    @Test
    public void testDeleteAll() throws Exception {
        asyncOperation.getExecuteDao().insert(createModel(createRandomPK()));

        asyncOperation.deleteAll();
        asyncOperation.setOnOperationListener(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.DeleteAll) {
                    Assert.assertEquals(0, asyncOperation.getExecuteDao().count());
                }
            }
        });
    }

    @Test
    public void testInsertInTx() throws Exception {
        asyncOperation.getExecuteDao().deleteAll();

        /* 如果插入的数据的Key重复，则会导致执行失败；不会抛异常 */
        final List<Model> modelList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            modelList.add(createModel(createRandomPK()));
        }

        final long teaTime = System.currentTimeMillis();
        asyncOperation.insertInTx(modelList);
        asyncOperation.setOnOperationListener(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.InsertInTxIterable) {
                    Log.i(TAG, "testInsertInTx: teaTime = " + (System.currentTimeMillis() - teaTime));

                    Assert.assertEquals(modelList.size(), asyncOperation.getExecuteDao().count());
                }
            }
        });
    }


    @Test
    public void testInsertOrReplaceInTx() throws Exception {
        asyncOperation.getExecuteDao().deleteAll();

        final List<Model> listPartial = new ArrayList<>();
        final List<Model> listAll = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Model model = createModel(createRandomPK());
            if (i % 2 == 0) {
                listPartial.add(model);
            }
            listAll.add(model);
        }

        final long teaTime = System.currentTimeMillis();
        asyncOperation.insertOrReplaceInTx(listPartial);
        asyncOperation.setOnOperationListener(new AsyncOperationListener() {
            @Override
            public void onAsyncCompleted(AsyncOperationModel operationModel) {
                if (operationModel.getType() == AsyncOperationType.InsertOrReplaceInTxIterable) {
                    Assert.assertEquals(asyncOperation.getExecuteDao().count(), listPartial.size());

                    Log.i(TAG, "testInsertOrReplaceInTx: first teaTime = " + (System.currentTimeMillis() - teaTime));
                    final long secondTeaTime = System.currentTimeMillis();
                    // 第二次异步
                    asyncOperation.insertOrReplaceInTx(listAll);
                    asyncOperation.setOnOperationListener(new AsyncOperationListener() {

                        @Override
                        public void onAsyncCompleted(AsyncOperationModel operationModel) {
                            Log.i(TAG, "testInsertOrReplaceInTx: second teaTime = " + (System.currentTimeMillis() - secondTeaTime));
                            Assert.assertEquals(asyncOperation.getExecuteDao().count(), listAll.size());
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
