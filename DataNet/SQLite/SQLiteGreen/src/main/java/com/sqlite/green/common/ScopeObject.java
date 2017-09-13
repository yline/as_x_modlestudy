package com.sqlite.green.common;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据缓存类
 *
 * @author yline 2017/9/7 -- 11:00
 * @version 1.0.0
 */
public class ScopeObject<Key, Module> implements IExecuteScope<Key, Module> {

    private final HashMap<Key, Reference<Module>> mHashMap;
    private final ReentrantLock mReentrantLock;

    public ScopeObject() {
        mHashMap = new HashMap<>();
        mReentrantLock = new ReentrantLock();
    }

    @Override
    public Module get(Key key) {
        Reference<Module> reference;

        mReentrantLock.lock();
        try {
            reference = mHashMap.get(key);
        } finally {
            mReentrantLock.unlock();
        }

        if (null != reference) {
            return reference.get();
        } else {
            return null;
        }
    }

    @Override
    public Module getNoLock(Key key) {
        Reference<Module> reference = mHashMap.get(key);
        if (null != reference) {
            return reference.get();
        } else {
            return null;
        }
    }

    @Override
    public void put(Key key, Module module) {
        mReentrantLock.lock();
        try {
            mHashMap.put(key, new WeakReference<>(module));
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public void putNoLock(Key key, Module module) {
        mHashMap.put(key, new WeakReference<>(module));
    }

    @Override
    public void remove(Key key) {
        mReentrantLock.lock();
        try {
            mHashMap.remove(key);
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public void remove(Iterable<Key> keys) {
        mReentrantLock.lock();
        try {
            for (Key key : keys) {
                mHashMap.remove(key);
            }
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public void clear() {
        mReentrantLock.lock();
        try {
            mHashMap.clear();
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public void lock() {
        mReentrantLock.lock();
    }

    @Override
    public void unLock() {
        mReentrantLock.unlock();
    }
}
