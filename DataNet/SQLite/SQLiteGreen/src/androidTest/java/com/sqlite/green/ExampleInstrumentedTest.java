package com.sqlite.green;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "xxx-ExampleInstrumented";

    @Before
    public void setUp() throws Exception {
        Log.i(TAG, "setUp: ");
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.sqlite.green.test", appContext.getPackageName());
    }

    @Test
    public void sample() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Log.i(TAG, "tearDown: ");
    }
}
