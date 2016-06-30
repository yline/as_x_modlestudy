package com.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 字符流,如果进行文本内容输入输出时,首要考虑
 * 字节流,如果进行二进制内容输入输出时,首要考虑
 */
public class BufferedReaderTest
{
    public static void main(String[] args)
    {
        testBufferedReader();
    }
    
    /**
     * 乱码原因: 字符流转成字节流,必然会造成部分的乱码问题
     */
    public static void testBufferedReader()
    {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        
        String line = null;
        try
        {
            while (null != (line = bufferedReader.readLine()))
            {
                if (line.equals("exit"))
                {
                    System.exit(0);
                }
                // 需要构建新的函数,完成转码工作,东西比较多
                // 以下函数,会造成部分乱码,反正不能用
                line = new String(line.getBytes("GBK"), "utf-8");
                System.out.println("输入内容为:" + line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
