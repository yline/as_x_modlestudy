package com.dm.proxy.uml.realsubject;

import com.dm.proxy.uml.subject.ILawsuit;
import com.yline.utils.LogUtil;

/**
 * 具体逻辑的执行
 *
 * @author yline 2020-04-11 -- 12:55
 */
public class XiaoMin implements ILawsuit {

    @Override
    public void submit() {
        LogUtil.v("xiaomin" + "申请 submit");
    }

    @Override
    public void burden() {
        LogUtil.v("xiaomin" + "申请 burden");
    }

    @Override
    public void defend() {
        LogUtil.v("xiaomin" + "申请 defend");
    }

    @Override
    public void finish() {
        LogUtil.v("xiaomin" + "申请 finish");
    }

}
