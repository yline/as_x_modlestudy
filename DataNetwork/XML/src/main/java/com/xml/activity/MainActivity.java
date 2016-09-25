package com.xml.activity;

import android.os.Bundle;
import android.view.View;

import com.data.network.xml.R;
import com.xml.general.GenInfo;
import com.xml.general.append.XmlGenWithAppend;
import com.xml.general.serializer.XmlGenWithSerializer;
import com.xml.parse.ParseInfo;
import com.xml.parse.PullParseUtil;
import com.xml.parse.SaxParseUtil;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity
{
	private SaxParseUtil saxParseUtil;

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
					infos.add(new GenInfo(System.currentTimeMillis(), random.nextInt(2) + 1, "body", "address", (random.nextInt(100)) + 15958145457L));
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
					infos.add(new GenInfo(System.currentTimeMillis(), random.nextInt(2) + 1, "body", "address", (random.nextInt(100)) + 15958145457L));
				}

				XmlGenWithSerializer.generalWithSerializer(MainActivity.this, infos);
			}
		});

		findViewById(R.id.btn_parse_pull).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_pull");

				InputStream inputStream = MainActivity.this.getClassLoader().getResourceAsStream("assets/weather.xml");
				// java 目录下,读取会失败,必须新建assets目录
				// InputStream inputStream = MainActivity.this.getClassLoader().getResourceAsStream("java/weather.xml");
				
				try
				{
					List<ParseInfo> infos = PullParseUtil.getWeatherInfo(inputStream);

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

		saxParseUtil = new SaxParseUtil();
		findViewById(R.id.btn_parse_sax).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_parse_sax");
				InputStream inputStream = MainActivity.this.getClassLoader().getResourceAsStream("assets/weather.xml");
				// java 目录下,读取会失败,必须新建assets目录
				// InputStream inputStream = MainActivity.this.getClassLoader().getResourceAsStream("java/weather.xml");

				saxParseUtil.parseXmlWithSAX(inputStream);
			}
		});
	}

}
