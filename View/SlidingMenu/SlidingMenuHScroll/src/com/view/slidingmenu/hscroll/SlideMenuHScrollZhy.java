package com.view.slidingmenu.hscroll;

import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlideMenuHScrollZhy extends HorizontalScrollView
{
    private static final boolean isDebug = false;
    
    private final String tag = "SlideMenuHScrollZhy";
    
    private int mMenuRightPadding;
    
    /** 横屏的百分比 */
    private float mMenuRightPaddingScale = 1 - 320 / 640f;
    
    private int mScreenWidth;
    
    private int mMenuWidth;
    
    private int mHalfMenuWidth;
    
    private boolean once;
    
    private boolean isOpen = false;
    
    private Context mContext;
    
    private ViewGroup mMenu;
    
    private ViewGroup mContent;
    
    public SlideMenuHScrollZhy(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        mScreenWidth = getScreenWidth(mContext);
        mMenuRightPadding = (int)(mScreenWidth * mMenuRightPaddingScale);
        if (isDebug)
        {
            Log.v(tag, "mScreenWidth = " + mScreenWidth + ",mMenuRightPadding" + mMenuRightPadding);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!once)
        {
            LinearLayout wrapper = (LinearLayout)getChildAt(0);
            mMenu = (ViewGroup)wrapper.getChildAt(0);
            mContent = (ViewGroup)wrapper.getChildAt(1);
            
            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 2;
            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth)
                {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                }
                else
                {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        float scaleLeft = 1 - 0.3f * scale;
        float scaleRight = 0.8f + scale * 0.2f;
        
        ViewHelper.setScaleX(mMenu, scaleLeft);
        ViewHelper.setScaleY(mMenu, scaleLeft);
        ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);
        
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, scaleRight);
        ViewHelper.setScaleY(mContent, scaleRight);
    }
    
    private void openMenu()
    {
        if (isOpen)
        {
            return;
        }
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }
    
    private void closeMenu()
    {
        if (isOpen)
        {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }
    
    public void toggle()
    {
        if (isOpen)
        {
            closeMenu();
        }
        else
        {
            openMenu();
        }
    }
    
    private static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
