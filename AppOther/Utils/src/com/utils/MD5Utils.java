package com.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.utils.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 算法加密
 */
public final class MD5Utils
{
    
    private MD5Utils()
    {
    }
    
    private static final char hexDigits[] =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    /**
     * bytes 转16进制字符串
     * @param bytes
     * @return "" if input is null;
     */
    public static String toHexString(byte[] bytes)
    {
        if (null == bytes)
        {
            return "";
        }
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
        {
            hex.append(hexDigits[(b >> 4) & 0x0F]);
            hex.append(hexDigits[b & 0x0F]);
        }
        return hex.toString();
    }
    
    /**
     * 文件  md5 加密
     * @param file
     * @return
     * @throws IOException
     */
    public static String md5(File file)
        throws IOException
    {
        MessageDigest messagedigest = null;
        FileInputStream in = null;
        FileChannel ch = null;
        byte[] encodeBytes = null;
        try
        {
            messagedigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messagedigest.update(byteBuffer);
            encodeBytes = messagedigest.digest();
        }
        catch (NoSuchAlgorithmException neverHappened)
        {
            throw new RuntimeException(neverHappened);
        }
        finally
        {
            closeQuietly(in);
            closeQuietly(ch);
        }
        return toHexString(encodeBytes);
    }
    
    /**
     * String md5 加密
     * @param string
     * @return
     */
    public static String md5(String string)
    {
        byte[] encodeBytes = null;
        try
        {
            encodeBytes = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException neverHappened)
        {
            throw new RuntimeException(neverHappened);
        }
        
        return toHexString(encodeBytes);
    }
    
    /**
     * 关闭 字符流
     * @param closeable
     */
    private static void closeQuietly(Closeable closeable)
    {
        if (null != closeable)
        {
            try
            {
                closeable.close();
            }
            catch (Throwable ignored)
            {
                LogFileUtil.e(MainApplication.TAG, "MD5Utils -> closeQuietly ignored", ignored);
            }
        }
    }
}
