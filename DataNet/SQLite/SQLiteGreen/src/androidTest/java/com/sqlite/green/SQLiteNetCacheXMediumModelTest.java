package com.sqlite.green;

import android.support.test.runner.AndroidJUnit4;

import com.sqlite.green.common.AbstractSQLiteNetCacheTest;
import com.sqlite.green.model.XMediumModel;
import com.sqlite.green.test.NetCacheModel;

import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * 中等复杂度，model，单元测试
 * @author yline 2017/9/14 -- 15:02
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4.class)
public class SQLiteNetCacheXMediumModelTest extends AbstractSQLiteNetCacheTest {
    @Override
    protected void assertObject(byte[] tom, byte[] joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Object tomObject = SQLiteIOUtils.byteToObject(tom);
        Object joeObject = SQLiteIOUtils.byteToObject(tom);

        Assert.assertTrue(tomObject instanceof XMediumModel); // 断言一波
        Assert.assertTrue(joeObject instanceof XMediumModel); // 断言一波

        Assert.assertEquals(((XMediumModel) tomObject).getUserId(), ((XMediumModel) joeObject).getUserId());
        Assert.assertEquals(((XMediumModel) tomObject).getUserList(), ((XMediumModel) joeObject).getUserList());
        Assert.assertEquals(((XMediumModel) tomObject).getUserIntegerList(), ((XMediumModel) joeObject).getUserIntegerList());
    }

    @Override
    protected NetCacheModel createModel(String pk) {
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strList.add(pk + "-str");
        }

        List<Integer> intList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            intList.add(i);
        }

        byte[] modelByte = SQLiteIOUtils.objectToByte( new XMediumModel(pk, strList, intList));
        return new NetCacheModel(pk,modelByte);
    }
}
