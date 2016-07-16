package com.boye.http.delete;

import org.xutils.x;

import com.yline.base.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseIocFragment extends BaseFragment
{
    private boolean injected = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        injected = true;
        return x.view().inject(this, inflater, container);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (!injected)
        {
            x.view().inject(this, this.getView());
        }
    }
    
}
