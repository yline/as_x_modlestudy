package com.yline.runtime.xutils.lib.view;

import android.app.Activity;
import android.view.View;

public class ViewFinderCompat {
    private View view;

    public ViewFinderCompat(View view) {
        this.view = view;
    }

    public ViewFinderCompat(Activity activity) {
        this.view = activity.getWindow().getDecorView();
    }

    public View findViewById(int id, int pid) {
        View pView = null;
        if (pid > 0) {
            pView = this.findViewById(pid);
        }

        View view;
        if (null != pView) {
            view = pView.findViewById(id);
        } else {
            view = this.findViewById(id);
        }
        return view;
    }

    private View findViewById(int id) {
        if (null != view) {
            return view.findViewById(id);
        }
        return null;
    }
}
