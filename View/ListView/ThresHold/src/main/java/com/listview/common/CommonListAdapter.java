package com.listview.common;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * T -> 数据类型
 * @author YLine
 *
 * 2016年8月1日 下午11:16:12
 */
public abstract class CommonListAdapter<T> extends BaseAdapter
{
    protected Context sContext;
    
    protected List<T> sList;
    
    public CommonListAdapter(Context context, List<T> list)
    {
        this.sContext = context;
        this.sList = list;
    }
    
    @Override
    public int getCount()
    {
        return sList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return sList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(sContext).inflate(getItemRes(), parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        setViewContent(position, parent, holder);
        
        return convertView;
    }
    
    /**
     * @return	item 资源文件
     */
    protected abstract int getItemRes();
    
    /**
     * 对内容设置
     * @param position	当前item位置
     * @param parent	副控件(一般不用)
     * @param item	ViewHolder
     */
    protected abstract void setViewContent(int position, ViewGroup parent, ViewHolder item);
    
    protected class ViewHolder
    {
        private SparseArray<View> sArray;
        
        private View sView;
        
        public ViewHolder(View view)
        {
            this.sView = view;
            sArray = new SparseArray<>();
        }
        
        /**
         * 获取到相应的资源
         * @param resId
         * @return
         */
        public <T extends View> T get(int viewId)
        {
            if (sArray.get(viewId) == null)
            {
                View view = sView.findViewById(viewId);
                sArray.put(viewId, view);
            }
            return (T)sArray.get(viewId);
        }
        
        /**
         * 要求是TextView;   这样的方法就可以多写几个,然后就可以作死的连缀了
         * @param viewId	资源
         * @param content	内容
         * @return	为了连缀写法,返回自身
         */
        public ViewHolder setText(int viewId, String content)
        {
            TextView textView = this.get(viewId);
            textView.setText(content);
            return this;
        }
    }
}
