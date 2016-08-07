package com.dm.yuanxing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.design.pattern.prototype.R;
import com.dm.yuanxing.cloneable.WordDocument;
import com.yline.log.LogFileUtil;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_clone).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				WordDocument originDoc = new WordDocument();

				originDoc.setText("原始文档");
				originDoc.addImage("原始图片1");
				originDoc.addImage("原始图片2");
				originDoc.addImage("原始图片3");
				originDoc.showDocument();

				try
				{
					WordDocument doc2 = (WordDocument) originDoc.clone();
					doc2.showDocument();

					doc2.setText("修改过的文档");
					doc2.addImage("修改图片1");
					doc2.showDocument();

					originDoc.showDocument();
				}
				catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
					LogFileUtil.e(MainApplication.TAG, "MainActivity -> btn_clone CloneNotSupportedException", e);
				}
			}
		});

	}

}
