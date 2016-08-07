package com.dm.template.concrete;

import com.dm.template.MainApplication;
import com.dm.template.abstracts.AbstractComputer;
import com.yline.log.LogFileUtil;

public class MilitaryComputer extends AbstractComputer
{
    
    @Override
    protected void checkHardware()
    {
        super.checkHardware();
        LogFileUtil.v(MainApplication.TAG, "检查硬件防火墙");
    }
    
    @Override
    protected void login()
    {
        super.login();
        LogFileUtil.v(MainApplication.TAG, "进行指纹识别等复杂的用户验证");
    }
    
}
