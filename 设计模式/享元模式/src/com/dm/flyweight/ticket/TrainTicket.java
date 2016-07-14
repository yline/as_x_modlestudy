package com.dm.flyweight.ticket;

import java.util.Random;

import android.util.Log;

public class TrainTicket implements Ticket
{
    private String from; // 出发地
    
    private String to; // 目的地
    
    private String bunk; // 铺位
    
    private int price; // 价格
    
    public TrainTicket(String from, String to)
    {
        this.from = from;
        this.to = to;
    }
    
    @Override
    public void showTicketInfo(String bunk)
    {
        this.bunk = bunk;
        this.price = new Random().nextInt(1000);
        Log.v("xiangyuan_tag", "TrainTicket [from=" + from + ", to=" + to + ", bunk=" + this.bunk + ", price=" + price
            + "]");
    }
}
