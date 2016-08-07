package com.dm.singleton.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用容器实现单例模式,多个单例模式注入统一的管理类
 */
public class SingletonManager
{
    private static Map<String, Object> mObjMap = new HashMap<String, Object>();
    
    private SingletonManager()
    {
    }
    
    public static void register(String key, Object instance)
    {
        if (!mObjMap.containsKey(key))
        {
            mObjMap.put(key, instance);
        }
    }
    
    public static Object getInstance(String key)
    {
        return mObjMap.get(key);
    }
}
