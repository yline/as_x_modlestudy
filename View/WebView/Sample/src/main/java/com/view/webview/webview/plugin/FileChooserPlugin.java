package com.view.webview.webview.plugin;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.view.webview.R;
import com.view.webview.utils.IntentUtils;
import com.view.webview.webview.view.PictureSelectDialog;
import com.yline.utils.PermissionUtil;

/**
 * 文件选择支持【作为组件存在】
 *
 * @author yline 2019/10/23 -- 9:39
 */
public class FileChooserPlugin {
    private final int CODE_PERMISSION;

    private final int CODE_CAMERA_FILE;
    private final int CODE_ALBUM_FILE;
    private final int CODE_ALL_FILE;

    private FileChooserPlugin(int codePermission, int codeCameraFile, int codeAlbumFile, int codeAllFile) {
        this.CODE_PERMISSION = codePermission;
        this.CODE_CAMERA_FILE = codeCameraFile;
        this.CODE_ALBUM_FILE = codeAlbumFile;
        this.CODE_ALL_FILE = codeAllFile;
    }

    public static FileChooserPlugin create(int codePermission, int codeCameraFile, int codeAlbumFile, int codeAllFile) {
        return new FileChooserPlugin(codePermission, codeCameraFile, codeAlbumFile, codeAllFile);
    }

    private ValueCallback<Uri[]> mFilePathCallback;
    private Uri mCameraUri; // 拍照，保存的路径

    public void showFileChooser(final Activity activity, ValueCallback<Uri[]> filePathCallback) {
        mFilePathCallback = filePathCallback;

        final PictureSelectDialog selectDialog = new PictureSelectDialog(activity);
        selectDialog.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();

                // 拍照
                boolean isRequestPermission = PermissionUtil.request(activity, CODE_PERMISSION, Manifest.permission.CAMERA);
                if (!isRequestPermission) {
                    mCameraUri = IntentUtils.openCamera(activity, "temp" + System.currentTimeMillis() + ".jpg", CODE_CAMERA_FILE);
                }
            }
        });
        selectDialog.setOnAlbumClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();

                // 图片上传
                IntentUtils.openAlbum(activity, CODE_ALBUM_FILE);
            }
        });
        selectDialog.setOnFileClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();

                // 文件选择
                IntentUtils.openFile(activity, CODE_ALL_FILE);
            }
        });
        selectDialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();

                if (null != mFilePathCallback) {
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                }
            }
        });
        selectDialog.show();
    }

    public void onRequestPermissionsResult(final Activity activity, int requestCode, int[] grantResults) {
        boolean isPermissionGranted = PermissionUtil.isPermissionGranted(grantResults);
        if (isPermissionGranted) {
            mCameraUri = IntentUtils.openCamera(activity, "temp" + System.currentTimeMillis() + ".jpg", CODE_CAMERA_FILE);
        } else {
            Toast.makeText(activity, R.string.picture_permission_deny, Toast.LENGTH_LONG).show();

            if (null != mFilePathCallback) {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //图片上传
        if (requestCode == CODE_ALBUM_FILE || requestCode == CODE_CAMERA_FILE || requestCode == CODE_ALL_FILE) {
            if (null == mFilePathCallback) {
                return;
            }

            if (resultCode != Activity.RESULT_OK) {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
                return;
            }

            Uri[] results = null;

            if (requestCode == CODE_ALBUM_FILE) {
                if (data == null) {
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                    return;
                }

                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            } else if (requestCode == CODE_CAMERA_FILE) {
                if (null == mCameraUri) {
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                    return;
                }
                results = new Uri[]{mCameraUri};
            } else {
                if (null == data) {
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                    return;
                }

                Uri uri = data.getData();
                if (null != uri) {
                    results = new Uri[]{uri};
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }
}
