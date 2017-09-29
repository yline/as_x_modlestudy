package com.view.widget.label.sample;

import android.os.Bundle;
import android.view.View;

import com.view.widget.label.sample.activity.WidgetFlowAble3Activity;
import com.view.widget.label.sample.activity.WidgetFlowAbleActivity;
import com.view.widget.label.sample.activity.WidgetFlowActivity;
import com.view.widget.label.sample.activity.WidgetFlowSingleActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState)
    {
        addButton("WidgetFlow", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WidgetFlowActivity.actionStart(MainActivity.this);
            }
        });

        addButton("WidgetFlowAble", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WidgetFlowAbleActivity.actionStart(MainActivity.this);
            }
        });

        addButton("WidgetFlowAble Single", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WidgetFlowSingleActivity.actionStart(MainActivity.this);
            }
        });

        addButton("WidgetFlowAble Click+Select+Press", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WidgetFlowAble3Activity.actionStart(MainActivity.this);
            }
        });
    }
}
