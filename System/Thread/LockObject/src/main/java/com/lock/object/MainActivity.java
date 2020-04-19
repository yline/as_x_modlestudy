package com.lock.object;

import android.os.Bundle;
import android.view.View;

import com.lock.object.reen.ReenTest;
import com.lock.object.semaphore.SemaphoreTest;
import com.lock.object.sync.SynchronizedLock;
import com.lock.object.sync.SynchronizedLockSimple;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        /* ---------------------- 可重入性 ---------------------- */
        addButton("reenLock", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReenTest.reenSample();
            }
        });

        addButton("unreenLock", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReenTest.unreenSample();
            }
        });

        addButton("synchronized 可重入性测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReenTest.synchronizedSample();
            }
        });

        // 单纯的 synchronized 关键词
        addButton("synchronized", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynchronizedLockSimple.test("AA", 1500, 500, 500);
                SynchronizedLockSimple.test("BB", 1000, 3000, 500);
            }
        });

        // 单独测试 lock.wait
        addButton("testWait", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynchronizedLock.testWait("wait forever", 500, -1, 500);
                SynchronizedLock.testWait("wait 3000ms", 500, 3000, 500);
            }
        });

        // 单独测试 lock.notify
        addButton("testNotify", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynchronizedLock.testNotify("notify", 500, 500, 500, false);
                SynchronizedLock.testNotify("notifyAll", 500, 500, 500, true);
            }
        });

        // 混合测试 wait + notify
        addButton("testWaitAndNotify", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynchronizedLock.testWait("wait AA", 200, -1, 800);
                SynchronizedLock.testWait("wait BB", 300, -1, 700);
                SynchronizedLock.testWait("wait CC", 400, -1, 600);

                SynchronizedLock.testNotify("notify", 1000, 1, 500, false);
            }
        });

        // 混合测试 wait + notifyAll
        addButton("testWaitAndNotifyAll", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynchronizedLock.testWait("wait AA", 200, -1, 800);
                SynchronizedLock.testWait("wait BB", 300, -1, 700);
                SynchronizedLock.testWait("wait CC", 400, -1, 600);

                SynchronizedLock.testNotify("notify", 1000, 1, 3000, true);
            }
        });

        addButton("Semaphore", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SemaphoreTest.test();
            }
        });
    }
}
