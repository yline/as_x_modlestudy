package com.dm.proxy.uml.proxysubject;

import com.dm.proxy.uml.subject.ILawsuit;
import com.yline.utils.LogUtil;

/**
 * 静态代理的实现，需要手动继承接口；因此有直接依赖
 *
 * @author yline 2020-04-11 -- 12:56
 */
public class Lawyer implements ILawsuit {
    private ILawsuit mLawsuit;

    public Lawyer(ILawsuit lawsuit) {
        this.mLawsuit = lawsuit;
    }

    @Override
    public void submit() {
        mLawsuit.submit();
        LogUtil.v("Lawyer:" + "处理 submit");
    }

    @Override
    public void burden() {
        mLawsuit.burden();
        LogUtil.v("Lawyer:" + "处理 burden");
    }

    @Override
    public void defend() {
        mLawsuit.defend();
        LogUtil.v("Lawyer:" + "处理 defend");
    }

    @Override
    public void finish() {
        mLawsuit.finish();
        LogUtil.v("Lawyer:" + "处理 finish");
    }

}
