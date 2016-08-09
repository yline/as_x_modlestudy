package com.textview.hscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class HScrollTextView extends TextView
{
    
    public HScrollTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    @Override
    public boolean isFocused()
    {
        return true;
    }
}
