package com.lock.object.sync;

import com.lock.object.utils.Utils;

/**
 * 测试代码：
 * SynchronizedLockSimple.test("AA", 1500, 500, 500);
 * SynchronizedLockSimple.test("BB", 1000, 3000, 500);
 * <p>
 * # 同时开始
 * 04-26 09:40:25.182 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:61):: xxx->LogFile->Thread = AA, location = start, create sleep time = 1500
 * 04-26 09:40:25.182 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:61):: xxx->LogFile->Thread = BB, location = start, create sleep time = 1000
 * 04-26 09:40:25.188 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:64):: xxx->LogFile->Thread = BB, location = start, try create time = 1000
 * 04-26 09:40:25.189 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:64):: xxx->LogFile->Thread = AA, location = start, try create time = 1500
 * # BB先结束睡眠
 * 04-26 09:40:26.192 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:66):: xxx->LogFile->Thread = BB, location = start, try finish time = 1000
 * 04-26 09:40:26.195 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:71):: xxx->LogFile->Thread = BB, location = start, finish sleep time = 1000
 * # BB先获取到锁，并没有影响到AA的锁前的代码，执行
 * 04-26 09:40:26.198 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:61):: xxx->LogFile->Thread = BB, location = lock, create sleep time = 3000
 * 04-26 09:40:26.201 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:64):: xxx->LogFile->Thread = BB, location = lock, try create time = 3000
 * # AA睡眠结束
 * 04-26 09:40:26.692 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:66):: xxx->LogFile->Thread = AA, location = start, try finish time = 1500
 * 04-26 09:40:26.696 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:71):: xxx->LogFile->Thread = AA, location = start, finish sleep time = 1500
 * # BB睡眠3000ms后，开始释放锁
 * 04-26 09:40:29.205 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:66):: xxx->LogFile->Thread = BB, location = lock, try finish time = 3000
 * 04-26 09:40:29.208 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:71):: xxx->LogFile->Thread = BB, location = lock, finish sleep time = 3000
 * 04-26 09:40:29.211 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:61):: xxx->LogFile->Thread = BB, location = end, create sleep time = 500
 * # AA等待3000 + 1000 - 1500 = 2500后，拿到锁
 * 04-26 09:40:29.211 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:61):: xxx->LogFile->Thread = AA, location = lock, create sleep time = 500
 * 04-26 09:40:29.215 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:64):: xxx->LogFile->Thread = AA, location = lock, try create time = 500
 * # AA后获取到锁，并没有影响到BB的锁后的代码，执行
 * 04-26 09:40:29.216 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:64):: xxx->LogFile->Thread = BB, location = end, try create time = 500
 * 04-26 09:40:29.718 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:66):: xxx->LogFile->Thread = AA, location = lock, try finish time = 500
 * 04-26 09:40:29.720 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:66):: xxx->LogFile->Thread = BB, location = end, try finish time = 500
 * 04-26 09:40:29.721 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:71):: xxx->LogFile->Thread = AA, location = lock, finish sleep time = 500
 * 04-26 09:40:29.724 20293-20320/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:71):: xxx->LogFile->Thread = BB, location = end, finish sleep time = 500
 * 04-26 09:40:29.726 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:61):: xxx->LogFile->Thread = AA, location = end, create sleep time = 500
 * 04-26 09:40:29.730 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:64):: xxx->LogFile->Thread = AA, location = end, try create time = 500
 * 04-26 09:40:30.234 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:66):: xxx->LogFile->Thread = AA, location = end, try finish time = 500
 * 04-26 09:40:30.236 20293-20319/com.system.thread.lockobject V/SynchronizedLockSimple.doSleepLog(L:71):: xxx->LogFile->Thread = AA, location = end, finish sleep time = 500
 * <p>
 * 结论:
 * 1, synchronized会阻塞 内部代码块以及后续的代码块
 * 2, synchronized能够保证同一时刻最多只有一个线程执行该段代码
 *
 * @author YLine
 *         2016年6月26日 下午9:10:29
 */
public class SynchronizedLockSimple {
    private static Object lock = new Object();

    private SynchronizedLockSimple() {
    }

    public static void test(String threadName, final int startSleep, final int lockSleep, final int endSleepTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.doSleepLog("start", startSleep);

                synchronized (lock) {
                    Utils.doSleepLog("lock", lockSleep);
                }

                Utils.doSleepLog("end", endSleepTime);
            }
        }, threadName).start();
    }
}
