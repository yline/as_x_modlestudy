package com.dm.flyweight.ticketfactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dm.flyweight.activity.MainApplication;
import com.dm.flyweight.ticket.Ticket;
import com.dm.flyweight.ticket.TrainTicket;
import com.yline.log.LogFileUtil;

/**
 * 享元模式, 的 公开部分
 * 
 * 中间利用了 HashMap 做内存缓存
 */
public class TicketFactory
{
    private static Map<String, Ticket> sTicketMap = new ConcurrentHashMap<String, Ticket>();
    
    //	public static Ticket getTicket(String from, String to){
    //		return new TrainTicket(from, to);
    //	}
    
    public static Ticket getTicket(String from, String to)
    {
        String key = from + "-" + to;
        if (sTicketMap.containsKey(key))
        {
            LogFileUtil.v(MainApplication.TAG, "TicketFactory -> 使用缓存");
            return sTicketMap.get(key);
        }
        else
        {
            LogFileUtil.v(MainApplication.TAG, "TicketFactory -> 创建对象," + key);
            Ticket ticket = new TrainTicket(from, to);
            sTicketMap.put(key, ticket);
            return ticket;
        }
    }
    
}
