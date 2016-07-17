package com.slidingmenu.demo.activity.bynew;

import com.lib.slidingmenu.SlidingMenu;
import com.view.slidingmenu.demo.R;
import com.yline.base.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewActivity extends BaseActivity
{
    private Button mBtnSwitch;
    
    private SlidingMenu mSlidingMenu;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        
        initView();
        initMenu(200);
        
        mBtnSwitch.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                mSlidingMenu.toggle();
            }
        });
    }
    
    private void initView()
    {
        mBtnSwitch = (Button)findViewById(R.id.btn_switch_bynew);
    }
    
    private void initMenu(int width)
    {
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        
        mSlidingMenu.setShadowWidth(10);
        mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        mSlidingMenu.setBehindOffset((720 - width));
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        
        mSlidingMenu.setMenu(R.layout.slidingmenu_new_left);
    }
    
    public static void actionStart(Context context)
    {
        Intent intent = new Intent();
        intent.setClass(context, NewActivity.class);
        context.startActivity(intent);
    }
}
