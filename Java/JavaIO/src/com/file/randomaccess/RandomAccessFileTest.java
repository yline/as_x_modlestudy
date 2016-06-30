package com.file.randomaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest
{
    public static void main(String[] args)
    {
        testInsert();
        testWriter();
        testRead();
    }
    
    public static void testRead()
    {
        RandomAccessFile randomAccessFile = null;
        try
        {
            randomAccessFile = new RandomAccessFile("RandomAccessFile.txt", "r");
            
            // 获取RandomAccessFile对象文件指针的位置,初始位置为0
            // 移动RandomAccessFile的文件记录指针位置
            randomAccessFile.seek(300);
            
            byte[] bs = new byte[1024];
            // 用于保存实际读取的字节数
            int hasRead = 0;
            while ((hasRead = randomAccessFile.read(bs)) > 0)
            {
                // 取出"竹筒"中的字节,将字节转换成字符串输出
                System.out.println(new String(bs, 0, hasRead));
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
                randomAccessFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void testWriter()
    {
        RandomAccessFile randomAccessFile = null;
        try
        {
            randomAccessFile = new RandomAccessFile("RandomAccessFile.txt", "rw");
            
            // 移动到文件的最后
            randomAccessFile.seek(randomAccessFile.length());
            
            randomAccessFile.write("追加的内容: content \r\n".getBytes());
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
                randomAccessFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 向指定文件、指定位置插入内容的function
     */
    public static void testInsert()
    {
        String filename = "RandomAccessFile.txt";
        long position = 300;
        String insertContent = "我是插入的内容";
        
        File tmp = null;
        try
        {
            tmp = File.createTempFile("tmp", null);
            
            // 退出程序时,删除临时文件
            tmp.deleteOnExit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        RandomAccessFile randomAccessFile = null;
        FileOutputStream tmpOutputStream = null;
        FileInputStream tmpInputStream = null;
        
        try
        {
            randomAccessFile = new RandomAccessFile(filename, "rw");
            
            // 使用临时文件,保存插入点后的数据
            tmpOutputStream = new FileOutputStream(tmp);
            tmpInputStream = new FileInputStream(tmp);
            
            // 定位到插入点
            randomAccessFile.seek(position);
            
            // =====================将插入点后的内容读入临时文件中保存=========================
            byte[] bs = new byte[64];
            int hasRead = 0;
            while ((hasRead = randomAccessFile.read(bs)) > 0)
            {
                // 将读取的数据 写入临时文件
                tmpOutputStream.write(bs, 0, hasRead);
            }
            
            // =====================用于插入内容=====================================
            // 将文件记录指针重新定位到position位置
            randomAccessFile.seek(position);
            // 追加需要插入的内容
            randomAccessFile.write(insertContent.getBytes());
            // 追加临时文件中的内容
            while ((hasRead = tmpInputStream.read(bs)) > 0)
            {
                randomAccessFile.write(bs, 0, hasRead);
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
                randomAccessFile.close();
                tmpOutputStream.close();
                tmpInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
    }
}
