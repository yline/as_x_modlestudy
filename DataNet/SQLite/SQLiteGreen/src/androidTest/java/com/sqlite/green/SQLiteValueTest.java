package com.sqlite.green;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sqlite.green.common.AbstractSQLiteTest;
import com.sqlite.green.gen.DaoManager;
import com.sqlite.green.test.SimpleModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Random;

// @RunWith(AndroidJUnit4.class)
public class SQLiteValueTest extends AbstractSQLiteTest<String, SimpleModel> {
    public static final String TAG = "xxx-SQLiteValueTest";

    protected Random mRandom;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        mDao = DaoManager.getValueModelDao();
        mRandom = new Random();
    }

    @Override
    protected void assertModel(SimpleModel tom, SimpleModel joe) {
        Assert.assertEquals(tom.getKey(), joe.getKey());
        Assert.assertEquals(tom.getValue(), joe.getValue());
    }

    @Override
    protected String createRandomPK() {
        return mRandom.nextInt() + "";
    }

    @Override
    protected SimpleModel createModel(String pk) {
        String value = pk + "-value";
        return new SimpleModel(pk, value);
    }

    @After
    public void tearDown() throws Exception {
        mDao = null;
    }
}
