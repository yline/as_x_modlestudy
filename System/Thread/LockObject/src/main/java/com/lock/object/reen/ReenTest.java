package com.lock.object.reen;

import com.yline.utils.LogUtil;

/**
 * 可重入锁，和，不可重入锁，对比
 *
 * @author yline 2018/5/2 -- 16:08
 * @version 1.0.0
 */
public class ReenTest {
    /**
     * 案例A：
     * 打印结果
     * 无锁：A1 -> B1 -> A2 -> B2 -> A3 -> B3 -> A4 -> B4
     * 有锁：A1 -> A2 -> A3 -> A4 -> B1 -> B2 -> B3 -> B4
     * 有锁时：
     * 在A1之后，A2也能执行，说明，单线程内，单线程内，可重复被调用
     * 在A1之后，B1未被执行，说明，多线程，锁起作用了
     * ===>> 可重入锁
     */
    public static void reenSample() {
        sampleA(new ReenLock());
    }

    /**
     * 案例A：
     * 打印结果
     * 无锁：A1 -> B1 -> A2 -> B2 -> A3 -> B3 -> A4 -> B4
     * 有锁：A1
     * 有锁时：
     * A1之后，锁住；B1之后，锁住，发生了死锁
     * ===>> 不可重入锁
     */
    public static void unreenSample() {
        sampleA(new UnreenLock());
    }

    /**
     * 线程A，全部打完之后，才能打印线程B
     * <p>
     * 1，A占用方法，释放之后，B才能执行；说明，满足锁的特性
     * 2，A中两个方法，都被执行了，而锁住的是这个对象，说明，满足可重入锁的特性
     */
    public static void synchronizedSample() {
        final SynLock synLock = new SynLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    synLock.lockedApi();
                } catch (InterruptedException e) {
                    LogUtil.e("InterruptedException", e);
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    synLock.lockedApi();
                } catch (InterruptedException e) {
                    LogUtil.e("InterruptedException", e);
                }
            }
        }, "B").start();
    }

    /**
     * 案例一：
     * 线程A，
     * 1ms，锁住，打印A1
     * 1000ms，锁住，打印A2
     * 2000ms，解锁，打印A3
     * 3000ms，解锁，打印A4
     * <p>
     * 线程B，
     * 500ms，锁住，打印B1
     * 1500ms，锁住，打印B2
     * 2500ms，解锁，打印B3
     * 3500ms，解锁，打印B4
     */
    private static void sampleA(final Object lock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    print(lock, 1, "A1", true);
                    print(lock, 1000, "A2", true);
                    print(lock, 1000, "A3", false);
                    print(lock, 1000, "A4", false);
                } catch (InterruptedException e) {
                    LogUtil.e("InterruptedException", e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    print(lock, 500, "B1", true);
                    print(lock, 1000, "B2", true);
                    print(lock, 1000, "B3", false);
                    print(lock, 1000, "B4", false);
                } catch (InterruptedException e) {
                    LogUtil.e("InterruptedException", e);
                }
            }
        }).start();
    }

    private static void print(Object lock, int millis, String tag, boolean enable) throws InterruptedException {
        Thread.sleep(millis);
        if (enable) {
            if (lock instanceof ReenLock) {
                ((ReenLock) lock).lock();
            } else if (lock instanceof UnreenLock) {
                ((UnreenLock) lock).lock();
            }
        } else {
            if (lock instanceof ReenLock) {
                ((ReenLock) lock).unlock();
            } else if (lock instanceof UnreenLock) {
                ((UnreenLock) lock).unlock();
            }
        }
        LogUtil.v(lock.getClass().getSimpleName() + " - " + tag);
    }
}
