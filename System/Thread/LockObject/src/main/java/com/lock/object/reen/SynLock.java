package com.lock.object.reen;

import com.yline.utils.LogUtil;

/**
 * 验证synchronized 关键字，是可重入锁
 *
 * @author yline 2019/7/3 -- 17:08
 */
public class SynLock {

    public synchronized void lockedApi() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        long diffTime = 0;
        while (diffTime < 2000) {
            Thread.sleep(1);
            if (diffTime % 389 < 10) {
                LogUtil.v(Thread.currentThread().getName() + "1 running" + ", diffTime = " + diffTime);
                lockedApiInner();
            }
            diffTime = System.currentTimeMillis() - startMillis;
        }
    }

    private synchronized void lockedApiInner() {
        LogUtil.v(Thread.currentThread().getName() + "2 running");
    }
}
