package com.udp.server;

import com.udp.server.helper.UDPHelper;

/**
 * 作为  服务端
 * @author YLine
 *
 * 2016年7月22日 下午11:15:01
 */
public class UDPReceiver implements Runnable
{
    public static final String TAG_JAVA_SERVER = "java_server -> ";
    
    private int aPort, timeout;
    
    public UDPReceiver(int aPort, int timeout)
    {
        this.aPort = aPort;
        this.timeout = timeout;
    }
    
    @Override
    public void run()
    {
        System.out.println(TAG_JAVA_SERVER + "UDPRunnable run start");
        
        while (true)
        {
            System.out.println("aPort = " + aPort + ",timeout = " + timeout);
            byte[] result = new UDPHelper().receiver(aPort, timeout);
            
            System.out.println("result = " + new String(result));
        }
    }
    
}
