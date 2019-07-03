package com.lock.object.fair;

/**
 * 公平锁 // 案例为不可重入
 * 在并发环境中，每个线程在获取锁时会先查看此锁维护的等待队列，如果为空，或者当前线程是等待队列的第一个，就占有锁
 * 否则就会加入到等待队列中，以后会按照FIFO的规则从队列中取到自己【先进先出】
 *
 * // 锁，默认为非公平锁
 *
 * @author yline 2019/7/3 -- 19:43
 */
public class FairLock {
    private Node currentNode = null; // 当前node，如果为null，则非锁状态
    private Node lastNode = null; // 最后的node

    /**
     * 锁住
     * 当前没有线程被锁，则继续执行，并且标识当前线程拥有锁
     * 当前有线程被锁，则终止新加入的线程运行，并且排队到最后一个
     *
     * @throws InterruptedException
     */
    public synchronized void lock() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        if (null == currentNode) {
            // 没有线程在执行了
            currentNode = new Node(currentThread);
            lastNode = currentNode;
        } else {
            // 还有线程在执行
            currentThread.wait(); // 停止运行
            lastNode.next = new Node(currentThread);
            lastNode = lastNode.next;
        }
    }

    /**
     * 解锁
     * 当前线程执行完成，判断下一个线程，如果有，则将下一个线程唤醒，执行
     */
    public synchronized void unlock() {
        currentNode = currentNode.next; // 移到下一个，开始执行

        if (null != currentNode) {
            currentNode.thread.interrupt(); // 唤醒
        }
    }

    private static final class Node {
        private volatile Thread thread; // 当前线程

        private volatile Node next; // 下一个节点

        public Node(Thread thread) {
            this.thread = thread;
        }
    }
}
