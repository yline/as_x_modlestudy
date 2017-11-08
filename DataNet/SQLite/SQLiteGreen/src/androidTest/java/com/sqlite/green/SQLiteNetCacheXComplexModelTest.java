package com.sqlite.green;

import com.sqlite.green.common.AbstractSQLiteNetCacheTest;
import com.sqlite.green.model.XComplexModel;
import com.sqlite.green.model.XMediumModel;
import com.sqlite.green.test.NetCacheModel;
import com.sqlite.green.test.SimpleModel;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yline 2017/9/14 -- 15:02
 * @version 1.0.0
 */
public class SQLiteNetCacheXComplexModelTest extends AbstractSQLiteNetCacheTest {
    @Override
    protected void assertObject(byte[] tom, byte[] joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Object tomObject = SQLiteIOUtils.byteToObject(tom);
        Object joeObject = SQLiteIOUtils.byteToObject(tom);

        Assert.assertTrue(tomObject instanceof XComplexModel); // 断言一波
        Assert.assertTrue(joeObject instanceof XComplexModel); // 断言一波

        Assert.assertEquals(((XComplexModel) tomObject).getId(), ((XComplexModel) joeObject).getId());
        Assert.assertEquals(((XComplexModel) tomObject).getTime(), ((XComplexModel) joeObject).getTime());
        Assert.assertEquals(((XComplexModel) tomObject).getUserName(), ((XComplexModel) joeObject).getUserName());
        Assert.assertEquals(((XComplexModel) tomObject).getStrList(), ((XComplexModel) joeObject).getStrList());
        // Assert.assertEquals(((XComplexModel) tomObject).getSimpleModelList(), ((XComplexModel) joeObject).getSimpleModelList()); // 内存地址的确不同
        // Assert.assertEquals(((XComplexModel) tomObject).getMediumModelList(), ((XComplexModel) joeObject).getMediumModelList()); // 内存地址的确不同
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

        List<SimpleModel> simpleModelList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            simpleModelList.add(new SimpleModel(i + "-key", i + "-value"));
        }

        List<XMediumModel> mediumModelList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mediumModelList.add(new XMediumModel("id -" + i, strList, intList));
        }

        long time = System.currentTimeMillis();
        byte[] modelByte = SQLiteIOUtils.objectToByte(new XComplexModel(pk, 21, time, strList, simpleModelList, mediumModelList));
        return new NetCacheModel(pk, modelByte);
    }
}
