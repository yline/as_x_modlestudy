package com.utils.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 检查Root相关
 * 检查su文件，并执行；已测试过，非root返回false，已root手机返回true
 *
 * @author yline 2019/3/6 -- 10:14
 */
public class RootUtil {
    /**
     * 通过判断，手机中是否有su文件，su文件是否能够执行[耗时，0ms]
     *
     * @return true(已经被root了)
     */
    public static boolean checkRootByExecute() {
        File file = getRootFile();
        return (null != file && isCanExecute(file.getPath()));
    }

    /**
     * 检查手机是否存在 su 文件，获取su文件
     *
     * @return su文件 or null
     */
    private static File getRootFile() {
        String[] pathArray = {"/sbin/su", "/system/bin/su", "/system/xbin/su",
                "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};

        for (String path : pathArray) {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    /**
     * 检查su文件，是否有x或s权限
     *
     * @param filePath su 文件的路径，比如/system/bin/su 或者/system/xbin/su
     * @return true(有root权限)
     */
    private static boolean isCanExecute(String filePath) {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls -l " + filePath);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.length() >= 4) {
                char flag = firstLine.charAt(3);
                if (flag == 's' || flag == 'x') {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
        return false;
    }
}
