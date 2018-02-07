package com.sqlite.green.common;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试基类
 *
 * @author yline 2017/9/13 -- 17:22
 * @version 1.0.0
 */
public abstract class AbstractSQLiteTest<Key, Model> {
    public String TAG = "xxx-";

    protected AbstractDao<Key, Model> mDao;

    public AbstractSQLiteTest() {
        TAG += getClass().getSimpleName();
    }

    @Test
    public void testInsertAndLoad() throws Exception {
        Key key = createRandomPK();
        Model model = createModel(key);

        mDao.insert(model);
        Assert.assertEquals(key, mDao.getKey(model));

        Model loadModel = mDao.load(key);
        assertModel(model, loadModel);
    }

    @Test
    public void testCount() throws Exception {
        long countA = mDao.count();

        mDao.insert(createModel(createRandomPK()));

        long countB = mDao.count();
        Assert.assertEquals(countA + 1, countB);
    }

    @Test
    public void testDeleteAll() throws Exception {
        mDao.insert(createModel(createRandomPK()));

        mDao.deleteAll();
        Assert.assertEquals(0, mDao.count());
    }

    @Test
    public void testInsertInTx() throws Exception {
        mDao.deleteAll();

        /* 如果插入的数据的Key重复，则会导致执行失败；不会抛异常 */
        List<Model> modelList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            modelList.add(createModel(createRandomPK()));
        }

        long teaTime = System.currentTimeMillis();
        mDao.insertInTx(modelList);

        Log.i(TAG, "testInsertInTx: teaTime = " + (System.currentTimeMillis() - teaTime));
        Assert.assertEquals(modelList.size(), mDao.count());
    }

    @Test
    public void testInsertTwice() throws Exception {
        mDao.deleteAll();

        Key key = createRandomPK();
        Model model = createModel(key);

        long rowId = mDao.insert(model);
        Assert.assertNotEquals(rowId, -1);

        long newRowId = mDao.insert(model);
        Assert.assertEquals(newRowId, -1);

        Log.i(TAG, "testInsertTwice: rowId = " + rowId + ", newRowId = " + newRowId);
    }

    @Test
    public void testInsertOrReplaceTwice() throws Exception {
        Model model = createModel(createRandomPK());

        long rowIdA = mDao.insert(model);
        long countA = mDao.count();

        long rowIdB = mDao.insertOrReplace(model);
        long countB = mDao.count();

        Assert.assertEquals(countA, countB); // 确定数据没有增加
        Assert.assertNotEquals(rowIdB, -1); // 确定替换成功了

        Log.e(TAG, "testInsertOrReplaceTwice: rowIdA = " + rowIdA + ", rowIdB = " + rowIdB + ", result = " + (rowIdA == rowIdB));
        Log.e(TAG, "testInsertOrReplaceTwice: if Key is String, the rowId will be different");
    }

    @Test
    public void testInsertOrReplaceInTx() throws Exception {
        mDao.deleteAll();

        List<Model> listPartial = new ArrayList<>();
        List<Model> listAll = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Model model = createModel(createRandomPK());
            if (i % 2 == 0) {
                listPartial.add(model);
            }
            listAll.add(model);
        }

        long teaTime = System.currentTimeMillis();
        mDao.insertOrReplaceInTx(listPartial);
        Assert.assertEquals(mDao.count(), listPartial.size());

        mDao.insertOrReplaceInTx(listAll);

        Log.i(TAG, "testInsertOrReplaceInTx: teaTime = " + (System.currentTimeMillis() - teaTime));
        Assert.assertEquals(listAll.size(), mDao.count());
    }

    @Test
    public void testLoadAll() throws Exception {
        mDao.deleteAll();

        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 1090; i++)
        {
            Model model = createModel(createRandomPK());
            list.add(model);
        }

        mDao.insertInTx(list);

        List<Model> loadList = mDao.loadAll();
        Assert.assertEquals(list.size(), loadList.size());
    }

    @Test
    public void testRowId() throws Exception {
        Model modelA = createModel(createRandomPK());
        Model modelB = createModel(createRandomPK());

        long rowIdA = mDao.insert(modelA);
        long countA = mDao.count();

        long rowIdB = mDao.insert(modelB);
        long countB = mDao.count();

        Assert.assertEquals(countA + 1, countB); // 确定数据没有增加
        Assert.assertNotEquals(rowIdB, -1); // 确定替换成功了

        Log.e(TAG, "testRowId: rowIdA = " + rowIdA + ", rowIdB = " + rowIdB + ", result = " + (rowIdA == rowIdB));
        Log.e(TAG, "testRowId: if Key is String, the rowId will be different");
    }

    protected abstract void assertModel(Model tom, Model joe);

    protected abstract Key createRandomPK();

    protected abstract Model createModel(Key pk);
}
