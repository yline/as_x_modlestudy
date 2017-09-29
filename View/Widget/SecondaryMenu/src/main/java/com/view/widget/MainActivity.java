package com.view.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.view.widget.widget.SecondaryMenuWidget;
import com.yline.log.LogFileUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.activity_second_menu);

        final SecondaryMenuWidget secondaryWidget = new SecondaryMenuWidget(this, viewGroup);
        secondaryWidget.setData(Arrays.asList("11", "12", "13", "14", "15"));
        secondaryWidget.setOnSecondaryCallback(new SecondaryMenuWidget.OnSecondaryCallback()
        {
            @Override
            public void onFirstSelected(String firstName, int position)
            {
                secondaryWidget.updateSecondList(Arrays.asList(firstName + "1", firstName + "2", firstName + "3", firstName + "4", firstName + "5"));
            }

            @Override
            public void onSecondSelected(String secondName, int position, boolean isSelected)
            {
				/*
				// 实现第一个选项的 霸道逻辑
				if (position == 0)
				{
					if (isSelected)
					{
						// 选中了第一项，取消其它所有项
						secondaryWidget.setSecondSelectOnly(0);
					}
					else
					{
						// 已经选中了第一项，再次选中第一项，则保存第一项处于选择状态
						secondaryWidget.setSecondSelect(0, true);
					}
				}
				else
				{
					// 如果第一项，是已选状态，则第一项取消选择
					if (secondaryWidget.isSecondSelect(0))
					{
						secondaryWidget.setSecondSelect(0, false);
					}
				}
				*/

                LogFileUtil.v("secondName = " + secondName + ", position = " + position + ", isSelected = " + isSelected);
            }

            @Override
            public void onSelectedConfirm(String firstName, List<String> secondList)
            {
                LogFileUtil.v("firstName = " + firstName + ", secondList = " + secondList.toString());
            }
        });
    }
}
