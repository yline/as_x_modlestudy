package tool.decimal;

import java.math.BigDecimal;

/**
 * BigDecimal   1,构造它的时候,需要精确的话,应该传入 String
 */
public class BigDecimalTest
{
    public static void main(String[] args)
    {
        System.out.println("\n********************BigDecimal****************************");
        BigDecimal bigDecimal1 = new BigDecimal("0.05");
        BigDecimal bigDecimal2 = new BigDecimal(0.05);
        BigDecimal bigDecimal3 = BigDecimal.valueOf(0.01);
        
        // 精确
        System.out.println("精确 **************************************************************");
        System.out.println("test1   -->     0.05 + 0.01 = " + bigDecimal1.add(bigDecimal3));
        System.out.println("test1   -->     0.05 - 0.01 = " + bigDecimal1.subtract(bigDecimal3));
        System.out.println("test1   -->     0.05 * 0.01 = " + bigDecimal1.multiply(bigDecimal3));
        System.out.println("test1   -->     0.05 / 0.01 = " + bigDecimal1.divide(bigDecimal3));
        
        // 不精确
        System.out.println("精确 **************************************************************");
        System.out.println("test2   -->     0.05 + 0.01 = " + bigDecimal2.add(bigDecimal3));
        System.out.println("test2   -->     0.05 - 0.01 = " + bigDecimal2.subtract(bigDecimal3));
        System.out.println("test2   -->     0.05 * 0.01 = " + bigDecimal2.multiply(bigDecimal3));
        System.out.println("test2   -->     0.05 / 0.01 = " + bigDecimal2.divide(bigDecimal3));
        
        System.out.println("精确工具类 **************************************************************");
        System.out.println("test3   -->     0.05 + 0.01 = " + Arith.add(0.05, 0.01));
        System.out.println("test3   -->     0.05 - 0.01 = " + Arith.subtract(0.05, 0.01));
        System.out.println("test3   -->     0.05 * 0.01 = " + Arith.multiply(0.05, 0.01));
        System.out.println("test3   -->     0.05 / 0.01 = " + Arith.divide(0.05, 0.01));
    }
    
    public static class Arith
    {
        private static final int DEF_DIV_SCALE = 10;
        
        private Arith()
        {
        };
        
        public static double add(double val1, double val2)
        {
            BigDecimal bigDecimal1 = BigDecimal.valueOf(val1);
            BigDecimal bigDecimal2 = BigDecimal.valueOf(val2);
            return bigDecimal1.add(bigDecimal2).doubleValue();
        }
        
        public static double subtract(double val1, double val2)
        {
            BigDecimal bigDecimal1 = BigDecimal.valueOf(val1);
            BigDecimal bigDecimal2 = BigDecimal.valueOf(val2);
            return bigDecimal1.subtract(bigDecimal2).doubleValue();
        }
        
        public static double multiply(double val1, double val2)
        {
            BigDecimal bigDecimal1 = BigDecimal.valueOf(val1);
            BigDecimal bigDecimal2 = BigDecimal.valueOf(val2);
            return bigDecimal1.multiply(bigDecimal2).doubleValue();
        }
        
        public static double divide(double val1, double val2)
        {
            BigDecimal bigDecimal1 = BigDecimal.valueOf(val1);
            BigDecimal bigDecimal2 = BigDecimal.valueOf(val2);
            return bigDecimal1.divide(bigDecimal2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }
}
