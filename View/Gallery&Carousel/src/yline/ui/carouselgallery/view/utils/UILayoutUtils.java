package yline.ui.carouselgallery.view.utils;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 单一的设置宽高
 * -1 match
 * -2 wrap
 */
public class UILayoutUtils
{
    public UILayoutUtils()
    {
        /**
         * 实例化失败
         */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    public static void setMarginAll(View view, int top, int bottom, int left, int right)
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
            Log.e("setMarginAll", "父窗体不在范围内");
        }
    }
    
    public static void setLayoutAllByNew(View view, int width, int height)
    {
        if (view.getParent() instanceof FrameLayout)
        {
            setFrameByNew(view, width, height);
        }
        else if (view.getParent() instanceof LinearLayout)
        {
            setLinearByNew(view, width, height);
        }
        else if (view.getParent() instanceof RelativeLayout)
        {
            setRelativeByNew(view, width, height);
        }
        else
        {
            Log.e("setLayoutAllByNew", "父窗体不在范围内");
        }
    }
    
    public static void setLayoutAll(View view, int width, int height)
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
            Log.e("setLayoutAll", "父窗体不在范围内");
        }
    }
    
    public static void setLinear(View view, int width, int height)
    {
        android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)view.getLayoutParams();
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setLinearByNew(View view, int width, int height)
    {
        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(1, 1);
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setLinearMargin(View view, int top, int bottom, int left, int right)
    {
        android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)view.getLayoutParams();
        
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
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRelativeByNew(View view, int width, int height)
    {
        android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(1, 1);
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRelativeMargin(View view, int top, int bottom, int left, int right)
    {
        android.widget.RelativeLayout.LayoutParams lp =
            (android.widget.RelativeLayout.LayoutParams)view.getLayoutParams();
        
        lp.topMargin = top;
        lp.bottomMargin = bottom;
        lp.leftMargin = left;
        lp.rightMargin = right;
        
        view.setLayoutParams(lp);
    }
    
    public static void setFrame(View view, int width, int height)
    {
        android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams)view.getLayoutParams();
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setFrameByNew(View view, int width, int height)
    {
        android.widget.FrameLayout.LayoutParams lp = new android.widget.FrameLayout.LayoutParams(1, 1);
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setFrameMargin(View view, int top, int bottom, int left, int right)
    {
        android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams)view.getLayoutParams();
        
        lp.topMargin = top;
        lp.bottomMargin = bottom;
        lp.leftMargin = left;
        lp.rightMargin = right;
        
        view.setLayoutParams(lp);
    }
    
    public static void setViewGroup(View view, int width, int height)
    {
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setViewGroupByNew(View view, int width, int height)
    {
        android.view.ViewGroup.LayoutParams lp = new android.view.ViewGroup.LayoutParams(1, 1);
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRadioGroup(View view, int width, int height)
    {
        android.widget.RadioGroup.LayoutParams lp = (android.widget.RadioGroup.LayoutParams)view.getLayoutParams();
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    public static void setRadioGroupByNew(View view, int width, int height)
    {
        android.widget.RadioGroup.LayoutParams lp = new android.widget.RadioGroup.LayoutParams(1, 1);
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    @SuppressWarnings("deprecation")
    public static void setGallery(View view, int width, int height)
    {
        android.widget.Gallery.LayoutParams lp = (android.widget.Gallery.LayoutParams)view.getLayoutParams();
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
    
    @SuppressWarnings("deprecation")
    public static void setGalleryByNew(View view, int width, int height)
    {
        android.widget.Gallery.LayoutParams lp = new android.widget.Gallery.LayoutParams(1, 1);
        
        lp.width = width;
        lp.height = height;
        
        view.setLayoutParams(lp);
    }
}
