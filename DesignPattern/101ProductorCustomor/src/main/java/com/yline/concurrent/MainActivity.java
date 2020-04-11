package com.yline.concurrent;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {


    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("one 2 one", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private static void test() {
        final FactoryManager factoryManager = new FactoryManager();

        // 生产者，多个线程和单个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    factoryManager.product();
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 消费者，多个线程和单个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    factoryManager.custom();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
