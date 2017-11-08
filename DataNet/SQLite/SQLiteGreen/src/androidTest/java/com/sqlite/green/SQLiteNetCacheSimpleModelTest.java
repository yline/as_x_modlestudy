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
    protected void assertObject(byte[] tom, byte[] joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Object tomObject = SQLiteIOUtils.byteToObject(tom);
        Object joeObject = SQLiteIOUtils.byteToObject(tom);

        Assert.assertTrue(tomObject instanceof SimpleModel); // 断言一波
        Assert.assertTrue(joeObject instanceof SimpleModel); // 断言一波

        Assert.assertEquals(((SimpleModel) tomObject).getKey(), ((SimpleModel) joeObject).getKey());
        Assert.assertEquals(((SimpleModel) tomObject).getValue(), ((SimpleModel) joeObject).getValue());
    }

    @Override
    protected NetCacheModel createModel(String pk) {
        byte[] modelByte = SQLiteIOUtils.objectToByte(new SimpleModel(pk, pk + "-value"));
        return new NetCacheModel(pk, modelByte);
    }
}
