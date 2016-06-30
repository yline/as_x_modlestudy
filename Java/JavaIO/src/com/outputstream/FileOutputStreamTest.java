package com.outputstream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * 现象：生成了两个文件,刷新工程就可以看到
 */
public class FileOutputStreamTest
{
    
    public static void main(String[] args)
    {
        testPrintStream();
        
        testFileOutputStream();
    }
    
    /**
     * 输出的只是一个byte
     */
    public static void testPrintStream()
    {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try
        {
            fileOutputStream = new FileOutputStream("FileOutputStream.txt");
            
            printStream = new PrintStream(fileOutputStream);
            
            printStream.println("普通字符串");
            // 直接 输出 对象(输出的只是一个byte)
            printStream.println(new FileOutputStreamTest());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            printStream.close();
        }
    }
    
    public static void testFileOutputStream()
    {
        FileInputStream fileInputStream = null;
        
        FileOutputStream fileOutputStream = null;
        try
        {
            fileInputStream = new FileInputStream("src/com/outputstream/FileOutputStreamTest.java");
            
            fileOutputStream = new FileOutputStream("FileOutputStream.txt");
            
            byte[] bs = new byte[1024];
            int hasRead = 0;
            while ((hasRead = fileInputStream.read(bs)) > 0)
            {
                fileOutputStream.write(bs, 0, hasRead);
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
            
            try
            {
                fileOutputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
