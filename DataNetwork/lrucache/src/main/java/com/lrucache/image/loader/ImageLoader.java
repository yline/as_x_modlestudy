package com.lrucache.image.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.lrucache.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yline on 2016/12/6.
 * 图片的高效缓存类。
 * 主要通过LruCache内存缓存和DiskLruCache磁盘缓存来工作。
 * 该类的实现思想：
 * 通过loadBitmap方法来分别从缓存、磁盘和网络中加载图片(这里是一层一层来判断的，
 * 如果缓存中有这个图片程序变不再向下执行)。
 * 步骤：
 * 1、ImageLoader构造方法，初始化LruCache和DiskLruCache类。
 * 2、分别实现LruCache和DiskLruCache的增加、删除、查找方法。
 * 3、通过LoadBitmap方法分别从缓存中加载bitmap、磁盘中加载图片、网络中加载图片。
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private LruCache<String, Bitmap> mMemoryCache;

    private DiskLruCache mDiskLruCache;

    private Context mContext;

    private boolean mIsDiskLruCacheCreated = false;

    private static final int TAG_KEY_URI = R.string.ImageLoader;

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;

    private static final int DISK_CACHE_INDEX = 0;

    private static final int MESSAGE_POST_result = 1;

    //cpu核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    //cpu核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    //线程超时时长
    private static final long KEEP_ALIVE = 10l;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "imageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(), sThreadFactory);

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView iv = result.imageView;
            Bitmap bitmap = result.bitmap;
            String getUrl = (String) iv.getTag(TAG_KEY_URI);
            if (getUrl.equals(result.url)) {
                iv.setImageBitmap(bitmap);
            } else {
                Log.i(TAG, "set image bitmap,but url has changed");
            }
        }
    };

    //无参构造方法，来初始化LruCache|DiskLruCache
    public ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        initLruCache();
        initDiskLruCache();
    }

    /**
     * 初始化LruCache
     */
    private void initLruCache() {
        //计算缓存的大小
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //设置缓存大小为当前进程可用内存的1/8
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    /**
     * 初始化DiskLruCache
     */
    private void initDiskLruCache() {
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) { //如果不存在你们 创建目录
            diskCacheDir.mkdir();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void bindBitmap(String url, ImageView imageView) {
        bindBitmap(url, imageView, 0, 0);
    }

    /**
     * 通过异步方式加载bitmap。
     * 步骤，先从缓存中加载bitmap，如果存在就直接返回，并设置图片。
     * 不存在，就会在线程池中调用loadBitmap方法，来更新UI
     *
     * @param url
     * @param imageView
     * @param reqWidth
     * @param reqHeight
     */
    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, url);
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        //缓存中不存在bitmap图片
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                //线程池中加载bitmap，调用loadBitmap()方法
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_result, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 获取磁盘中可用的空间
     *
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return stats.getBlockSize() * stats.getAvailableBlocks();
    }

    /**
     * 同步加载网络图片
     * 加载图片的顺序：
     * 缓存中加载图片--->磁盘中加载图片--->网络中加载图片
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        //从缓冲中加载图片
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            //从磁盘中加载bitmap
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
            if (bitmap != null) {
                return bitmap;
            }
            //从网络中加载bitmap
            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            //如果bitmap为空，并且，磁盘中没有缓存bitmap。那么通过url路径来获取bitmap
            bitmap = downloadBitmapFromUrl(url);
        }
        return bitmap;
    }

    /**
     * 通过url路径加载网络图片，
     * 获取bitmap。
     *
     * @param urlString
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            bis = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 从缓存中加载bitmap
     *
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        String key = hashKeyFromUrl(url);
        Log.i(TAG, "从缓存中加载数据");
        return getBitmapFromMemoryCache(key);
    }

    /**
     * LruCache的添加方法
     * 存储到缓存中。
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) != null)
            mMemoryCache.put(key, bitmap);
    }

    /**
     * LruCache的查找方法
     * 从缓冲中查询
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 获取磁盘的存储目录，并创建目录
     *
     * @param mContext
     * @param bitmap
     * @return
     */
    private File getDiskCacheDir(Context mContext, String bitmap) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + bitmap);
    }

    /**
     * DiskLruCache添加方法(存储)
     * 将图片写入到本地文件中。
     *
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection conn = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 从网络上加载图片
     *
     * @param urlString
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromHttp(String urlString, int reqWidth, int reqHeight) throws IOException {
        Log.i(TAG, "从网络中加载数据");

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("不能从主线程中访问网络图片");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        //将url转换成key
        String key = hashKeyFromUrl(urlString);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(urlString, outputStream)) { //存储到本地文件夹中
                editor.commit();
            } else {
                editor.abort();
            }
        }
        mDiskLruCache.flush();
        return loadBitmapFromDiskCache(urlString, reqWidth, reqHeight);
    }

    /**
     * 将url转换成String类型的key值
     * 这里采用url的md5值作为key值
     *
     * @param url
     * @return
     */
    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 具体将url转换成MD5值的方式。
     *
     * @param digest
     * @return
     */
    private String bytesToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * DiskLruCache是查询方法
     * 从磁盘中获取Bitmap
     *
     * @param urlString
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromDiskCache(String urlString, int reqWidth, int reqHeight) throws IOException {
        Log.i(TAG, "从磁盘中加载数据");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("不能从主线程中加载图片");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFromUrl(urlString);
        //通过key值获取磁盘中的输入流
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor descriptor = fileInputStream.getFD();
            //通过写的一个ImageResizer高效加载图片
            bitmap = ImageResizer.decodeSampleBitmapFromFile(descriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                //向缓存中加入图片
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 静态方法创建ImageLoader
     *
     * @param context
     * @return
     */
    public static ImageLoader bindBitmap(Context context) {
        return new ImageLoader(context);
    }

    private class LoaderResult {
        public ImageView imageView;

        public String url;

        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            this.bitmap = bitmap;
            this.imageView = imageView;
            this.url = url;
        }
    }
}
