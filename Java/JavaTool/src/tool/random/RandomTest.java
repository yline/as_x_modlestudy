package tool.random;

import java.util.Random;

/**
 * Random               一般使用
 * ThreadLocalRandom    多线程环境下使用
 * simple introduction
 */
public class RandomTest
{
    public static void main(String[] args)
    {
        // 演示  通常使用
        Random random = new Random();
        
        byte[] bs = new byte[16];
        random.nextBytes(bs);
        String result = "";
        for (byte b : bs)
        {
            result += b + " | ";
        }
        System.out.println("bs.toString():   " + result);
        
        System.out.println("random.nextBoolean():   " + random.nextBoolean());
        
        // 0.0 - 1.0
        System.out.println("random.nextDouble():   " + random.nextDouble());
        
        // 0.0 - 1.0
        System.out.println("random.nextFloat():   " + random.nextFloat());
        
        // 平均值为0    标准差为1.0 的伪高斯数
        System.out.println("random.nextGaussian():   " + random.nextGaussian());
        
        // int 范围内 数值
        System.out.println("random.nextInt():   " + random.nextInt());
        
        System.out.println("random.nextInt(26):   " + random.nextInt(26));
        
        // long 范围内 数值
        System.out.println("random.nextLong():   " + random.nextLong());
        
        //*****************************************************************
        // 演示   Random (种子固定时,伪随机)
        System.out.println("*****************************************************************");
        Random random2 = new Random(50); // 50就是种子,而不输入时,种子是当前时间
        System.out.println("random2.nextBoolean():   " + random2.nextBoolean());
        // 0.0 - 1.0
        System.out.println("random2.nextDouble():   " + random2.nextDouble());
        // 0.0 - 1.0
        System.out.println("random2.nextFloat():   " + random2.nextFloat());
        // int 范围内 数值
        System.out.println("random2.nextInt():   " + random2.nextInt());
        
        System.out.println("*****************************************************************");
        Random random3 = new Random(50); // 50就是种子,而不输入时,种子是当前时间
        System.out.println("random3.nextBoolean():   " + random3.nextBoolean());
        // 0.0 - 1.0
        System.out.println("random3.nextDouble():   " + random3.nextDouble());
        // 0.0 - 1.0
        System.out.println("random3.nextFloat():   " + random3.nextFloat());
        // int 范围内 数值
        System.out.println("random3.nextInt():   " + random3.nextInt());
    }
}
