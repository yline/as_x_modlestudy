package com.dm.iterator.leader.abstarct;

import com.dm.iterator.MainApplication;
import com.yline.log.LogFileUtil;

public abstract class Leader
{
    protected Leader nextHandler; // 上一级领导处理者
    
    public final void handleRequest(int money)
    {
        if (money <= limit())
        {
            handle(money);
        }
        else
        {
            if (null != nextHandler)
            {
                nextHandler.handleRequest(money);
            }
            else
            {
                LogFileUtil.e(MainApplication.TAG, "上一级领导未指定,批复失败");
            }
        }
    }
    
    /**
     * 自身能批复的额度权限
     * @return
     */
    public abstract int limit();
    
    /**
     * 处理报账行为
     * @param money
     */
    public abstract void handle(int money);
    
    /**
     * 暴露接口给外界,设置上一层领导
     * @param nextHandler
     */
    public void setNextHandler(Leader nextHandler)
    {
        this.nextHandler = nextHandler;
    }
}
