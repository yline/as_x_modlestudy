package com.view.webview.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import com.yline.utils.LogUtil;

import java.io.File;

public class IntentUtils {
    public static final String FILE_PROVIDER_AUTHORITY = "com.view.webview.fileProvider";

    public static boolean openAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (null != intent.resolveActivity(activity.getPackageManager())) {
            activity.startActivityForResult(intent, requestCode);
            return true;
        } else {
            LogUtil.e("openAlbum album not exist");
            return false;
        }
    }

    /**
     * 拍照失败，则data不为null，其它值全部为null
     * 拍照成功，则data为null
     *
     * @param fileName    暂存的图片名， camera_picture.jpg
     * @param requestCode 请求码
     * @return true(打开成功)
     */
    public static Uri openCamera(Activity activity, String fileName, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(activity.getExternalCacheDir(), fileName);
        Uri outputUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            /* Android N 写法*/
            outputUri = FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTHORITY, file);
        } else {
            /* Android N之前的老版本写法*/
            outputUri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        if (null != intent.resolveActivity(activity.getPackageManager())) {
            activity.startActivityForResult(intent, requestCode);
            return outputUri;
        } else {
            LogUtil.e("openCamera camera not exist");
            return null;
        }
    }

    /**
     * 打开 文件管理器(包含音频、图片、视频等等)
     *
     * @param requestCode 请求码
     */
    public static boolean openFile(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);   // 可选择的选项

        if (null != intent.resolveActivity(activity.getPackageManager())) {
//            Intent target = Intent.createChooser(intent, "chooser");
            activity.startActivityForResult(intent, requestCode);
            return true;
        } else {
            LogUtil.e("openFile camera not exist");
            return false;
        }
    }
}
