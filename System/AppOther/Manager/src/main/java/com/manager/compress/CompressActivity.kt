package com.manager.compress

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import com.manager.IApplication
import com.manager.intent.IntentManager
import com.yline.test.BaseTestActivity
import com.yline.utils.FileUtil
import com.yline.utils.LogUtil
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Callable

/**
 * 图片压缩
 * @author yline 2018/6/6 -- 15:32
 * @version 1.0.0
 */
class CompressActivity : BaseTestActivity() {
    companion object {
        private const val ALBUM_PICK_PRESS = 1
        private const val ALBUM_PICK_FORMAT = 2

        fun launcher(context: Context) {
            val intent = Intent()
            intent.setClass(context, CompressActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun testStart(view: View?, savedInstanceState: Bundle?) {
        // 测试压缩效果
        addButton("选择图片并进行压缩") {
            IntentManager.openAlbum(this@CompressActivity, ALBUM_PICK_PRESS)
        }

        // 测试，同一个bitmap，不同格式，生成的文件
        addButton("") {
            IntentManager.openAlbum(this@CompressActivity, ALBUM_PICK_FORMAT)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        LogUtil.v("onActivityResult data is null ? -> " + (data == null))
        if (null != data) {
            when (requestCode) {
                ALBUM_PICK_PRESS -> {
                    val sourceFile = ul2File(this@CompressActivity, data.data)
                    if (null != sourceFile) {
                        val sourcePath = sourceFile.absolutePath
                        val resultPath = compressAlbum(sourcePath)

                        CompressResultActivity.launcher(this@CompressActivity, arrayListOf(sourcePath, resultPath))
                    } else {
                        LogUtil.v("选择失败")
                    }
                }
                ALBUM_PICK_FORMAT -> {
                    val sourceFile = ul2File(this@CompressActivity, data.data)
                    if (null != sourceFile) {
                        val sourcePath = sourceFile.absolutePath
                        val pathList = buildImgByFormat(sourcePath)

                        CompressResultActivity.launcher(this@CompressActivity, pathList)
                    } else {
                        LogUtil.v("选择失败")
                    }
                }
            }
        }
    }

    private fun compressAlbum(sourcePath: String): String {
        val future = IApplication.fixedThreadExecutor(Callable<String> {
            val startTime = System.currentTimeMillis()

            val resultFile = FileUtil.getFileExternal(this@CompressActivity, "yline", "compressTest" + System.currentTimeMillis())
            val isSuccess = CompressManager.compress(sourcePath, resultFile.absolutePath, (400 shl 10))
            LogUtil.v("compressTime = " + (System.currentTimeMillis() - startTime) + ", isSuccess = $isSuccess")

            return@Callable resultFile.absolutePath
        })

        return future.get()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun buildImgByFormat(sourcePath: String): ArrayList<String> {
        val future = IApplication.fixedThreadExecutor(Callable<ArrayList<String>> {
            val startTime = System.currentTimeMillis()

            val resultFile8888 = FileUtil.getFileExternal(this@CompressActivity, "yline", "compress8888" + System.currentTimeMillis() + ".bmp")
            val resultFile565 = FileUtil.getFileExternal(this@CompressActivity, "yline", "compress565" + System.currentTimeMillis() + ".bmp")

            val scale = CompressManager.computeScale(sourcePath)
            val loadBitmap = CompressManager.loadBitmap(sourcePath, scale)
            val format = CompressManager.computeCompressFormat(sourcePath)

            val bitmap8888 = loadBitmap.copy(Bitmap.Config.ARGB_8888, false)
            val isSuccess8888 = CompressManager.saveBitmap(bitmap8888, format, 100, resultFile8888)
//            saveBmp(bitmap8888, resultFile8888.absolutePath)

            val bitmap565 = loadBitmap.copy(Bitmap.Config.RGB_565, false)
//            saveBmp(bitmap565, resultFile565.absolutePath)
            val isSuccess565 = CompressManager.saveBitmap(bitmap565, format, 100, resultFile565)

            LogUtil.v("loadBitmap = ${loadBitmap.byteCount shr 10}k, bitmap8888 = ${bitmap8888.byteCount shr 10}k, bitmap565 = ${bitmap565.byteCount shr 10}k")
            LogUtil.v("isSuccess8888 = $isSuccess8888, isSuccess565 = $isSuccess565, diffTime = " + (System.currentTimeMillis() - startTime))

            return@Callable arrayListOf(sourcePath, resultFile8888.absolutePath, resultFile565.absolutePath)
        })
        return future.get()
    }

    /**
     * 将Bitmap存为 .bmp格式图片
     * @param bitmap
     */
    private fun saveBmp(bitmap: Bitmap, fileName: String) {
        // 位图大小
        val nBmpWidth = bitmap.width
        val nBmpHeight = bitmap.height
        // 图像数据大小
        val bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4)
        try {
            // 存储文件名
            val filename = fileName
            val file = File(filename)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileos = FileOutputStream(filename)
            // bmp文件头
            val bfType = 0x4d42
            val bfSize = (14 + 40 + bufferSize).toLong()
            val bfReserved1 = 0
            val bfReserved2 = 0
            val bfOffBits = (14 + 40).toLong()
            // 保存bmp文件头
            writeWord(fileos, bfType)
            writeDword(fileos, bfSize)
            writeWord(fileos, bfReserved1)
            writeWord(fileos, bfReserved2)
            writeDword(fileos, bfOffBits)
            // bmp信息头
            val biSize = 40L
            val biWidth = nBmpWidth.toLong()
            val biHeight = nBmpHeight.toLong()
            val biPlanes = 1
            val biBitCount = 24
            val biCompression = 0L
            val biSizeImage = 0L
            val biXpelsPerMeter = 0L
            val biYPelsPerMeter = 0L
            val biClrUsed = 0L
            val biClrImportant = 0L
            // 保存bmp信息头
            writeDword(fileos, biSize)
            writeLong(fileos, biWidth)
            writeLong(fileos, biHeight)
            writeWord(fileos, biPlanes)
            writeWord(fileos, biBitCount)
            writeDword(fileos, biCompression)
            writeDword(fileos, biSizeImage)
            writeLong(fileos, biXpelsPerMeter)
            writeLong(fileos, biYPelsPerMeter)
            writeDword(fileos, biClrUsed)
            writeDword(fileos, biClrImportant)
            // 像素扫描
            val bmpData = ByteArray(bufferSize)
            val wWidth = nBmpWidth * 3 + nBmpWidth % 4
            var nCol = 0
            var nRealCol = nBmpHeight - 1
            while (nCol < nBmpHeight) {
                {
                    var wRow = 0
                    var wByteIdex = 0
                    while (wRow < nBmpWidth) {
                        val clr = bitmap.getPixel(wRow, nCol)
                        bmpData[nRealCol * wWidth + wByteIdex] = Color.blue(clr) as Byte
                        bmpData[nRealCol * wWidth + wByteIdex + 1] = Color.green(clr) as Byte
                        bmpData[nRealCol * wWidth + wByteIdex + 2] = Color.red(clr) as Byte
                        wRow++
                        wByteIdex += 3
                    }
                }
                ++nCol
                --nRealCol
            }

            fileos.write(bmpData)
            fileos.flush()
            fileos.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    protected fun writeWord(stream: FileOutputStream, value: Int) {
        val b = ByteArray(2)
        b[0] = (value and 0xff).toByte()
        b[1] = (value shr 8 and 0xff).toByte()
        stream.write(b)
    }

    @Throws(IOException::class)
    protected fun writeDword(stream: FileOutputStream, value: Long) {
        val b = ByteArray(4)
        b[0] = (value and 0xff).toByte()
        b[1] = (value shr 8 and 0xff).toByte()
        b[2] = (value shr 16 and 0xff).toByte()
        b[3] = (value shr 24 and 0xff).toByte()
        stream.write(b)
    }

    @Throws(IOException::class)
    protected fun writeLong(stream: FileOutputStream, value: Long) {
        val b = ByteArray(4)
        b[0] = (value and 0xff).toByte()
        b[1] = (value shr 8 and 0xff).toByte()
        b[2] = (value shr 16 and 0xff).toByte()
        b[3] = (value shr 24 and 0xff).toByte()
        stream.write(b)
    }

    private fun ul2File(context: Context, uri: Uri): File? {
        var file: File? = null
        try {
            val inputStream = context.contentResolver.openInputStream(uri)

            file = File(getExternalFilesDir(null), "test_" + System.currentTimeMillis() + ".jpg")
            val fos = FileOutputStream(file)

            val buffer = ByteArray(4096)
            var len = inputStream.read(buffer)
            while (len > 0) {
                fos.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            fos.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }
}