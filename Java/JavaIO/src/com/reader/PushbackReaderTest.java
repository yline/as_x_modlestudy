package com.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class PushbackReaderTest
{
    /**
     * 找出"输入内容"字符串
     * 并打印该字符串之前的内容
     * 
     * 注意: 这个案例中,缓冲区的大小,必须为每次读取字符的大小的两倍
     */
    public static void main(String[] args)
    {
        FileReader fileReader = null;
        PushbackReader pushbackReader = null;
        try
        {
            fileReader = new FileReader("src/com/reader/PushbackReaderTest.java");
            pushbackReader = new PushbackReader(fileReader, 64);
            
            char[] cs = new char[32];
            
            // 用于保存上次读取的字符串内容
            String lastContent = "";
            int hasRead = 0;
            
            // 读取文件内容,下面的问题直接可以忽略
            while ((hasRead = pushbackReader.read(cs)) > 0)
            {
                // 将读取的内容转换成字符串
                String content = new String(cs, 0, hasRead);
                int targetIndex = 0;
                
                // 将上次读取的字符串和本次读取的字符串拼起来,为了防止把"输入内容"截断,导致程序的错误
                // 查看是否包含目标字符串,如果包含目标字符串
                if ((targetIndex = (lastContent + content).indexOf("输入内容")) > 0)
                {
                    // 将本次内容和上次内容一起推回缓冲区
                    pushbackReader.unread((lastContent + content).toCharArray());
                    // 重新定义一个长度为targetIndex的char数组
                    if (targetIndex > 32)
                    {
                        cs = new char[targetIndex];
                    }
                    
                    // 再次读取指定长度的内容
                    pushbackReader.read(cs, 0, targetIndex);
                    
                    // 打印读取的内容
                    System.out.println(new String(cs, 0, targetIndex));
                    System.exit(0);
                }
                else
                {
                    // 打印上次读取的内容
                    System.out.println(lastContent);
                    // 将本次内容设为上次读取的内容
                    lastContent = content;
                }
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
                pushbackReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
