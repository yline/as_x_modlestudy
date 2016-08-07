package com.dm.iterator.leader;

import com.dm.iterator.MainApplication;
import com.dm.iterator.leader.abstarct.Leader;
import com.yline.log.LogFileUtil;

public class Director extends Leader
{
    
    @Override
    public int limit()
    {
        return 5000;
    }
    
    @Override
    public void handle(int money)
    {
        LogFileUtil.v(MainApplication.TAG, "主管批复报销" + money + "元");
    }
    
}
