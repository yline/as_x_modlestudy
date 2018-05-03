package com.lrucache;

import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;

import com.lrucache.image.ImageActivity;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.util.LinkedHashMap;

/**
 * 首页入口
 *
 * @author yline 2018/5/3 -- 14:17
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("ImageLoader应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.launcher(MainActivity.this);
            }
        });

        addButton("testLruCache", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testLruCache();
            }
        });
    }

    // LruCache的使用
    private void testLruCache() {
        // 创建一个，LruCache
        final int maxSize = 45; // 1-9的总和
        LruCache<String, Integer> lruCache = new LruCache<String, Integer>(maxSize) {
            // 每一个传入的，由具体内容决定大小；但必须大于0
            @Override
            protected int sizeOf(String key, Integer value) {
                return Math.max(1, value);
            }
        };

        // 添加
        lruCache.put("1", 1);
        lruCache.put("2", 2);
        lruCache.put("3", 3);
        lruCache.put("4", 4);
        lruCache.put("5", 5);
        lruCache.put("6", 6);
        lruCache.put("7", 7);
        lruCache.put("8", 8);
        lruCache.put("9", 9);
        logLruCache(lruCache, "first add");

        // 获取对象，相当于调整顺序
        lruCache.get("2");
        lruCache.get("1");
        logLruCache(lruCache, "adjust order");

        // 越界添加，10；理论上，前三条会被删除 (3,4,5)
        lruCache.put("10", 10);
        logLruCache(lruCache, "over add");
    }

    /**
     * 最近访问的，最后输出
     */
    private void logLruCache(LruCache<String, Integer> lruCache, String location) {
        LinkedHashMap<String, Integer> tempMap = (LinkedHashMap<String, Integer>) lruCache.snapshot();
        for (String key : tempMap.keySet()) {
            LogUtil.v(location + ", key = " + key + ", value = " + tempMap.get(key));
        }
    }
}
