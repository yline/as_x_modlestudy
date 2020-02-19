package com.yline.plugins.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.yline.utils.LogUtil;

public class LifecycleObserver implements DefaultLifecycleObserver {
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        LogUtil.v(owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        LogUtil.v(owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        LogUtil.v(owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        LogUtil.v(owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        LogUtil.v(owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        LogUtil.v(owner.getLifecycle().getCurrentState().name());
    }

}
