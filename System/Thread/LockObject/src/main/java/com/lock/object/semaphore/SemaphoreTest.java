package com.lock.object.semaphore;

import com.yline.utils.LogUtil;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    public static void test() {

        // 3把锁
        final Semaphore semaphore = new Semaphore(3);
        // 5个线程
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();

                        LogUtil.v(Thread.currentThread().getName() + "获取信号");
                        Thread.sleep(300);

                        semaphore.release();
                        LogUtil.v(Thread.currentThread().getName() + "消耗完毕");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "name:" + i).start();
        }
    }
}
