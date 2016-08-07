package com.dm.visitor.uml.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.visitor.R;
import com.dm.visitor.uml.structure.BusinessReport;
import com.dm.visitor.uml.visitor.CEOVisitor;
import com.dm.visitor.uml.visitor.CTOVisitor;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_visitor).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 构建报表
				BusinessReport report = new BusinessReport();
				LogFileUtil.v(MainApplication.TAG, "----------CEO Visitor------------");
				report.showReport(new CEOVisitor());

				LogFileUtil.v(MainApplication.TAG, "----------CTO Visitor------------");
				report.showReport(new CTOVisitor());
			}
		});

	}

}
