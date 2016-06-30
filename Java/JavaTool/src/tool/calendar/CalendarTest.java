package tool.calendar;

import java.time.Duration;
import java.util.Calendar;

/**
 * lenient 模式               每个时间字段可接受超出它允许的范围的值（默认）
 * non-lenient 模式      每个时间字段不可接受超出它允许的范围的值
 */
public class CalendarTest
{
    public static void main(String[] args)
    {
        // Calendar 介绍
        Calendar calendar = Calendar.getInstance();
        // 年
        System.out.println("年 : " + calendar.get(Calendar.YEAR));
        
        // 月
        System.out.println("月 : " + calendar.get(Calendar.MONTH));
        
        // 日
        System.out.println("日 : " + calendar.get(Calendar.DATE));
        
        System.out.println(calendar.getTime());
        
        // 设置,年、月、日、时、分、秒
        calendar.set(2003, 10, 23, 12, 32, 23); // 设置时间后,之后获取的时间,都是设置的时间
        System.out.println(calendar.getTime());
        
        // 推一年,add功能比roll功能强大
        calendar.add(Calendar.MONTH, 10);
        System.out.println(calendar.getTime());
        
        // 推8个月
        calendar.roll(Calendar.YEAR, 1);
        System.out.println(calendar.getTime());
        
        System.out.println("\n其它时间类*****************************************************start");
        test_java_time_package();
        System.out.println("其它时间类*****************************************************end");
    }
    
    private static void test_java_time_package()
    {
        // clock
        java.time.Clock clock = java.time.Clock.systemUTC();
        System.out.println("当前时刻的毫秒数:" + clock.millis());
        System.out.println("当前时刻的毫秒数:" + System.currentTimeMillis());
        
        // duration
        Duration duration = Duration.ofSeconds(6000);
        System.out.println("6000秒 = " + duration.toMinutes() + "分");
        System.out.println("6000秒 = " + duration.toHours() + "时");
        System.out.println("6000秒 = " + duration.toDays() + "天");
        
        // instant
        System.out.println("工具类有:\n"
            + "时间类(日期、时区):Clock,Duration,Instant,LocalData,LocalTime,LocalDateTime,MonthDay,Year,YearMonth,ZonedDateTime \n"
            + "枚举类:DayOfWeek,Month");
    }
}
