package com.sqlite.green;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.sqlite.green.common.AbstractSQLiteTest;
import com.sqlite.green.test.Value;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

// @RunWith(AndroidJUnit4.class)
public class SQLiteValueTest extends AbstractSQLiteTest<String, Value> {
    public static final String TAG = "xxx-SQLiteValueTest";

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoManager.init(appContext);

        mDao = DaoManager.getValueDao();
    }

    @Override
    protected void assertModel(Value tom, Value joe) {
        Assert.assertEquals(tom.getKey(), joe.getKey());
        Assert.assertEquals(tom.getValue(), joe.getValue());
    }

    @Override
    protected String createRandomPK() {
        int number = (int) (System.currentTimeMillis() % (1000 * 1000 * 1000));
        return String.valueOf(number);
    }

    @Override
    protected Value createModel(String pk) {
        String value = pk + "-value";
        return new Value(pk, value);
    }

    @After
    public void tearDown() throws Exception {
        Log.i(TAG, "tearDown: ");

        mDao = null;
    }
}
