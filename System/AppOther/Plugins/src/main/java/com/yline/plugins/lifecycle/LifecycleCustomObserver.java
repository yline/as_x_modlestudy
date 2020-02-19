package com.yline.plugins.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.yline.utils.LogUtil;

public class LifecycleCustomObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onEventCreate() {
        LogUtil.v("fuck create do!!!");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEventStart() {
        LogUtil.v("fuck start do!!!");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onEventResume() {
        LogUtil.v("fuck resume do!!!");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onEventPause() {
        LogUtil.v("fuck pause do!!!");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEventStop() {
        LogUtil.v("fuck stop do!!!");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onEventDestroy() {
        LogUtil.v("fuck destroy do!!!");
    }


}
