package com.uml.composition.b;

import java.util.ArrayList;

/**
 * 组合案例 B
 *
 * @author yline 2018/6/28 -- 16:26
 * @version 1.0.0
 */
public class WindowManagerGlobal {
    private final ArrayList<ViewRootImpl> mRoots = new ArrayList<>();

    public void addView() {
        ViewRootImpl root = new ViewRootImpl();
        mRoots.add(root);
    }

    public void remoteView(int index) {
        mRoots.remove(index);
    }

    public void closeAll() {
        mRoots.clear();
    }
}
