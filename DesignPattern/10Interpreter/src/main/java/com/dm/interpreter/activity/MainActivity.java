package com.dm.interpreter.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.interpreter.R;
import com.dm.interpreter.operator.caculator.Calculator;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

/**
 * 优点:
 * 灵活的扩展性,当需要对文法规则进行扩展时,只需要增加相应的非终结符解释器,并构建其抽象语法树具体解释即可。
 * <p/>
 * 缺点:
 * 因为每一条文法都需要至少一个解释器,后期维护。
 * 特别是对于复杂的文法,不推荐. 例如该demo中的加入乘法、除法、括号就涉及到优先级问题
 */
public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_caculate).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Calculator calculator = new Calculator("152 + 35123 + 123 - 543");
				LogFileUtil.v(MainApplication.TAG, "计算结果 = " + calculator.calculate());
			}
		});
	}
}
