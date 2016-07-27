package com.udp.server.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 负责提供工具类
 * @author YLine
 *
 * 2016年7月23日 上午12:02:30
 */
public class UDPHelper
{
    private DatagramSocket mSendDatagramSocket;
    
    private DatagramPacket mSendDatagramPacket;
    
    private DatagramSocket mReceiverDatagramSocket;
    
    private DatagramPacket mReceiverDatagramPacket;
    
    /**
     * @param data 发送的数据
     * @param host 服务端host
     * @param port 服务端端口
     * @return
     */
    public boolean send(byte[] data, String host, int port)
    {
        try
        {
            if (null == mSendDatagramSocket)
            {
                mSendDatagramSocket = new DatagramSocket();
            }
            
            InetAddress inetAddress = InetAddress.getByName(host);
            
            if (null == mSendDatagramPacket)
            {
                mSendDatagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
            }
            
            mSendDatagramSocket.send(mSendDatagramPacket);
            
            return true;
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * @param aPort  本地接收用的端口
     * @param timeout 超时时间; if 0 无限超时
     * @return
     */
    public byte[] receiver(int aPort, int timeout)
    {
        try
        {
            if (null == mReceiverDatagramSocket)
            {
                mReceiverDatagramSocket = new DatagramSocket(aPort);
            }
            
            byte data[] = new byte[2048];
            if (null == mReceiverDatagramPacket)
            {
                mReceiverDatagramPacket = new DatagramPacket(data, data.length);
            }
            
            System.out.println("receive waiting");
            mReceiverDatagramSocket.receive(mReceiverDatagramPacket);
            // mReceiverDatagramSocket.setSoTimeout(timeout);
            
            /** 除去1024无用的数据 */
            byte[] result = new byte[mReceiverDatagramPacket.getLength()];
            System.arraycopy(mReceiverDatagramPacket.getData(), 0, result, 0, mReceiverDatagramPacket.getLength());
            
            return result;
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
}
