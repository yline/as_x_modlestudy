package com.dm.proxy.uml.subject;

/**
 * 普通的业务方法
 *
 * @author yline 2020-04-11 -- 12:55
 */
public interface ILawsuit {
    /**
     * 提交申请
     */
    void submit();

    /**
     * 进行举证
     */
    void burden();

    /**
     * 开始辩护
     */
    void defend();

    /**
     * 诉讼完成
     */
    void finish();
}
