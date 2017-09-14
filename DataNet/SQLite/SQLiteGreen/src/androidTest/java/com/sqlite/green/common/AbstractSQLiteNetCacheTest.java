package com.sqlite.green.common;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sqlite.green.DaoManager;
import com.sqlite.green.test.NetCacheModel;

import org.junit.Assert;
import org.junit.Before;

import java.util.Random;

/**
 * 数据库在 NetCache基础上测试
 *
 * @author yline 2017/9/14 -- 14:20
 * @version 1.0.0
 */
public abstract class AbstractSQLiteNetCacheTest extends AbstractSQLiteTest<String, NetCacheModel>{
    protected Random mRandom;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoManager.init(appContext);

        mDao = DaoManager.getNetCacheDao();
        mRandom = new Random();
    }

    @Override
    protected void assertModel(NetCacheModel tom, NetCacheModel joe) {
        Assert.assertEquals(tom.getRequestUrl(), joe.getRequestUrl());
        Assert.assertEquals(tom.getRequestTag(), joe.getRequestTag());

        assertObject(tom.getResultHeader(), joe.getResultHeader());
        assertObject(tom.getResultData(), joe.getResultData());
    }

    protected abstract void assertObject(Object tom, Object joe);

    @Override
    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }
}
