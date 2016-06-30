package com.writer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 字符流,如果进行文本内容输入输出时,首要考虑
 * 字节流,如果进行二进制内容输入输出时,首要考虑
 */
public class StringWriterTest
{
    public static void main(String[] args)
    {
        testStringStream();
    }
    
    public static void testStringStream()
    {
        // 读取内容
        String string = "content A";
        char[] cs = new char[32];
        int hasRead = 0;
        StringReader stringReader = new StringReader(string);
        
        try
        {
            while ((hasRead = stringReader.read(cs)) > 0)
            {
                System.out.println(new String(cs, 0, hasRead));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        // 写入内容
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("\r    锦瑟 - 李商隐    \r\n");
        stringWriter.write("锦瑟无端五十弦,一弦一柱思华年.\r\n");
        stringWriter.write("庄生晓梦迷蝴蝶,望帝春心托杜鹃.\r\n");
        stringWriter.write("沧海月明珠有泪,蓝田日暖玉生烟.\r\n");
        stringWriter.write("此情可待成追忆,只是当时已惘然.\r\n");
        // 输出内容
        System.out.println(stringWriter.toString());
    }
}
