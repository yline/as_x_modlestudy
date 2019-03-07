package com.manager.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.yline.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 加载图片，已经压缩了一次，通过Options
 * 放置图片，可以设置quality，进行再次压缩
 *
 * @author yline 2018/6/6 -- 14:48
 * @version 1.0.0
 */
public class CompressManager {
    private static final int DEFAULT_QUALITY = 80;

    /**
     * 常规意义的压缩
     * 压缩次数：2
     * 1) 读取成bitmap时，压缩
     * 2) 存储成文件时，压缩
     *
     * @param sourcePath 资源路径
     * @param resultPath 结果路径
     */
    public static boolean compress(String sourcePath, String resultPath) {
        LogUtil.v("source = $sourcePath, result = $resultPath");
        Bitmap.CompressFormat format = computeCompressFormat(sourcePath);
        return compressDefault(new File(sourcePath), new File(resultPath), format, -1);
    }

    /**
     * 确定目标大小进行压缩
     * 压缩次数：2
     * 1) 读取成bitmap时，压缩
     * 2) 存储成文件时，压缩
     *
     * @param sourcePath 资源路径
     * @param resultPath 结果路径
     * @param aimSize    目标大小
     */
    public static boolean compress(String sourcePath, String resultPath, int aimSize) {
        Bitmap.CompressFormat format = computeCompressFormat(sourcePath);
        return compressDefault(new File(sourcePath), new File(resultPath), format, aimSize);
    }

    /**
     * @param sourceFile 源文件
     * @param resultFile 输出文件
     * @param format     文件格式
     * @param aimSize    图片目标大小，若无，则按照默认设置压缩
     */
    private static boolean compressDefault(File sourceFile, File resultFile, Bitmap.CompressFormat format, int aimSize) {
        // 源文件检测
        if (!sourceFile.exists()) {
            return false;
        }

        // 目标文件地址检测
        File resultParentDir = resultFile.getParentFile();
        if (!resultParentDir.exists()) {
            resultParentDir.mkdirs();
        }

        // 压缩算法
        String sourcePath = sourceFile.getAbsolutePath();
        int scale = computeScale(sourcePath); // 计算采样率
        Bitmap sourceBitmap = loadBitmap(sourcePath, scale); // 加载出 bitmap，第一次压缩

        int quality = DEFAULT_QUALITY;
        if (aimSize > 0) {
            quality = computeQuality(sourceBitmap, format, aimSize);
        }
        return saveBitmap(sourceBitmap, format, quality, resultFile); // 保存bitmap，第二次压缩
    }

    /**
     * @param sourcePath 输入文件流
     * @param scale      读取图片成Bitmap时，采样率；可以起到质量压缩的作用
     */
    public static Bitmap loadBitmap(String sourcePath, int scale) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = false;
        option.inSampleSize = scale;

        option.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeFile(sourcePath, option);
    }

    /**
     * 保存图片，参数未做校验
     *
     * @param bitmap     准备保存的图片
     * @param format     图片的格式
     * @param quality    质量压缩的参数【quality(1-100),100表示不压缩】
     * @param resultFile 输出到的文件夹
     */
    public static boolean saveBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality, File resultFile) {
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

    /**
     * 依据图片宽高，确定图片采样率
     *
     * @param sourcePath 输入的文件
     */
    public static int computeScale(String sourcePath) {
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

    /**
     * 计算，压缩，应该采用的格式
     */
    public static Bitmap.CompressFormat computeCompressFormat(String sourcePath) {
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
     */
    private static int computeQuality(Bitmap bitmap, Bitmap.CompressFormat format, int aimSize) {
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
}
