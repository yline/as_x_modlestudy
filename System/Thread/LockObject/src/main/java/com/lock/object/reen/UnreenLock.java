package com.lock.object.reen;

/**
 * 不可重入锁
 * <p>
 * 无论线程内，还是多线程间，一旦被锁，则无法被继续执行
 * 而且，解锁时，与线程无关；其他线程也允许解锁当前线程
 *
 * @author yline 2019/7/3 -- 14:56
 */
public class UnreenLock {
    private boolean isLocked = false;

    /**
     * 线程A，首次进入，isLocked = false，允许往下执行
     * 线程A，再次进入，isLocked = true，不允许往下执行
     * 线程B，在线程A进入之后进入，不允许往下执行
     *
     * @throws InterruptedException
     */
    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }

        isLocked = true;
    }

    /**
     * 解锁
     */
    public synchronized void unlock() {
        isLocked = false;
        notify();
    }
}
