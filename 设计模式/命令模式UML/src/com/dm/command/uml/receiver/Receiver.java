package com.dm.command.uml.receiver;

import com.dm.command.uml.MainApplication;
import com.yline.log.LogFileUtil;

public class Receiver
{
    /** 真正执行具体命令逻辑的方法 */
    public void action()
    {
        LogFileUtil.v(MainApplication.TAG, "执行具体的操作");
    }
}
