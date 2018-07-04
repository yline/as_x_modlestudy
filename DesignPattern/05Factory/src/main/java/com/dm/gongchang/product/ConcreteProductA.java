package com.dm.gongchang.product;

import com.dm.gongchang.activity.IApplication;
import com.yline.log.LogFileUtil;

public class ConcreteProductA extends Product {
    @Override
    public void method() {
        LogFileUtil.v(IApplication.TAG, "ConcreteProduct A");
    }

}
