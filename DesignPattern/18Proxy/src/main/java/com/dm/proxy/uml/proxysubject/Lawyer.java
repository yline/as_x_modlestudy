package com.dm.proxy.uml.proxysubject;

import com.dm.proxy.uml.activity.MainApplication;
import com.dm.proxy.uml.subject.ILawsuit;
import com.yline.log.LogFileUtil;

public class Lawyer implements ILawsuit
{
    private ILawsuit mLawsuit;
    
    public Lawyer(ILawsuit lawsuit)
    {
        this.mLawsuit = lawsuit;
    }
    
    @Override
    public void submit()
    {
        mLawsuit.submit();
        LogFileUtil.v(MainApplication.TAG, "Lawyer:" + "处理 submit");
    }
    
    @Override
    public void burden()
    {
        mLawsuit.burden();
        LogFileUtil.v(MainApplication.TAG, "Lawyer:" + "处理 burden");
    }
    
    @Override
    public void defend()
    {
        mLawsuit.defend();
        LogFileUtil.v(MainApplication.TAG, "Lawyer:" + "处理 defend");
    }
    
    @Override
    public void finish()
    {
        mLawsuit.finish();
        LogFileUtil.v(MainApplication.TAG, "Lawyer:" + "处理 finish");
    }
    
}
