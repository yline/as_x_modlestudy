package com.slidingmenu.demo.activity.esliding;

import com.lib.slidingmenu.SlidingMenu;
import com.lib.slidingmenu.sdk.SlidingActivity;
import com.slidingmenu.demo.activity.MainApplication;
import com.view.slidingmenu.demo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ESlidingActivity extends SlidingActivity
{
    private SlidingMenu mSlidingMenu;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainApplication.addAcitivity(this);
        
        setContentView(R.layout.activity_esliding);
        
        setBehindContentView(R.layout.slidingmenu_esliding_left);
        initMenu(200);
        
        Button btn_switch = (Button)findViewById(R.id.btn_bylib_slidingactivity);
        btn_switch.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                mSlidingMenu.toggle();
            }
        });
    }
    
    private void initMenu(int width)
    {
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        mSlidingMenu.setShadowWidth(10);
        mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        mSlidingMenu.setAboveOffset(200);
        mSlidingMenu.setFadeDegree(0.35f);
        
        ESlidingActivity.this.setSlidingActionBarEnabled(false);
    }
    
    public static void actionStart(Context context)
    {
        Intent intent = new Intent();
        intent.setClass(context, ESlidingActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    protected void onDestroy()
    {
        MainApplication.removeActivity(this);
        super.onDestroy();
    }
}
