package com.sqlite.green;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sqlite.green.common.AbstractSQLiteTest;
import com.sqlite.green.test.Msg;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Random;

public class SQLiteMsgTest extends AbstractSQLiteTest<String, Msg> {
    public static final String TAG = "xxx-SQLiteMsgTest";

    private Random mRandom;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoManager.init(appContext);

        mDao = DaoManager.getMsgDao();
        mRandom = new Random();
    }

    @Override
    protected void assertModel(Msg tom, Msg joe) {
        Assert.assertEquals(tom.getUserId(), joe.getUserId());
        Assert.assertEquals(tom.getUserName(), joe.getUserName());
        Assert.assertEquals(tom.getUserNickName(), joe.getUserNickName());
        Assert.assertEquals(tom.getPhoneTag(), joe.getPhoneTag());
        Assert.assertEquals(tom.getPhoneNumber(), joe.getPhoneNumber());
    }

    @Override
    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }

    @Override
    protected Msg createModel(String pk) {
        String userId = pk;
        String userName = pk + "-name";
        String userNickName = pk + "-nickname";
        String phoneNumber = pk + "-number";
        String phoneTag = pk + "-tag";
        return new Msg(userId, userName, userNickName, phoneNumber, phoneTag);
    }

    @After
    public void tearDown() throws Exception {
        mDao = null;
    }
}
