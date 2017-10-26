package com.viewgroup.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.viewgroup.swipeback.view.SwipeBackCallback;
import com.viewgroup.swipeback.view.SwipeBackLayout;
import com.viewgroup.swipeback.view.SwipeBackManager;
import com.yline.test.BaseTestActivity;

/**
 * @author yline 2017/10/26 -- 11:47
 * @version 1.0.0
 */
public class SecondActivity extends BaseTestActivity implements SwipeBackCallback {
    private SwipeBackManager mSwipeBackManager;

    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, SecondActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackManager = new SwipeBackManager(this);
        mSwipeBackManager.onActivityCreate();

        // setContentView(R.layout.swipeback_layout);
        setSwipeBackEnable(true);
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addTextView("SecondActivity");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackManager.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mSwipeBackManager != null) {
            return mSwipeBackManager.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackManager.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackManager.setSwipeBackEnable(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        mSwipeBackManager.scrollToFinishActivity();
    }
}
