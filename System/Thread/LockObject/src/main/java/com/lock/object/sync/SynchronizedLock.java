package com.lock.object.sync;

import com.lock.object.utils.Utils;

/**
 * wait(): 导致当前线程等待,直到其它线程调用该同步监视器的notify()方法或者notifyAll()方法来唤醒该线程.
 * 该方法可带  倒计时功能
 * <p>
 * notify(): 唤醒在此同步监视器上等待的单个线程。如果所有线程都在此同步监视器上等待,则会选择唤醒其中一个线程,选择是任意性的.
 * 只有当前线程放弃对该同步监视器的锁定后(使用wait()方法),才可以执行会唤醒的线程.
 * <p>
 * notifyAll(): 唤醒在此同步监视器上等待的所有线程. 只有当前线程放弃对该同步监视器的锁定后才可以执行被唤醒的线程.
 * <p>
 * 上述代码执行步骤分析:
 * 前部代码块代号:"A"; 内部代码块代号"B",后部代码块"C"
 * <p>
 * 总结:
 * 1,wait和notify不是万能的,它要求在Synchronized(lock)内部调用才能保证不出错;即:它要求在"具有控制权的线程"中执行对象的wait或notify方法.
 * 否则报错:java.lang.IllegalMonitorStateException:object not locked by thread before notify()
 *
 * @author YLine
 *         2016年6月26日 下午11:04:13
 */
public class SynchronizedLock {
    private static Object lock = new Object();

    private SynchronizedLock() {
    }

    /**
     * 测试lock.wait
     * 若规定了等待时间：超时，则放弃等待 ------- 等待完成，执行后续代码
     * 若未规定等待时间：若无notify，则永远等待 ------- 阻塞后续执行
     */
    public static void testWait(String threadName, final int startSleepTime, final int lockWaitTime, final int endSleepTime) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Utils.doSleepLog("start", startSleepTime);

                synchronized (lock) {
                    Utils.doSomeLog("lock wait", new Utils.OnCallback() {
                        @Override
                        public void call() throws Exception {
                            if (lockWaitTime > 0) {
                                lock.wait(lockWaitTime);
                            } else {
                                lock.wait();
                            }
                        }
                    });
                }

                Utils.doSleepLog("end", endSleepTime);
            }
        }, threadName).start();
    }

    public static void testNotify(String threadName, final int startSleepTime, final int lockSleepTime, final int endSleepTime, final boolean isAll) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Utils.doSleepLog("start", startSleepTime);

                synchronized (lock) {
                    Utils.doSleepLog("lock", lockSleepTime);

                    Utils.doSomeLog("lock notify", new Utils.OnCallback() {
                        @Override
                        public void call() throws Exception {
                            if (isAll) {
                                lock.notifyAll();
                            } else {
                                lock.notify();
                            }
                        }
                    });
                }

                Utils.doSleepLog("end", endSleepTime);
            }
        }, threadName).start();
    }
}
