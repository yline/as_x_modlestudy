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
    protected void assertObject(Object tom, Object joe) {
        if (null == tom && null == joe) {
            Assert.assertEquals(true, true);
            return;
        }

        Assert.assertTrue(tom instanceof XComplexModel); // 断言一波
        Assert.assertTrue(joe instanceof XComplexModel); // 断言一波

        Assert.assertEquals(((XComplexModel) tom).getId(), ((XComplexModel) joe).getId());
        Assert.assertEquals(((XComplexModel) tom).getTime(), ((XComplexModel) joe).getTime());
        Assert.assertEquals(((XComplexModel) tom).getUserName(), ((XComplexModel) joe).getUserName());
        Assert.assertEquals(((XComplexModel) tom).getStrList(), ((XComplexModel) joe).getStrList());
        Assert.assertEquals(((XComplexModel) tom).getSimpleModelList(), ((XComplexModel) joe).getSimpleModelList());
        Assert.assertEquals(((XComplexModel) tom).getMediumModelList(), ((XComplexModel) joe).getMediumModelList());
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
        return new NetCacheModel(pk, new XComplexModel(pk, 21, time, strList, simpleModelList, mediumModelList));
    }
}
