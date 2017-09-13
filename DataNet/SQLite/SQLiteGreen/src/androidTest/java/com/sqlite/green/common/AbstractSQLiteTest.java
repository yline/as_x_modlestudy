package com.sqlite.green.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试基类
 *
 * @author yline 2017/9/13 -- 17:22
 * @version 1.0.0
 */
public abstract class AbstractSQLiteTest<Key, Model> {
    public static final String TAG = "xxx-AbstractSQLiteTest";

    protected AbstractDao<Key, Model> mDao;

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



    protected abstract void assertModel(Model tom, Model joe);

    protected abstract Key createRandomPK();

    protected abstract Model createModel(Key pk);
}
