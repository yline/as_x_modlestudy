package com.dm.mediator.colleague;

import com.dm.mediator.mediator.Mediator;

public class CPU extends Collleague
{
    private String dataVideo, dataSound; // 视频数据、音频数据
        
    public CPU(Mediator mediator)
    {
        super(mediator);
    }
    
    /**
     * 获取视频数据
     * @return
     */
    public String getDataVideo()
    {
        return this.dataVideo;
    }
    
    /**
     * 获取音频数据
     * @return
     */
    public String getDataSound()
    {
        return this.dataSound;
    }
    
    /**
     * 解码数据
     * @param data
     */
    public void decodeData(String data)
    {
        // 分割音频、视频数据
        String[] tmp = data.split(",");
        // 解析音频、视频数据
        this.dataVideo = tmp[0];
        this.dataSound = tmp[1];
        
        // 告诉中介者自身状态改变
        mediator.changed(this);
    }
    
}
