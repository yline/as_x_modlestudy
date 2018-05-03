package com.lrucache;

import com.lrucache.image.loader.ImageLoader;
import com.yline.application.BaseApplication;

/**
 * 程序入口
 *
 * @author yline 2018/5/3 -- 11:40
 * @version 1.0.0
 */
public class IApplication extends BaseApplication {
    public static final String TAG = "LruCache";

    private static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        imageLoader = new ImageLoader(this);
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }
}
