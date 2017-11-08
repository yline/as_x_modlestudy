package com.sqlite.green.rebuild.sqlite;

import com.sqlite.green.SQLiteIOUtils;
import com.sqlite.green.common.AbstractSQLiteAsyncTest;
import com.sqlite.green.test.NetCacheModel;
import com.sqlite.green.test.SimpleModel;

import org.junit.Assert;

public class SQLiteAsyncSimpleTest extends AbstractSQLiteAsyncTest<String, NetCacheModel> {

    @Override
    protected void assertModel(NetCacheModel tom, NetCacheModel joe) {
        Assert.assertEquals(tom.getRequestUrl(), joe.getRequestUrl());
        Assert.assertEquals(tom.getRequestTag(), joe.getRequestTag());

        assertModel(tom.getResultHeader(), joe.getResultHeader());
        assertModel(tom.getResultData(), joe.getResultData());
    }

    private void assertModel(Object tom, Object joe){
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Assert.assertTrue(tom instanceof SimpleModel); // 断言一波
        Assert.assertTrue(joe instanceof SimpleModel); // 断言一波

        Assert.assertEquals(((SimpleModel) tom).getKey(), ((SimpleModel) joe).getKey());
        Assert.assertEquals(((SimpleModel) tom).getValue(), ((SimpleModel) joe).getValue());
    }

    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }

    protected NetCacheModel createModel(String pk) {
        byte[] modelByte = SQLiteIOUtils.objectToByte(new SimpleModel(pk, pk + "-value"));
        return new NetCacheModel(pk, modelByte);
    }
}
