package com.utils.utils;

import com.yline.log.LogFileUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 该类的方法,未检测过
 *
 * @author yline 2016/10/31 --> 20:51
 * @version 1.0.0
 */
public class Mp3ByteUtil {
    /**
     * mp3格式文件转成byte格式
     *
     * @param path 文件路径
     * @param name 文件名字，加后缀
     * @return byte[] 流
     */
    public static byte[] Mp32Byte(String path, String name) {
        byte[] result = null;
        try {
            File file = new File(path + name);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                BAOS.write(buffer, 0, length);
            }
            fis.close();
            BAOS.close();
            result = BAOS.toByteArray();
        } catch (FileNotFoundException e) {
            LogFileUtil.v("mp3 parse to byte error FileNotFoundException");
        } catch (IOException e) {
            LogFileUtil.v("mp3 parse to byte error IOException");
        }
        return result;
    }

    /**
     * 将byte[]字符流，写进path路径下，新的文件名name中
     *
     * @param bytes 字符流
     * @param path  路径
     * @param name  新的文件名
     */
    public static File Byte2Mp3(byte[] bytes, String path, String name) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        File dir = new File(path);
        if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
            dir.mkdirs();
        }

        try {
            file = new File(path + name);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (FileNotFoundException e) {
            LogFileUtil.v("byte save to mp3 error FileNotFoundException");
        } catch (IOException e) {
            LogFileUtil.v("byte save to mp3 error IOException");
        }
        return file;
    }
}
