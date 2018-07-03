package com.uml.realization.a;

import android.view.View;
import android.view.ViewGroup;

public interface WindowManager {
    void addView(View view, ViewGroup.LayoutParams params);

    void updateViewLayout(View view, ViewGroup.LayoutParams params);

    void removeView(View view);
}
