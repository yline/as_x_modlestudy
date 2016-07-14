package com.dm.facade.out;

import com.dm.facade.activity.MainApplication;
import com.dm.facade.in.CameraSamsung;
import com.dm.facade.in.Phone;
import com.dm.facade.in.PhoneImpl;
import com.yline.log.LogFileUtil;


/**
 * 通过这个类来管理in,中的包
 */
public class MobilePhone
{
    private Phone sPhone;
    
    private CameraSamsung sCameraSamsung;
    
    public MobilePhone()
    {
        this.sPhone = new PhoneImpl();
        this.sCameraSamsung = new CameraSamsung();
    }
    
    public void dail()
    {
        sPhone.dail();
    }
    
    public void videoChat()
    {
        LogFileUtil.v(MainApplication.TAG, "MobilePhone -> videoChat");
        sCameraSamsung.open();
        sPhone.dail();
    }
    
    public void hangup()
    {
        sPhone.hangup();
    }
    
    public void takePicture()
    {
        LogFileUtil.v(MainApplication.TAG, "MobilePhone -> takePicture");
        sCameraSamsung.open();
        sCameraSamsung.takePicture();
    }
    
    public void closeCamera()
    {
        sCameraSamsung.close();
    }
}
