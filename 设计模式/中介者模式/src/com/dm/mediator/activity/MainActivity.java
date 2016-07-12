package com.dm.mediator.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.mediator.R;
import com.dm.mediator.colleague.CDDevice;
import com.dm.mediator.colleague.CPU;
import com.dm.mediator.colleague.GraphicsCard;
import com.dm.mediator.colleague.SoundCard;
import com.dm.mediator.mediator.MainBoard;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_mainbroad).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                MainBoard mediator = new MainBoard();
                
                CDDevice cdDevice = new CDDevice(mediator);
                CPU cpu = new CPU(mediator);
                GraphicsCard graphicsCard = new GraphicsCard(mediator);
                SoundCard soundCard = new SoundCard(mediator);
                
                mediator.setCDDevice(cdDevice);
                mediator.setCPU(cpu);
                mediator.setGraphicsCard(graphicsCard);
                mediator.setSoundCard(soundCard);
                
                cdDevice.load();
            }
        });
    }
    
}
