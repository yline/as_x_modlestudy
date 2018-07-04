package com.dm.iterator.leader;

import com.dm.iterator.MainApplication;
import com.dm.iterator.leader.abstarct.Leader;
import com.yline.log.LogFileUtil;

public class Boss extends Leader {

    @Override
    public int limit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void handle(int money) {
        LogFileUtil.v(MainApplication.TAG, "老板批复报销" + money + "元");
    }

}
