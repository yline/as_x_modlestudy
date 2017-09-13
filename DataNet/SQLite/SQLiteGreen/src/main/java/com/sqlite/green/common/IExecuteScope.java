package com.sqlite.green.common;

/**
 * 数据缓存 接口
 *
 * @author yline 2017/9/7 -- 10:47
 * @version 1.0.0
 */
public interface IExecuteScope<Key, Model> {
    /**
     * 获取数据；默认加锁
     *
     * @param key 关键词
     * @return 单个数据
     */
    Model get(Key key);

    /**
     * 获取数据
     *
     * @param key 关键词
     * @return 单个数据
     */
    Model getNoLock(Key key);

    /**
     * 放置数据；默认加锁
     *
     * @param key   关键词
     * @param model 单个数据
     */
    void put(Key key, Model model);

    /**
     * 放置数据
     *
     * @param key   关键词
     * @param model 单个数据
     */
    void putNoLock(Key key, Model model);

    /**
     * 移除数据；默认加锁
     *
     * @param key 关键词
     */
    void remove(Key key);

    /**
     * 移除数据；默认加锁
     *
     * @param keys 关键词
     */
    void remove(Iterable<Key> keys);

    /**
     * 移除所有数据
     */
    void clear();

    /**
     * 锁住
     */
    void lock();

    /**
     * 解锁
     */
    void unLock();
}
