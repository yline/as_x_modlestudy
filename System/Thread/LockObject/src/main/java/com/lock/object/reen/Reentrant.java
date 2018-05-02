package com.lock.object.reen;

import com.lock.object.utils.Utils;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantLock 测试
 *
 * @author yline 2018/5/2 -- 16:08
 * @version 1.0.0
 */
public class Reentrant {
    private static ReentrantLock lock = new ReentrantLock();

    public static void testSimple(final int aStartTime, final int aSleepTime, final int bStartTime, final int bSleepTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.doSleepLog("start", aStartTime);

                lock.lock();
                Utils.doSleepLog("lock first", aSleepTime);

                lock.lock();
                Utils.doSleepLog("lock second", aSleepTime);
                lock.unlock();

                lock.unlock();
            }
        }, "AA").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.doSleepLog("start", bStartTime);

                lock.lock();
                Utils.doSleepLog("lock first", bSleepTime);
                lock.unlock();
            }
        }, "BB").start();
    }

    public static void testLockInterruptibly(final int aStartTime, final int aSleepTime, final int bStartTime, final int bSleepTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.doSleepLog("start", aStartTime);

                Utils.doSomeLog("lock", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.lockInterruptibly();

                        Utils.doSleepLog("lockInterruptibly", aSleepTime);
                    }
                });
            }
        }, "AA").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.doSleepLog("start", bStartTime);

                lock.lock();
                Utils.doSleepLog("lock", bSleepTime);
                lock.unlock();
            }
        }, "BB").start();
    }

    private static ReentrantReadWriteLock sourceLock = new ReentrantReadWriteLock();

    /**
     * 暂不测试
     * 理论：{读-读 可以同时} {读-写 不可以同时} {写-写 不可以同时}
     */
    public static void testReadWriteLock() {
        sourceLock.readLock().lock(); // 可重入
        // TODO
        sourceLock.readLock().unlock();

        sourceLock.writeLock().lock(); // 不可重入
        // TODO
        sourceLock.writeLock().unlock();
    }
}
