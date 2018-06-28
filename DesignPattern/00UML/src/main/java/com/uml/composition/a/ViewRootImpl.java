package com.uml.composition.a;

import com.yline.utils.LogUtil;

import java.lang.ref.WeakReference;

/**
 * 组合案例 A
 *
 * @author yline 2018/6/28 -- 16:00
 * @version 1.0.0
 */
public class ViewRootImpl {
    final W mWindow;

    public ViewRootImpl() {
        LogUtil.v("init");
        mWindow = new W(this);
    }

    static class W {
        private final WeakReference<ViewRootImpl> mViewAncestor;

        W(ViewRootImpl viewRoot) {
            LogUtil.v("init");
            mViewAncestor = new WeakReference<>(viewRoot);
        }
    }
}
