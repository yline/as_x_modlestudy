package com.xml.parse.pull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.xml.parse.ParseInfo;

import android.util.Xml;

/**
 * simple introduction
 *
 * @author YLine 2016-5-1 -> 上午9:18:50
 * @version
 */
public class ParseUtil
{
    public static final String PARSE_FORMATTER = "utf-8";
    
    public static List<ParseInfo> getWeatherInfo(InputStream fis)
        throws Exception
    {
        /**
         * DOM解析，加载内存，生成一个树状结构；消耗内存比较大
         * SAX解析，基于事件的方式，速度快，效率高;不能倒退
         * pull解析，和SAX解析类似，语法相对方便理解 
         */
        XmlPullParser parser = Xml.newPullParser();
        
        //初始化解析器  为 UTF-8 格式
        parser.setInput(fis, ParseUtil.PARSE_FORMATTER);
        
        List<ParseInfo> weatherInfo = null;
        ParseInfo info = null;
        /**
         * pull解析的过程：
         * 初始化的时候，定义了一个指针，指向了开头;  需要方法得到该指针
         * <> 是 一个个的TAG, 中间的是 文本
         */
        int type = parser.getEventType(); //得到指针
        
        //至上而下解析
        while (type != XmlPullParser.END_DOCUMENT)
        {
            switch (type)
            {
                case XmlPullParser.START_TAG:
                    if ("infos".equals(parser.getName()))
                    {
                        //解析到全局开始的标签
                        weatherInfo = new ArrayList<ParseInfo>();
                    }
                    else if ("city".equals(parser.getName()))
                    {
                        info = new ParseInfo();
                        String idStr = parser.getAttributeValue(0);
                        info.setId(Integer.parseInt(idStr)); //转化接受到的int类型
                    }
                    else if ("temp".equals(parser.getName()))
                    {
                        String temp = parser.nextText();
                        info.setTemp(temp);
                    }
                    else if ("weather".equals(parser.getName()))
                    {
                        String weather = parser.nextText();
                        info.setWeather(weather);
                    }
                    else if ("wind".equals(parser.getName()))
                    {
                        String wind = parser.nextText();
                        info.setWind(wind);
                    }
                    else if ("name".equals(parser.getName()))
                    {
                        String name = parser.nextText();
                        info.setName(name);
                    }
                    else if ("pm".equals(parser.getName()))
                    {
                        String pm = parser.nextText();
                        info.setPm(pm);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("city".equals(parser.getName()))
                    {
                        //一个城市的信息 已经处理完毕了
                        weatherInfo.add(info); //将一个城市的信息加载到总信息中
                        info = null;
                    }
                    break;
            }
            type = parser.next(); //更改指针的值
        }
        return weatherInfo; //返回值
    }
}
