package com.lock.object.readwrite;

/**
 * 读写锁
 * 1，读锁，不互斥
 * 2，读写锁，互斥
 * 3，写锁，互斥
 *
 * ReentrantReadWriteLock
 *
 * @author yline 2020/4/8 -- 10:44
 */
public class ReadWriteLock {
    private int readCount = 0;
    private int writeCount = 0;

    public synchronized void lockRead() throws InterruptedException {
        while (writeCount > 0) {
            wait();
        }
        readCount++;
    }

    public synchronized void unlockRead() {
        readCount--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        // 是否有写请求
        while (writeCount > 0) {
            wait();
        }

        writeCount++;
        while (readCount > 0) {
            wait();
        }
    }

    public synchronized void unlockWrite() {
        writeCount--;
        notifyAll();
    }
}
