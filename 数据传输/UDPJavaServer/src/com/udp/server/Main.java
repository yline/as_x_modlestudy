package com.udp.server;

public class Main
{
    public static void main(String[] args)
    {
        // send
        final String msg = "hello world";
        // final String serverIp = "0.0.0.0"; // 广播形式发出去
        final String serverIp = "255.255.255.255"; // 广播形式发出去
        final int serverPort = 2002;
        
        new UDPSender().send(msg, serverIp, serverPort);
        
        // receiver
        final int aPort = 1001;
        final int timeout = 0;
        
        initUDPServer(aPort, timeout);
    }
    
    private static void initUDPServer(int aPort, int timeout)
    {
        System.out.println(UDPReceiver.TAG_JAVA_SERVER + "initUDPServer thread start");
        new Thread(new UDPReceiver(aPort, timeout)).start();
    }
}
