package com.sdcard.utils;

import java.io.File;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public final class SDCardUtil
{
    public SDCardUtil()
    {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 获取SD卡路径,内置
     * @return  /storage/emulated/0/
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }
    
    /**
     * 获取SD卡的已使用容量 单位byte
     * @return 0 if SDcard cannot be use
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static float getSDCardBlockSize()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            
            long blockSize = 0;
            long blockCount = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                blockSize = stat.getBlockSizeLong();
                blockCount = stat.getBlockCountLong() - 4;
            }
            else
            {
                blockSize = stat.getBlockSize();
                blockCount = stat.getBlockCount() - 4;
            }
            
            return blockCount * blockSize;
        }
        return 0;
    }
    
    /**
     * 获取SD卡的剩余容量 单位byte
     * @return 0 if SDCard cannot be use
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardAvailableSize()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            
            long blockSize = 0;
            long availableBlocks = 0;
            // 版本判断
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong() - 4;
            }
            else
            {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks() - 4;
            }
            
            return availableBlocks * blockSize;
        }
        return 0;
    }
    
    /**
     * 获取系统存储路径
     * @return  /System/
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath() + File.separator;
    }
    
    /**
     * 判断SDCard是否可用
     * @return true if can be use
     */
    public static boolean isSDCardEnable()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    
}
