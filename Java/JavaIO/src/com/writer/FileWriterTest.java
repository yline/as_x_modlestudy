package com.writer;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 现象：生成了一个文件,刷新工程就可以看到
 */
public class FileWriterTest
{
    public static void main(String[] args)
    {
        testFileWriter();
    }
    
    /**
     * 一定要记得关闭,关闭之前会调用执行输出流的flush方法,实现缓冲以及数据最后的实际写入
     * 因此,不关闭,有时候,会获取不到结果
     */
    public static void testFileWriter()
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter("writer.txt");
            fileWriter.write("\r    锦瑟 - 李商隐    \r\n");
            fileWriter.write("锦瑟无端五十弦,一弦一柱思华年.\r\n");
            fileWriter.write("庄生晓梦迷蝴蝶,望帝春心托杜鹃.\r\n");
            fileWriter.write("沧海月明珠有泪,蓝田日暖玉生烟.\r\n");
            fileWriter.write("此情可待成追忆,只是当时已惘然.\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
