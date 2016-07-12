package com.dm.mediator.colleague;

import com.dm.mediator.activity.MainApplication;
import com.dm.mediator.mediator.Mediator;
import com.yline.log.LogFileUtil;

public class SoundCard extends Collleague
{
    
    public SoundCard(Mediator mediator)
    {
        super(mediator);
    }
    
    /**
     * 音频数据
     * @param data
     */
    public void soundPlay(String data)
    {
        LogFileUtil.v(MainApplication.TAG, "音频:" + data);
    }
    
}
