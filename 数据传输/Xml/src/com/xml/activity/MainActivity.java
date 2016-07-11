package com.xml.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.xml.R;
import com.xml.general.GenInfo;
import com.xml.general.append.XmlGenWithAppend;
import com.xml.general.serializer.XmlGenWithSerializer;
import com.xml.parse.ParseInfo;
import com.xml.parse.pull.ParseUtil;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_general_append).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_general_append");
                
                List<GenInfo> infos = new ArrayList<GenInfo>();
                Random random = new Random();
                for (int i = 0; i < 20; i++)
                {
                    infos.add(new GenInfo(System.currentTimeMillis(), random.nextInt(2) + 1, "body", "address",
                        (random.nextInt(100)) + 15958145457L));
                }
                
                XmlGenWithAppend.generalWithAppend(MainActivity.this, infos);
            }
        });
        
        findViewById(R.id.btn_general_serializer).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_general_serializer");
                
                List<GenInfo> infos = new ArrayList<GenInfo>();
                Random random = new Random();
                for (int i = 0; i < 20; i++)
                {
                    infos.add(new GenInfo(System.currentTimeMillis(), random.nextInt(2) + 1, "body", "address",
                        (random.nextInt(100)) + 15958145457L));
                }
                
                XmlGenWithSerializer.generalWithSerializer(MainActivity.this, infos);
            }
        });
        
        findViewById(R.id.btn_pull).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_pull");
                
                InputStream inputStream = MainActivity.this.getClassLoader().getResourceAsStream("weather.xml");
                try
                {
                    List<ParseInfo> infos = ParseUtil.getWeatherInfo(inputStream);
                    
                    StringBuffer sBuffer = new StringBuffer();
                    for (ParseInfo weatherInfo : infos)
                    {
                        sBuffer.append(weatherInfo.toString());
                        sBuffer.append("\n");
                    }
                    String content = sBuffer.toString();
                    LogFileUtil.v(MainApplication.TAG, content);
                }
                catch (Exception e)
                {
                    LogFileUtil.e(MainApplication.TAG, "解析出错", e);
                }
            }
        });
    }
    
}
