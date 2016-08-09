package com.view.radiogroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 * RadioGroup 流式布局,即宽度到了,自动换行
 * 现换行支持:垂直方向
 * @author YLine
 *
 * 2016年7月17日 下午12:19:56
 */
public class RadioGroupFlow extends RadioGroup
{
    
    public RadioGroupFlow(Context context)
    {
        super(context);
    }
    
    public RadioGroupFlow(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;
        
        for (int index = 0; index < childCount; index++)
        {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE)
            {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth)
                {
                    x = width;
                    row++;
                    y = row * height + height;
                }
            }
        }
        setMeasuredDimension(maxWidth, y);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++)
        {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE)
            {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth)
                {
                    x = width;
                    row++;
                    y = row * height + height;
                }
                child.layout(x - width, y - height, x, y);
            }
        }
    }
}
