package tool.runtime;

import java.io.IOException;

/**
 * 代表java程序运行时环境
 * 不能创建,只能获取
 * 
 * 并且可以直接单独启动一个进程来运行操作系统的命令
 */
public class RuntimeTest
{
    public static void main(String[] args)
    {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("处理器数量:   " + runtime.availableProcessors());
        System.out.println("空闲内存数:   " + runtime.freeMemory());
        System.out.println("总内存数:     " + runtime.totalMemory());
        System.out.println("可用最大内存数:" + runtime.maxMemory());
        
        // 打开记事本
        try
        {
            runtime.exec("notepad.exe");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
