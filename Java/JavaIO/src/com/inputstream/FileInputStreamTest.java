package com.inputstream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 字符流,如果进行文本内容输入输出时,首要考虑
 * 字节流,如果进行二进制内容输入输出时,首要考虑
 */
public class FileInputStreamTest
{
    public static void main(String[] args)
    {
        testFileInputStream();
    }
    
    /**
     * 一般new String的默认编码格式和运行的main.java文件的编码格式一致
     * GBK格式编码中文,如果读取字节流的长度较短,可能制度取到半个字节,导致乱码
     */
    public static void testFileInputStream()
    {
        FileInputStream fileInputStream = null;
        try
        {
            // 创建字节输入流
            // 使用默认路径,即:props.txt可以成功,Text.java不能成功,src/com/java/main/Test.java可以成功
            fileInputStream = new FileInputStream("src/com/inputstream/FileInputStreamTest.java");
            // 创建一个长度为1024的"竹筒"
            byte[] bs = new byte[1024];
            
            // 用于保存实际读取的字节数目
            int hasRead = 0;
            
            // 使用循环重复"取水"过程
            while ((hasRead = fileInputStream.read(bs)) > 0)
            {
                System.out.println("length = " + hasRead + "\n" + new String(bs, 0, hasRead, "gbk") + "\n");
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
