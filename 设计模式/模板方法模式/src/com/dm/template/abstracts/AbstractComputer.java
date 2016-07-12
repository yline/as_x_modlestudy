package com.dm.template.abstracts;

import com.dm.template.MainApplication;
import com.yline.log.LogFileUtil;

/** 抽象的电脑 */
public class AbstractComputer
{
    
    protected void powerOn()
    {
        LogFileUtil.v(MainApplication.TAG, "开启电源");
    }
    
    protected void checkHardware()
    {
        LogFileUtil.v(MainApplication.TAG, "硬件检查");
    }
    
    protected void loadOS()
    {
        LogFileUtil.v(MainApplication.TAG, "载入操作系统");
    }
    
    protected void login()
    {
        LogFileUtil.v(MainApplication.TAG, "小白的电脑无验证,直接进入系统");
    }
    
    /**
     * 启动计算机方法
     * 步骤固定为:
     * 开启电源、系统检查、加载操作系统、用户登录
     * 该方法为final,防止算法框架被覆写
     */
    public final void startUp()
    {
        LogFileUtil.v(MainApplication.TAG, "-------------开机 START--------------");
        powerOn();
        checkHardware();
        loadOS();
        login();
        LogFileUtil.v(MainApplication.TAG, "-------------开机 END--------------");
    }
}
