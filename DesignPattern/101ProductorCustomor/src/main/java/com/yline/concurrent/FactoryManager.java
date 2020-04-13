package com.yline.concurrent;

import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class FactoryManager {
    private static final int MAX_SIZE = 10;
    private List<String> mList = new ArrayList<>();

    public synchronized void product() {
        try {
            if (mList.size() == MAX_SIZE) {
                wait();
            }

            String data = "cookie: " + (Math.random() * 10 + 1);
            mList.add(data);
            LogUtil.v("product: " + data);

            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void custom() {
        try {
            if (mList.size() == 0) {
                wait();
            }

            String data = mList.remove(0);
            LogUtil.v("custom: " + data);

            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test() {

    }
}
