package com.manager.compress

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.yline.utils.LogUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 加载图片，已经压缩了一次，通过Options
 * 放置图片，可以设置quality，进行再次压缩
 *
 * @author yline 2018/6/6 -- 14:48
 * @version 1.0.0
 */
object CompressManager {
    private const val DEFAULT_QUALITY = 80

    /**
     * 常规意义的压缩
     * 压缩次数：2
     * 1) 读取成bitmap时，压缩
     * 2) 存储成文件时，压缩
     * @param sourcePath 资源路径
     * @param resultPath 结果路径
     */
    fun compress(sourcePath: String, resultPath: String): Boolean {
        LogUtil.v("source = $sourcePath, result = $resultPath")
        val format = computeCompressFormat(sourcePath)
        return compressDefault(File(sourcePath), File(resultPath), format, -1)
    }

    /**
     * 确定目标大小进行压缩
     * 压缩次数：2
     * 1) 读取成bitmap时，压缩
     * 2) 存储成文件时，压缩
     * @param sourcePath 资源路径
     * @param resultPath 结果路径
     * @param aimSize 目标大小
     */
    fun compress(sourcePath: String, resultPath: String, aimSize: Int): Boolean {
        val format = computeCompressFormat(sourcePath)
        return compressDefault(File(sourcePath), File(resultPath), format, aimSize)
    }

    /**
     * @param sourceFile 源文件
     * @param resultFile 输出文件
     * @param format 文件格式
     * @param aimSize 图片目标大小，若无，则按照默认设置压缩
     */
    private fun compressDefault(sourceFile: File, resultFile: File, format: Bitmap.CompressFormat, aimSize: Int): Boolean {
        // 源文件检测
        if (!sourceFile.exists()) {
            return false
        }

        // 目标文件地址检测
        val resultParentDir = resultFile.parentFile
        if (!resultParentDir.exists()) {
            resultParentDir.mkdirs()
        }

        // 压缩算法
        val sourcePath = sourceFile.absolutePath
        val scale = computeScale(sourcePath) // 计算采样率
        val sourceBitmap = loadBitmap(sourcePath, scale) // 加载出 bitmap，第一次压缩

        var quality = DEFAULT_QUALITY
        if (aimSize > 0) {
            quality = computeQuality(sourceBitmap, format, aimSize)
        }
        return saveBitmap(sourceBitmap, format, quality, resultFile) // 保存bitmap，第二次压缩
    }

    /**
     * @param sourcePath 输入文件流
     * @param scale 读取图片成Bitmap时，采样率；可以起到质量压缩的作用
     */
    fun loadBitmap(sourcePath: String, scale: Int): Bitmap {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = false
        option.inSampleSize = scale

        return BitmapFactory.decodeFile(sourcePath, option)
    }

    /**
     *  保存图片，参数未做校验
     *  @param bitmap 准备保存的图片
     *  @param format 图片的格式
     *  @param quality 质量压缩的参数【quality(1-100),100表示不压缩】
     *  @param resultFile 输出到的文件夹
     */
    fun saveBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int, resultFile: File): Boolean {
        // 保存图片，并进行压缩
        var isSuccess = false
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(resultFile)
            isSuccess = bitmap.compress(format, quality, fileOutputStream)
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            if (null != fileOutputStream) {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }
        return isSuccess
    }

    /**
     * 依据 aimSize，动态计算quality值
     */
    private fun computeQuality(bitmap: Bitmap, format: Bitmap.CompressFormat, aimSize: Int): Int {
        val output = ByteArrayOutputStream()

        var quality: Int = DEFAULT_QUALITY
        try {
            for (i in DEFAULT_QUALITY downTo 0 step 10) {
                output.reset()

                quality = i
                bitmap.compress(format, i, output)
                if (output.size() < aimSize) {
                    break
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            output.close()
        }
        return quality
    }

    /**
     * 依据图片宽高，确定图片采样率
     * @param sourcePath 输入的文件
     */
    fun computeScale(sourcePath: String): Int {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        option.inSampleSize = 1
        BitmapFactory.decodeFile(sourcePath, option)

        // 原始宽高、进行奇偶处理
        val sourceWidth = if (option.outWidth % 2 == 1) option.outWidth + 1 else option.outWidth
        val sourceHeight = if (option.outHeight % 2 == 1) option.outHeight + 1 else option.outHeight

        val longSide = Math.max(sourceWidth, sourceHeight)
        val shortSide = Math.min(sourceWidth, sourceHeight)

        val scale = shortSide.toFloat() / longSide
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1
            } else if (longSide < 4990) {
                return 2
            } else if (longSide > 4990 && longSide < 10240) {
                return 4
            } else {
                return if (longSide / 1280 == 0) 1 else longSide / 1280
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return if (longSide / 1280 == 0) 1 else longSide / 1280
        } else {
            return Math.ceil(longSide / (1280.0 / scale)).toInt()
        }
    }

    /**
     * 计算，压缩，应该采用的格式
     */
    fun computeCompressFormat(sourcePath: String): Bitmap.CompressFormat {
        if (sourcePath.isNotEmpty()) {
            val lastPointIndex = sourcePath.lastIndexOf(".")
            // 获取文件后缀名 (.jpg)
            val fileSuffix = sourcePath.substring(lastPointIndex)
            if (".png".equals(fileSuffix, true)) {
                return Bitmap.CompressFormat.PNG
            } else if (".webp".equals(fileSuffix, true)) {
                return Bitmap.CompressFormat.WEBP
            }
        }

        return Bitmap.CompressFormat.JPEG
    }
}