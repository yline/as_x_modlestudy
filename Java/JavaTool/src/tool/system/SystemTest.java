package tool.system;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SystemTest
{
    public static void main(String[] args)
    {
        // 获取系统的所有的环境变量
        java.util.Map<String, String> envMap = System.getenv();
        
        for (String envKey : envMap.keySet())
        {
            System.out.println("envKey = " + envKey + ",value = " + envMap.get(envKey));
        }
        
        // 获取指定环境变量的值
        System.out.println(System.getenv("JAVA_HOME")); // 没有返回null
        // 获取所有的系统属性
        java.util.Properties properties = System.getProperties();
        // 将所有的系统属性保存到props.txt文件中
        try
        {
            // 当前路径下,refresh工程就能看到
            properties.store(new FileOutputStream("props.txt"), "System Properties");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // 输出特定的系统属性 result =  Windows 7
        System.out.println(System.getProperty("os.name"));
    }
    
    public void judgeEqual()
    {
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println(s1.hashCode() + "---" + s2.hashCode());
        System.out.println(System.identityHashCode(s1) + "---" + System.identityHashCode(s2));
        
        String s3 = "hello";
        String s4 = "hello";
        System.out.println(s3.hashCode() + "---" + s4.hashCode());
        System.out.println(System.identityHashCode(s3) + "---" + System.identityHashCode(s4));
    }
}
