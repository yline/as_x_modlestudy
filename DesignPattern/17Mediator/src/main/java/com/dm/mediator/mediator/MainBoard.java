package com.dm.mediator.mediator;

import com.dm.mediator.colleague.CDDevice;
import com.dm.mediator.colleague.CPU;
import com.dm.mediator.colleague.Collleague;
import com.dm.mediator.colleague.GraphicsCard;
import com.dm.mediator.colleague.SoundCard;

/**
 * Changed 这个参数是给 colleague 调用的; 
 */
public class MainBoard extends Mediator
{
    private CDDevice cdDevice;
    
    private CPU cpu;
    
    private GraphicsCard graphicsCard;
    
    private SoundCard soundCard;
    
    @Override
    public void changed(Collleague colleage)
    {
        if (colleage == cdDevice)
        {
            handleCDData((CDDevice)colleage);
        }
        else if (colleage == cpu)
        {
            handleCPUData((CPU)colleage);
        }
    }
    
    /**
     * 处理光驱读取数据后与其他设备的交互
     * @param cdDevice
     */
    private void handleCDData(CDDevice cdDevice)
    {
        cpu.decodeData(cdDevice.read());
    }
    
    /**
     * 处理CPU读取数据后与其他设备的交互
     * @param cpu
     */
    private void handleCPUData(CPU cpu)
    {
        soundCard.soundPlay(cpu.getDataSound());
        graphicsCard.videoPlay(cpu.getDataVideo());
    }
    
    public void setCDDevice(CDDevice cdDevice)
    {
        this.cdDevice = cdDevice;
    }
    
    public void setCPU(CPU cpu)
    {
        this.cpu = cpu;
    }
    
    public void setGraphicsCard(GraphicsCard graphicsCard)
    {
        this.graphicsCard = graphicsCard;
    }
    
    public void setSoundCard(SoundCard soundCard)
    {
        this.soundCard = soundCard;
    }
    
}
