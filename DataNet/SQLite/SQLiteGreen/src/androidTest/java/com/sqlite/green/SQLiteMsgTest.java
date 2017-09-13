package com.sqlite.green;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.sqlite.green.common.AbstractSQLiteTest;
import com.sqlite.green.test.Msg;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class SQLiteMsgTest extends AbstractSQLiteTest<String, Msg>{
    private static final String TAG = "xxx-SQLiteMsgTest";

    @Before
    public void setUp() throws Exception {
        Log.i(TAG, "setUp: ");

        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoManager.init(appContext);

        mDao = DaoManager.getMsgDao();
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
        int number = (int) (System.currentTimeMillis() % (1000 * 1000 * 1000));
        return String.valueOf(number);
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
        Log.i(TAG, "tearDown: ");

        mDao = null;
    }
}
