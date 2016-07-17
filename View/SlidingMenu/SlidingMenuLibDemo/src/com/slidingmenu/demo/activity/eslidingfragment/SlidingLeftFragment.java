package com.slidingmenu.demo.activity.eslidingfragment;

import java.util.Arrays;
import java.util.List;

import com.view.slidingmenu.demo.R;
import com.yline.base.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SlidingLeftFragment extends BaseFragment
{
    private View mView;
    
    private ListView mCategories;
    
    private List<String> mDatas = Arrays.asList("list1", "list2", "list3", "list4");
    
    private ListAdapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mView == null)
        {
            initView(inflater, container);
        }
        return mView;
    }
    
    private void initView(LayoutInflater inflater, ViewGroup container)
    {
        mView = inflater.inflate(R.layout.slidingmenu_eslidingfragment_left, container, false);
        mCategories = (ListView)mView.findViewById(R.id.id_listview_categories);
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatas);
        mCategories.setAdapter(mAdapter);
    }
}
