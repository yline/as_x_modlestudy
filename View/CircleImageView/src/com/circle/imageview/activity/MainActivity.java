package com.circle.imageview.activity;

import com.circle.imageview.R;
import com.yline.base.BaseFragmentActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends BaseFragmentActivity
{
    private ListView listView;
    
    private static final String[] strs =
        new String[] {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "nineth", "ten"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        initView();
        initData();
    }
    
    private void initView()
    {
        listView = (ListView)findViewById(R.id.show_listview);
    }
    
    private void initData()
    {
        // listView计算高度就可以了
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
    }
}
