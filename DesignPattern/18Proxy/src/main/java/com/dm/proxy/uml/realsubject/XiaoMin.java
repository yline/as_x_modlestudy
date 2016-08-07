package com.dm.proxy.uml.realsubject;

import com.dm.proxy.uml.activity.MainApplication;
import com.dm.proxy.uml.subject.ILawsuit;
import com.yline.log.LogFileUtil;

public class XiaoMin implements ILawsuit
{
    
    @Override
    public void submit()
    {
        LogFileUtil.v(MainApplication.TAG, "xiaomin" + "申请 submit");
    }
    
    @Override
    public void burden()
    {
        LogFileUtil.v(MainApplication.TAG, "xiaomin" + "申请 burden");
    }
    
    @Override
    public void defend()
    {
        LogFileUtil.v(MainApplication.TAG, "xiaomin" + "申请 defend");
    }
    
    @Override
    public void finish()
    {
        LogFileUtil.v(MainApplication.TAG, "xiaomin" + "申请 finish");
    }
    
}
