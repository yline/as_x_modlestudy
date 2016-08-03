package com.utils;

import java.util.Locale;

/**
 * 时间单位转换,转换类
 * @author YLine
 *
 * 2016年8月3日 下午7:50:16
 */
public class TimeConvertUtil
{
    /**
     * @param time 毫秒  12223000
     * @return 3:23:43
     */
    public static String ms2FormatMinute(int time)
    {
        int seconds = time / 1000;
        if (seconds < 3600)
        {
            return String.format(Locale.CHINA, "%02d:%02d", seconds / 60, seconds % 60);
        }
        else
        {
            return String.format(Locale.CHINA, "%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        }
    }
}
