package com.manager.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 1, 读取图片，减少内存 {
 * <p>
 * }
 * 2, 存储成图片，减少文件大小 {
 * <p>
 * }
 *
 * @author yline 2020/12/22 -- 4:20 PM
 */
public class BitmapManager {
    private static final int DEFAULT_QUALITY = 80;

    /**
     * @param sourcePath 输入文件流
     * @return 返回的Bitmap
     */
    public static Bitmap loadBitmap(String sourcePath) {
        // 源文件检测
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            return null;
        }

        int scale = computeScaleInner(sourcePath);
        return loadBitmapInner(sourcePath, scale, Bitmap.Config.RGB_565);
    }

    /**
     * @param bitmap     保存的图片
     * @param resultPath 存储位置
     * @return 是否成功
     */
    public static boolean saveBitmap(Bitmap bitmap, String resultPath) {
        return saveBitmap(bitmap, resultPath, -1);
    }

    /**
     * @param bitmap     保存的图片
     * @param resultPath 存储位置
     * @return 是否成功
     */
    public static boolean saveBitmap(Bitmap bitmap, String resultPath, int aimSize) {
        // 目标文件地址检测
        File resultFile = new File(resultPath);
        File resultParentDir = resultFile.getParentFile();
        if (!resultParentDir.exists()) {
            resultParentDir.mkdirs();
        }

        Bitmap.CompressFormat format = computeCompressFormatInner(resultPath);
        if (aimSize > 0) {
            int quality = computeQualityInner(bitmap, format, aimSize);
            return saveBitmapInner(bitmap, format, quality, resultFile);
        } else {
            return saveBitmapInner(bitmap, format, DEFAULT_QUALITY, resultFile);
        }
    }

    /* ------------------- 读取图片，减少内存 ------------------- */

    /**
     * @param sourcePath 输入文件流
     * @param scale      读取图片成Bitmap时，采样率；可以起到质量压缩的作用
     * @param config     Bitmap.Config.RGB_565;
     */
    private static Bitmap loadBitmapInner(String sourcePath, int scale, Bitmap.Config config) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = false;
        option.inSampleSize = scale;

        option.inPreferredConfig = config;

        return BitmapFactory.decodeFile(sourcePath, option);
    }

    /**
     * 依据图片宽高，确定图片采样率
     *
     * @param sourcePath 输入的文件
     */
    private static int computeScaleInner(String sourcePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        option.inSampleSize = 1;
        BitmapFactory.decodeFile(sourcePath, option);

        // 原始宽高、进行奇偶处理
        int sourceWidth = (option.outWidth % 2 == 1) ? option.outWidth + 1 : option.outWidth;
        int sourceHeight = (option.outHeight % 2 == 1) ? option.outHeight + 1 : option.outHeight;

        int longSide = Math.max(sourceWidth, sourceHeight);
        int shortSide = Math.min(sourceWidth, sourceHeight);

        float scale = shortSide * 1.0f / longSide;
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return (longSide / 1280 == 0) ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    /* ------------------- 存储成图片，减少文件大小 ------------------- */

    /**
     * 计算，压缩，应该采用的格式
     */
    private static Bitmap.CompressFormat computeCompressFormatInner(String sourcePath) {
        if (!TextUtils.isEmpty(sourcePath)) {
            int lastPointIndex = sourcePath.lastIndexOf(".");
            // 获取文件后缀名 (.jpg)
            String fileSuffix = sourcePath.substring(lastPointIndex);
            if (".png".equals(fileSuffix)) {
                return Bitmap.CompressFormat.PNG;
            } else if (".webp".equals(fileSuffix)) {
                return Bitmap.CompressFormat.WEBP;
            }
        }

        return Bitmap.CompressFormat.JPEG;
    }

    /**
     * 依据 aimSize，动态计算quality值
     * 这里比较损耗性能
     */
    private static int computeQualityInner(Bitmap bitmap, Bitmap.CompressFormat format, int aimSize) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int quality = DEFAULT_QUALITY;
        try {
            for (int i = DEFAULT_QUALITY; i > 0; i -= 10) {
                output.reset();

                quality = i;
                bitmap.compress(format, i, output);
                if (output.size() < aimSize) {
                    break;
                }
            }
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return quality;
    }

    /**
     * 保存图片，参数未做校验
     *
     * @param bitmap     准备保存的图片
     * @param format     图片的格式
     * @param quality    质量压缩的参数【quality(1-100),100表示不压缩】
     * @param resultFile 输出到的文件夹
     */
    private static boolean saveBitmapInner(Bitmap bitmap, Bitmap.CompressFormat format, int quality, File resultFile) {
        // 保存图片，并进行压缩
        boolean isSuccess = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(resultFile);
            isSuccess = bitmap.compress(format, quality, fileOutputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }
}
