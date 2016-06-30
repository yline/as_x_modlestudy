package tool.string;

/**
 * String           序列不可变字符串
 * StringBuffer     序列可变字符串,线程安全
 * StringBuilder    序列可变字符串,线程不安全,性能略高
 */
public class StringTest
{
    public static void main(String[] args)
    {
        StringBuffer stringBuilder = new StringBuffer("hello java");
        
        // 追加字符
        // stringBuilder.append("java"); // java
        // 插入
        // stringBuilder.insert(0, "hello "); // hello java
        // 替换
        stringBuilder.replace(5, 6, ","); // hello,java
        // 删除
        stringBuilder.delete(5, 6); // hellojava
        // 反转
        stringBuilder.reverse(); // avajolleh
        // 长度
        int length = stringBuilder.length();
        
        System.out.println("length = " + length); // 9
        // 长度容量
        int capacity = stringBuilder.capacity(); // 26
        System.out.println("capacity = " + capacity);
        // 截断
        stringBuilder.setLength(5); // avajo
        // 打印
        System.out.println(stringBuilder.toString());
    }
}
