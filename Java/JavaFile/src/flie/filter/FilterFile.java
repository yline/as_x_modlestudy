package flie.filter;

import java.io.File;
import java.io.FilenameFilter;

public class FilterFile
{
    public static void main(String[] args)
    {
        System.out.println("\n\n**************当前目录下的文件过滤******************");
        new FilterFile().getFilterFileName();
    }
    
    public void getFilterFileName()
    {
        File file = new File(".");
        // 使用Lambda表达式(目标类型为FilenameFilter)实现文件过滤器
        // 如果文件名以".java"结尾,或者文件对应一个路径,则返回true
        String[] nameList = file.list(new FilenameFilter()
        {
            public boolean accept(File dir, String filename)
            {
                return filename.endsWith(".java") || new File(filename).isDirectory();
            }
        });
        
        for (String string : nameList)
        {
            System.out.println(string);
        }
    }
}
