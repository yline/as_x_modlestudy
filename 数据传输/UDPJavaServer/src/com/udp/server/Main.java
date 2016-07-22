package com.udp.server;

public class Main
{
    public static void main(String[] args)
    {
        new UDPServer();
        initUDPServer();
    }
    
    private static void initUDPServer()
    {
        System.out.println(UDPRunnable.TAG_JAVA_SERVER + "initUDPServer thread start");
        new Thread(new UDPRunnable()).start();
    }
}
