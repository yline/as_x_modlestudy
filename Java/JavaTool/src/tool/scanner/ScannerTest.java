package tool.scanner;

public class ScannerTest
{
    public static void main(String args[])
    {
        // 标准输入 --> 键盘输入
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        // 将 回车符 作为分隔符
        scanner.useDelimiter("\n");
        
        while (scanner.hasNext())
        {
            System.out.println("键盘输入的内容为:" + scanner.next());
        }
        
        scanner.close();
    }
    
    /**
     * 针对 long 方式
     */
    public void testLong()
    {
        // 标准输入 --> 键盘输入
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (scanner.hasNextLong())
        {
            System.out.println("键盘输入的内容为:" + scanner.nextLong());
        }
        
        scanner.close();
    }
}
