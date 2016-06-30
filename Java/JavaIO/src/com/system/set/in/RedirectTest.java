package com.system.set.in;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * 结果输入的出现在了文件中
 * 
 * 重定向标准输入,标准输出
 * 现象：生成了RedirectInstance.java文件
 */
public class RedirectTest
{
    public static void main(String[] args)
    {
        testSetOut();
        testSetIn();
    }
    
    public static void testSetOut()
    {
        // 一次性创建PrintStream输出流
        try
        {
            // 写入的文件
            PrintStream printStream = new PrintStream("RedirectInstance.txt");
            
            // 将标准输出 重定向到ps输出流
            System.setOut(printStream);
            
            System.out.println("===========================普通字符串===============================");
            System.out.println(new RedirectTest());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void testSetIn()
    {
        FileInputStream fileInputStream = null;
        
        Scanner scanner = null;
        try
        {
            fileInputStream = new FileInputStream("src/com/system/set/in/RedirectTest.java");
            
            // 将标准输入 重定向到 fis输入流
            System.setIn(fileInputStream);
            
            // 使用System.in 创建Scanner对象,用于获取标准输入
            scanner = new Scanner(System.in);
            
            // 将回车符作为分隔符
            scanner.useDelimiter("\n");
            
            while (scanner.hasNext())
            {
                // 输出输入项
                System.out.println("键盘输入的内容是:" + scanner.next());
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            scanner.close();
            
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
