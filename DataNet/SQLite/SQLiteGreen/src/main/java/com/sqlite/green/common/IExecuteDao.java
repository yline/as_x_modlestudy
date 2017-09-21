package com.sqlite.green.common;

import java.util.List;

/**
 * 数据操作 接口
 * 1） load
 * 2） insert
 * 3）insertOrReplace
 * // 4） query
 * // 5） delete
 * // 6） update
 * 7） count
 * 7-1）getKey
 *
 * @author yline 2017/9/7 -- 10:03
 * @version 1.0.0
 */
public interface IExecuteDao<Key, Model> {
    public static final int Error = -1;

    /**
     * 依据 Key 获取 Model
     * 默认读取缓存
     *
     * @param key 关键词
     * @return 单条数据
     */
    Model load(Key key);

    /**
     * 读取所有数据
     * 默认读取缓存(单条读取)
     *
     * @return 数据队列
     */
    List<Model> loadAll();

    /**
     * 插入单条数据，如果重复，则不会插入数据
     * 默认缓存写入
     *
     * @param model 单条数据
     * @return rowId
     */
    long insert(Model model);

    /**
     * 插入多条数据，开启事务，如果重复，则不会插入数据
     * 默认缓存写入
     *
     * @param models 数据队列
     */
    void insertInTx(Iterable<Model> models);

    /**
     * 插入多条数据，开启事务，如果重复，则不会插入数据
     *
     * @param models 数据队列
     * @param cache  是否写入缓存
     */
    void insertInTx(Iterable<Model> models, boolean cache);

    /**
     * 插入单条数据
     * 默认缓存写入
     *
     * @param model 单条数据
     * @return rowId
     */
    long insertOrReplace(Model model);

    /**
     * 插入多条数据，开启事务
     * 默认缓存写入
     *
     * @param models 数据队列
     */
    void insertOrReplaceInTx(Iterable<Model> models);

    /**
     * 插入多条数据，开启事务
     *
     * @param models 数据队列
     * @param cache  是否写入缓存
     */
    void insertOrReplaceInTx(Iterable<Model> models, boolean cache);

    /**
     * @return 行数
     */
    long count();

    /**
     * 依据Model 获取 缓存的Key
     *
     * @param module 数据队列
     * @return 关键词
     */
    Key getKey(Model module);

    /**
     * 清除所有数据
     */
    void deleteAll();
}
