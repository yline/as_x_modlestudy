package com.dm.mediator.colleague;

import com.dm.mediator.activity.MainApplication;
import com.dm.mediator.mediator.Mediator;
import com.yline.log.LogFileUtil;

public class GraphicsCard extends Collleague
{
    
    public GraphicsCard(Mediator mediator)
    {
        super(mediator);
    }
    
    /**
     * 播放视频
     * @param data
     */
    public void videoPlay(String data)
    {
        LogFileUtil.v(MainApplication.TAG, "视频:" + data);
    }
}
