package com.listview.threshold.instance;

import java.util.ArrayList;
import java.util.List;

import com.listview.MainApplication;
import com.listview.R;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author YLine
 *
 * 2016年8月1日 下午10:52:01
 */
public class ArrayAdapterInstance
{
    private List<String> list = new ArrayList<String>();
    
    public void setData()
    {
        list.add("funtion 1");
        list.add("funtion 2");
        list.add("funtion 3");
        list.add("funtion 4");
        list.add("funtion 5");
        list.add("funtion 6");
        list.add("funtion 7");
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void show(Context context, ListView lv)
    {
        lv.setAdapter(new ArrayAdapter(context, R.layout.listview_item, R.id.tv_id, list));
        LogFileUtil.v(MainApplication.TAG, "ArrayAdapterInstance show end");
    }
}
