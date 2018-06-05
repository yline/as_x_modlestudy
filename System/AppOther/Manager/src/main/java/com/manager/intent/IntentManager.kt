package com.manager.intent

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Contacts
import android.provider.MediaStore
import android.provider.Settings
import com.yline.application.SDKManager
import com.yline.log.LogFileUtil
import com.yline.utils.FileUtil
import java.io.File

/**
 * Intent 跳转 管理类(only 系统)
 * @author yline 2018/6/5 -- 15:51
 * @version 1.0.0
 */
object IntentManager {
    private const val HINT_JUMP_FAILED = "跳转失败"

    /**
     * 安装Apk;未测试
     *
     * @param path    /storage/sdcard1/临时文件夹/ActivityBackCode.apk
     */
    fun installApk(context: Context, path: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(Uri.fromFile(File(path)), "application/vnd.android.package-archive")

        context.startActivity(intent)
    }

    /**
     * kill Process myself
     */
    fun killMineProcess() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }


    /**
     * kill Process others;未测试
     *
     * @param context
     * @param packageName
     */
    @SuppressLint("MissingPermission")
    fun killOtherProcess(context: Context, packageName: String) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(packageName)
    }

    /**
     * 拍照失败，则data不为null，其它值全部为null
     * 拍照成功，则data为null
     *
     * @param activity
     * @param fileName    暂存的图片名， camera_picture.jpg
     * @param requestCode
     * @return
     */
    fun openCamera(activity: Activity, fileName: String, requestCode: Int): Boolean {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val outputUri = Uri.fromFile(FileUtil.create(activity.externalCacheDir, fileName))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri) // 存放位置为sdcard卡上cwj文件夹，文件名为android123.jpg格式
        if (null != intent.resolveActivity(activity.packageManager)) {
            activity.startActivityForResult(intent, requestCode)
            return true
        } else {
            LogFileUtil.e("IntentUtil", "camera do not exist")
            return false
        }
    }

    /**
     * 选择成功，返回data不为null, data.getdata 为null
     * 选择失败，返回data为null
     *
     * @param activity
     * @param requestCode
     * @return
     */
    fun openAlbum(activity: Activity, requestCode: Int): Boolean {
        val tempIntent = Intent()
        tempIntent.action = Intent.ACTION_PICK
        tempIntent.type = "image/*"
        if (null != tempIntent.resolveActivity(activity.packageManager)) {
            activity.startActivityForResult(tempIntent, requestCode)
            return true
        } else {
            LogFileUtil.e("IntentUtil", "album do not exist")
            return false
        }
    }

    /**
     * 请求相册，并裁剪
     * 选择成功，返回data不为null
     * 选择失败，返回data为null
     *
     * @param activity
     * @param uri         onActivityResult 返回的结果
     * @param fileName    缓存本地的文件名
     * @param requestCode
     * @return
     */
    fun openPictureZoom(activity: Activity, uri: Uri, fileName: String, requestCode: Int): Boolean {
        val tempIntent = Intent("com.android.camera.action.CROP")

        tempIntent.setDataAndType(uri, "image/*")
        tempIntent.putExtra("crop", "true")

        val outputUri = Uri.fromFile(FileUtil.create(activity.externalCacheDir, fileName))
        tempIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)// 图像输出
        tempIntent.putExtra("aspectX", 3) // 边长比例
        tempIntent.putExtra("aspectY", 3)
        tempIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        tempIntent.putExtra("noFaceDetection", true)
        tempIntent.putExtra("return-data", false)// 回调方法data.getExtras().getParcelable("data")返回数据为空
        if (null != tempIntent.resolveActivity(activity.packageManager)) {
            activity.startActivityForResult(tempIntent, requestCode)
            return true
        } else {
            LogFileUtil.e("IntentUtil", "album zoom do not exist")
            return false
        }
    }

    /**
     * 打开音乐浏览器,浏览文件
     *
     * @param requestCode 请求码
     */
    fun openAudio(activity: Activity, requestCode: Int) {
        val tempIntent = Intent()
        tempIntent.action = Intent.ACTION_PICK
        tempIntent.type = "audio/*"
        if (null != tempIntent.resolveActivity(activity.packageManager)) {
            activity.startActivityForResult(tempIntent, requestCode)
        } else {
            SDKManager.toast(HINT_JUMP_FAILED)
        }
    }

    /**
     * 浏览器
     *
     * @param website http:// website
     * @return
     */
    fun openBrowser(context: Context, website: String) {
        val tempIntent = Intent()
        tempIntent.action = Intent.ACTION_VIEW

        val tempUri: Uri
        if ("http://" == website.substring(0, 7) || "https://" == website.substring(0, 8)) {
            tempUri = Uri.parse(website)
        } else {
            tempUri = Uri.parse("https://$website")
        }
        tempIntent.data = tempUri

        // 跳转
        if (null != tempIntent.resolveActivity(context.packageManager)) {
            context.startActivity(tempIntent)
        } else {
            SDKManager.toast(HINT_JUMP_FAILED)
        }
    }


    /**
     * 打开联系人界面
     *
     * @param context
     */
    fun openContact(context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Contacts.People.CONTENT_URI
        context.startActivity(intent)
    }

    /**
     * 打开 文件管理器(包含音频、图片、视频等等)
     *
     * @param requestCode 请求码
     */
    fun openFile(activity: Activity, requestCode: Int) {
        val tempIntent = Intent()
        tempIntent.action = Intent.ACTION_GET_CONTENT
        tempIntent.type = "*/*"
        tempIntent.addCategory(Intent.CATEGORY_OPENABLE) // 可选择的选项

        if (null != tempIntent.resolveActivity(activity.packageManager)) {
            val target = Intent.createChooser(tempIntent, "chooser")
            activity.startActivityForResult(target, requestCode)
        } else {
            SDKManager.toast(HINT_JUMP_FAILED)
        }
    }

    /**
     * 打开录音器
     *
     * @param context
     */
    fun openRecord(context: Context) {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        context.startActivity(intent)
    }

    /**
     * 打开网络设置界面
     *
     * @param requestCode 请求码
     */
    fun openSettingWifi(activity: Activity, requestCode: Int) {
        val tempIntent = Intent(Settings.ACTION_WIFI_SETTINGS)

        if (null != tempIntent.resolveActivity(activity.packageManager)) {
            activity.startActivityForResult(tempIntent, requestCode)
        } else {
            SDKManager.toast(HINT_JUMP_FAILED)
        }
    }
}