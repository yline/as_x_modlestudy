package com.fragment.tab.fragment;

import com.fragment.tab.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 中间的Fragment视图
 */
public class ShowFragment extends Fragment
{
    private TextView showTv;
    
    private String showStr = "";
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        
        initView(view);
        initData();
    }
    
    private void initView(View view)
    {
        showTv = (TextView)view.findViewById(R.id.tv_show);
    }
    
    private void initData()
    {
        showTv.setText(showStr);
    }
    
    /**
     * 按理论来的应该要分出去，这里就算了
     * @param showStr
     */
    public void setShowStr(String showStr)
    {
        this.showStr = showStr;
    }
}
