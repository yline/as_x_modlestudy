package com.multi.filter.receiver;

import android.content.Context;
import android.content.Intent;

import com.yline.base.BaseReceiver;
import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/10/25.
 */
public class TestReceiver extends BaseReceiver
{
	public static final String ONE_1 = "test.one.one";

	public static final String ONE_2 = "test.one.two";

	public static final String TWO_1 = "test.two.one";

	public static final String TWO_2 = "test.two.two";

	public static final String TWO_3 = "test.two.three";

	public static final String THREE_1 = "test.three.one";

	public static final String THREE_2 = "test.three.two";

	public static final String FOUR_1 = "test.four.one";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);

		if (null != intent)
		{
			String action = intent.getAction();
			if (null != action)
			{
				LogFileUtil.v("action = " + action);
			}
			else
			{
				LogFileUtil.v("testReceiver action is null");
			}
		}
		else
		{
			LogFileUtil.v("testReceiver intent is null");
		}


	}
}
