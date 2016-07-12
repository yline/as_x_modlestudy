package com.dm.template.concrete;

import com.dm.template.MainApplication;
import com.dm.template.abstracts.AbstractComputer;
import com.yline.log.LogFileUtil;

/**
 * 程序员的计算机
 * @author f21
 * @date 2016-3-2
 */
public class CoderComputer extends AbstractComputer
{
    
    @Override
    protected void login()
    {
        super.login();
        LogFileUtil.v(MainApplication.TAG, "程序员只需要进行用户和密码验证就可以了");
    }
}
