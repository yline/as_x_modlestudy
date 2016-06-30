package flie.create;

import java.io.File;
import java.io.IOException;

public class CreateFile
{
    public static void main(String[] args)
    {
        CreateFile fileNew = new CreateFile();
        fileNew.getSystemPath();
        fileNew.getRootPath();
        fileNew.newFile();
    }
    
    public void newFile()
    {
        // 以当前路径
        java.io.File file = new File(".");
        
        // 直接获取文件名  result = .
        System.out.println(file.getName());
        // 获取相对路径下的父路径  result = null
        System.out.println(file.getParent());
        // 获取绝对路径   result = D:\Workspace_33066\ZipJava\.
        System.out.println(file.getAbsoluteFile());
        // 获取绝对路径下的父路径
        System.out.println(file.getAbsoluteFile().getParent());
        
        try
        {
            // 在当前路径下创建一个临时文件
            File tmpFile = File.createTempFile("aaa", ".txt", file);
            // 当JVM退出时,删除该文件
            tmpFile.deleteOnExit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        // 以系统当前时间作为新文件名来创建新文件
        File newFile = new File(System.currentTimeMillis() + ".txt");
        System.out.println("newFlie 对象是否存在:" + newFile.exists());
        
        try
        {
            // 以指定newFile对象创建一个文件
            newFile.createNewFile();
            
            // 以newFile对象来创建一个目录,因为newFile已经存在
            // 所以下面的方法返回false,即无法创建该目录
            System.out.println("newFlie 创建目录结果:" + newFile.mkdir());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void getSystemPath()
    {
        // 以当前路径
        java.io.File file = new File(".");
        
        // 使用list()方法列出当前路径下的所有文件和路径
        String[] fileList = file.list();
        System.out.println("===========当前路径下所有文件和路径如下============");
        for (String string : fileList)
        {
            System.out.println(string);
        }
    }
    
    public void getRootPath()
    {
        System.out.println("===========系统所有根路径如下============");
        File[] roots = File.listRoots();
        for (File root : roots)
        {
            System.out.println(root);
        }
    }
}
