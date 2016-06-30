package com.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 字符流,如果进行文本内容输入输出时,首要考虑
 * 字节流,如果进行二进制内容输入输出时,首要考虑
 */
public class FileReaderTest
{
    public static void main(String[] args)
    {
        testFileReader();
    }
    
    public static void testFileReader()
    {
        // 创建字符输入流
        FileReader fileReader = null;
        try
        {
            // 创建字符输入流
            // 使用默认路径,即:props.txt可以成功,Text.java不能成功,src/com/java/main/Test.java可以成功
            fileReader = new FileReader("src/com/reader/FileReaderTest.java");
            
            // 创建一个长度为32的"竹筒"
            char[] cs = new char[512];
            // 用于保存实际读取的字符数
            int hasRead = 0;
            
            // 使用循环来重复"取水"过程
            while ((hasRead = fileReader.read(cs)) > 0)
            {
                // 取出水滴
                System.out.println("length = " + hasRead + "\n" + new String(cs, 0, hasRead) + "\n");
            }
            
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
