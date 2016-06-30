package tool.pattern;

/**
 * 正则
 * simple introduction
 */
public class PatternTest
{
    public static void main(String[] args)
    {
        String str = "hello,java!";
        
        // 贪婪模式,一直匹配下去,直到无法匹配为止; X表达式,出现0次或多次
        // (\w)[匹配所有的单词字符,包括0-9所有数字、26个英文字母和下画线(_)]
        // result   *,java!
        System.out.println(str.replaceFirst("\\w*", "*"));
        
        // 勉强模式,只会匹配最少的字符; X表达式,出现0次或多次
        // (\w)[匹配所有的单词字符,包括0-9所有数字、26个英文字母和下画线(_)]
        // result   *hello,java!
        System.out.println(str.replaceFirst("\\w*?", "*"));
        
        // 占有模式,只会匹配最少的字符; X表达式,出现0次或多次
        // (\w)[匹配所有的单词字符,包括0-9所有数字、26个英文字母和下画线(_)]
        // result   *hello,java!
        System.out.println(str.replaceFirst("\\w*+", "*"));
    }
}
