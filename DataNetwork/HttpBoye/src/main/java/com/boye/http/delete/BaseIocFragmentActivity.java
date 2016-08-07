package com.boye.http.delete;

import org.xutils.x;

import com.yline.base.BaseFragmentActivity;

import android.os.Bundle;

public class BaseIocFragmentActivity extends BaseFragmentActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
