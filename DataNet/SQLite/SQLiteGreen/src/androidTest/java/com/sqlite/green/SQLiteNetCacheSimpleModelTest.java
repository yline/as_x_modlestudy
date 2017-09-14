package com.sqlite.green;

import com.sqlite.green.common.AbstractSQLiteNetCacheTest;
import com.sqlite.green.test.NetCacheModel;
import com.sqlite.green.test.SimpleModel;

import org.junit.Assert;

/**
 * NetCacheModel 装载 SimpleModel
 *
 * @author yline 2017/9/14 -- 14:26
 * @version 1.0.0
 */
public class SQLiteNetCacheSimpleModelTest extends AbstractSQLiteNetCacheTest {
    @Override
    protected void assertObject(Object tom, Object joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Assert.assertTrue(tom instanceof SimpleModel); // 断言一波
        Assert.assertTrue(joe instanceof SimpleModel); // 断言一波

        Assert.assertEquals(((SimpleModel) tom).getKey(), ((SimpleModel) joe).getKey());
        Assert.assertEquals(((SimpleModel) tom).getValue(), ((SimpleModel) joe).getValue());
    }

    @Override
    protected NetCacheModel createModel(String pk) {
        return new NetCacheModel(pk, new SimpleModel(pk, pk + "-value"));
    }
}
