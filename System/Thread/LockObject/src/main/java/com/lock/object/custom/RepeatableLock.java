package com.lock.object.custom;

/**
 * 可重入锁
 * 同一个线程，可以多次进入 一个代码块
 * {synchronized  和  ReentrantLock, 都是可重入锁}
 *
 * @author yline 2018/4/26 -- 15:38
 * @version 1.0.0
 */
public class RepeatableLock {
    private boolean isLocked = false;
    private Thread lockedBy = null;

    private int lockedCount = 0;

    public synchronized void lock() throws InterruptedException {
        Thread thread = Thread.currentThread();
        while (isLocked && lockedBy != thread) {
            wait();
        }

        isLocked = true;
        lockedCount++;
        lockedBy = thread;
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == this.lockedBy) {
            lockedCount--;
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }
        }
    }
}
