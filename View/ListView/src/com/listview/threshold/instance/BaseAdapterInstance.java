package com.listview.threshold.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.listview.MainApplication;
import com.listview.R;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author YLine
 *
 * 2016年8月1日 下午10:51:54
 */
public class BaseAdapterInstance
{
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    
    private static final String ID = "id";
    
    private static final String NAME = "name";
    
    public void setData()
    {
        list.add(getMap("1", "云彩"));
        list.add(getMap("2", "地球"));
        list.add(getMap("3", "阳光"));
        list.add(getMap("4", "树木"));
        list.add(getMap("5", "天空"));
        list.add(getMap("6", "群山"));
    }
    
    private Map<String, String> getMap(String id, String name)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ID, id);
        map.put(NAME, name);
        return map;
    }
    
    public void show(Context context, ListView lv)
    {
        lv.setAdapter(new ylineBaseAdapter(context, list));
    }
    
    public class ylineBaseAdapter extends BaseAdapter
    {
        private List<Map<String, String>> data;
        
        private Context context;
        
        public ylineBaseAdapter(Context context, List<Map<String, String>> data)
        {
            this.context = context;
            this.data = data;
        }
        
        @Override
        public int getCount()
        {
            return data.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            if (null != data && data.size() > 0)
            {
                return data.get(position);
            }
            return null;
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view;
            ViewHolder viewHolder;
            if (null == convertView)
            {
                view = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_id = (TextView)view.findViewById(R.id.tv_id);
                viewHolder.tv_name = (TextView)view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
            }
            else
            {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }
            
            bindView(position, viewHolder);
            
            return view;
        }
        
        private void bindView(int position, ViewHolder viewHolder)
        {
            viewHolder.tv_id.setText(data.get(position).get(ID));
            viewHolder.tv_name.setText(data.get(position).get(NAME));
            
            LogFileUtil.v(MainApplication.TAG, "BaseAdapterInstance bindView position = " + position);
        }
        
        private class ViewHolder
        {
            private TextView tv_id;
            
            private TextView tv_name;
        }
    }
    
}
