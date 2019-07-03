package com.lock.object.reen;

/**
 * 可重入锁
 * <p>
 * 可重复可递归调用的锁，在外层使用锁之后，在内层仍然可以使用，
 * 并且不发生死锁（前提是同一个对象或者同一个class）
 * <p>
 * ReentrantLock 和 synchronized都是可重入锁
 *
 * @author yline 2019/7/3 -- 15:10
 */
public class ReenLock {
    private boolean isLocked = false; // 锁
    private Thread lockedBy = null; // 锁所在的线程
    private int lockedCount = 0; // 锁的次数

    /**
     * 线程A，首次进入，isLocked = false，允许往下执行
     * 线程A，再次进入，lockedBy = false，允许往下执行
     * 线程B，在线程A进入之后进入，不允许往下执行
     *
     * @throws InterruptedException
     */
    public synchronized void lock() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        while (isLocked && lockedBy != currentThread) {
            wait();
        }

        // 当前线程，获取锁
        isLocked = true;
        lockedCount++;
        lockedBy = currentThread;
    }

    /**
     * 当且仅当，线程相同时，调用，才解锁一次
     */
    public synchronized void unlock() {
        Thread currentThread = Thread.currentThread();
        if (lockedBy == currentThread) {
            lockedCount--;
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }
        }
    }
}
