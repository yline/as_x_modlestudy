package com.manager.intent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yline.test.BaseTestActivity
import com.yline.utils.LogUtil

/**
 * IntentUtils 测试工具类
 * @author yline 2018/6/5 -- 15:47
 * @version 1.0.0
 */
class IntentActivity : BaseTestActivity() {
    private val fileName = "temp.jpg"

    companion object {
        private const val ALBUM_PICK = 1
        private const val ALBUM_PICK_ZOOM = 2
        private const val AUDIO_PICK = 3
        private const val FILE_CHOOSE = 5
        private const val SETTING_WIFI = 7
        private const val CAMERA = 8

        fun launcher(context: Context) {
            val intent = Intent()
            intent.setClass(context, IntentActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun testStart(view: View?, savedInstanceState: Bundle?) {
        addButton("系统相册,选取照片,并裁剪") {
            IntentManager.openAlbum(this@IntentActivity, ALBUM_PICK)
        }

        addButton("音乐播放器,选取音频") {
            IntentManager.openAudio(this@IntentActivity, AUDIO_PICK)
        }

        addButton("浏览器,打开网页") {
            IntentManager.openBrowser(this@IntentActivity, "www.baidu.com")
        }

        addButton("打开照相机") {
            IntentManager.openCamera(this@IntentActivity, fileName, CAMERA)
        }

        addButton("打开联系人界面") {
            IntentManager.openContact(this@IntentActivity)
        }

        addButton("文件浏览器,选择文件") {
            IntentManager.openFile(this@IntentActivity, FILE_CHOOSE)
        }

        addButton("打开录音机") {
            IntentManager.openRecord(this@IntentActivity)
        }

        addButton("设置,打开Wifi设置") {
            IntentManager.openSettingWifi(this@IntentActivity, SETTING_WIFI)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        LogUtil.v("onActivityResult data is null ? -> " + (data == null))
        if (null != data) {
            when (requestCode) {
                ALBUM_PICK -> {
                    LogUtil.v("ALBUM_PICK -> " + data.data!!)
                    IntentManager.openPictureZoom(this@IntentActivity, data.data, fileName, ALBUM_PICK_ZOOM)
                }
                ALBUM_PICK_ZOOM -> LogUtil.v("ALBUM_PICK_ZOOM -> $fileName")
                AUDIO_PICK -> LogUtil.v("AUDIO_PICK -> " + data.data!!.path)
                FILE_CHOOSE -> LogUtil.v("FILE_CHOOSE -> " + data.extras!!)
                SETTING_WIFI -> LogUtil.v("SETTING_WIFI -> " + data.extras!!)
                CAMERA -> LogUtil.v("CAMERA -> " + data.extras!!)
                else -> LogUtil.e("onActivityResult requestCode exception")
            }
        }
    }
}