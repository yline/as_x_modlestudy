package com.utils;

import com.utils.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 设置宽高
 * -1 match
 * -2 wrap
 * @author YLine
 *
 * 2016年7月17日 上午12:59:50
 */
public class UILayoutUtil
{
    public UILayoutUtil()
    {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    public static void setLayout(View view, int width, int height)
    {
        if (view.getParent() instanceof FrameLayout)
        {
            setFrame(view, width, height);
        }
        else if (view.getParent() instanceof LinearLayout)
        {
            setLinear(view, width, height);
        }
        else if (view.getParent() instanceof RelativeLayout)
        {
            setRelative(view, width, height);
        }
        else
        {
            LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setLayoutAll parent window error");
        }
    }
    
    public static void setMargin(View view, int top, int bottom, int left, int right)
    {
        if (view.getParent() instanceof FrameLayout)
        {
            setFrameMargin(view, top, bottom, left, right);
        }
        else if (view.getParent() instanceof LinearLayout)
        {
            setLinearMargin(view, top, bottom, left, right);
        }
        else if (view.getParent() instanceof RelativeLayout)
        {
            setRelativeMargin(view, top, bottom, left, right);
        }
        else
        {
            LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setMarginAll parent window error");
        }
    }
    
    public static void setLinear(View view, int width, int height)
    {
        android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            lp = new android.widget.LinearLayout.LayoutParams(1, 1);
        }
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setLinearMargin(View view, int top, int bottom, int left, int right)
    {
        android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setLinearMargin lp is null");
            return;
        }
        
        lp.topMargin = top;
        lp.bottomMargin = bottom;
        lp.leftMargin = left;
        lp.rightMargin = right;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRelative(View view, int width, int height)
    {
        android.widget.RelativeLayout.LayoutParams lp =
            (android.widget.RelativeLayout.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            lp = new android.widget.RelativeLayout.LayoutParams(1, 1);
        }
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRelativeMargin(View view, int top, int bottom, int left, int right)
    {
        android.widget.RelativeLayout.LayoutParams lp =
            (android.widget.RelativeLayout.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setRelativeMargin lp is null");
            return;
        }
        
        lp.topMargin = top;
        lp.bottomMargin = bottom;
        lp.leftMargin = left;
        lp.rightMargin = right;
        
        view.setLayoutParams(lp);
    }
    
    public static void setFrame(View view, int width, int height)
    {
        android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            lp = new android.widget.FrameLayout.LayoutParams(1, 1);
        }
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setFrameMargin(View view, int top, int bottom, int left, int right)
    {
        android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            LogFileUtil.e(MainApplication.TAG, "UILayoutUtils -> setFrameMargin lp is null");
            return;
        }
        
        lp.topMargin = top;
        lp.bottomMargin = bottom;
        lp.leftMargin = left;
        lp.rightMargin = right;
        
        view.setLayoutParams(lp);
    }
    
    public static void setViewGroup(View view, int width, int height)
    {
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        
        if (null == lp)
        {
            lp = new android.view.ViewGroup.LayoutParams(1, 1);
        }
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRadioGroup(View view, int width, int height)
    {
        android.widget.RadioGroup.LayoutParams lp = (android.widget.RadioGroup.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            lp = new android.widget.RadioGroup.LayoutParams(1, 1);
        }
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    @SuppressWarnings("deprecation")
    public static void setGallery(View view, int width, int height)
    {
        android.widget.Gallery.LayoutParams lp = (android.widget.Gallery.LayoutParams)view.getLayoutParams();
        
        if (null == lp)
        {
            lp = new android.widget.Gallery.LayoutParams(1, 1);
        }
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
}
