package com.manager.compress

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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
            }
        }
    }

    private fun compressAlbum(sourcePath: String): String {
        val future = IApplication.fixedThreadExecutor(Callable<String> {
            val startTime = System.currentTimeMillis()

            val resultFile = FileUtil.getFileExternal(this@CompressActivity, "yline", "compressTest" + System.currentTimeMillis())
            val sourceBitmap = BitmapManager.loadBitmap(sourcePath)
            if (null != sourceBitmap) {
                val isSuccess = BitmapManager.saveBitmap(sourceBitmap, resultFile.absolutePath, (400 shl 10))
                LogUtil.v("compressTime = " + (System.currentTimeMillis() - startTime) + ", isSuccess = $isSuccess")
            }
            return@Callable resultFile.absolutePath
        })

        return future.get()
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