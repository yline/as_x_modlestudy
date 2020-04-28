package com.utils.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class DeviceUtil {
    private static final int DEVICE_ID_LENGTH = 40;
    private static final String DEVICE_KEY = "device_key";
    private static final String FILE_NAME = "database_file_name";

    private static String DEVICES_ID = null;

    public static synchronized String getDevicesId(Context context) {
        if (TextUtils.isEmpty(DEVICES_ID)) {
            SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            DEVICES_ID = preferences.getString(DEVICE_KEY, "");
            if (TextUtils.isEmpty(DEVICES_ID)) {
                DEVICES_ID = getStringFromLocal(context);

                if (TextUtils.isEmpty(DEVICES_ID)) {
                    DEVICES_ID = generateDeviceId(context);
                } else {
                    preferences.edit().putString(DEVICE_KEY, DEVICES_ID).apply();
                }
            }
        }
        return DEVICES_ID;
    }

    private static String generateDeviceId(Context context) {
        String id = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                id = telephonyManager == null ? "" : telephonyManager.getDeviceId();
            }
        } else {
            id = telephonyManager == null ? "" : telephonyManager.getDeviceId();
        }

        if (TextUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString();
        }
        id = StorageUtils.md5(id);
        saveStringToLocal(context, id);
        return id;
    }

    private static String getStringFromLocal(Context context) {
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(StorageUtils.getPath(context) + "did")));
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void saveStringToLocal(Context context, String id) {
        try {
            if (TextUtils.isEmpty(id)) {
                return;
            }
            SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            preferences.edit().putString(DEVICE_KEY, id).apply();

            String path = StorageUtils.getPath(context) + "did";
            FileWriter writer = new FileWriter(path, false);
            writer.write(id);
            writer.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备、网络信息
     */
    public static String getDevicesInfo(Context context) {
        String deviceInfo = "";
        try {
            //KjtCryptUtils.encryptRequest(getDevicesId(context))
            deviceInfo = String.format("android;%s;1.0.0;%s;%s;%s;wifi", getDevicesId(context), Build.BRAND, Build.MODEL, Build.VERSION.RELEASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }

    private static class StorageUtils {
        private static String sPath = null;  //隐藏文件
        private static String sCachePath = null; //sd卡公开文件

        /**
         * 存放配置
         * 首先去sd卡路径，如果没有sd卡，则取内存路径，两者都取不到，返回null。
         * 获取的路径，可能文件夹并没有创建
         *
         * @return /storage/emulated/0/ or null
         */
        public static synchronized String getPath(Context context) {
            if (sPath == null) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    sPath = Environment.getExternalStorageDirectory().getPath();
                } else {
                    sPath = context.getFilesDir().getAbsolutePath();
                }

                if (!TextUtils.isEmpty(sPath)) {
                    sPath += (File.separator + ".kjt" + File.separator);
                }
            }
            return sPath;
        }


        /**
         * 文件名md5
         */
        public static String md5(String string) {
            if (TextUtils.isEmpty(string)) {
                return "";
            }
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(string.getBytes());
                StringBuilder result = new StringBuilder();
                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result.append(temp);
                }
                return result.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

}
