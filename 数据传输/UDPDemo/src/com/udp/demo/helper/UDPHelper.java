package com.udp.demo.helper;

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
        finally
        {
            mSendDatagramSocket.close();
        }
        
        return false;
    }
    
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
            
            mReceiverDatagramSocket.receive(mReceiverDatagramPacket);
            mReceiverDatagramSocket.setSoTimeout(timeout);
            
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
        finally
        {
            mReceiverDatagramSocket.close();
        }
        
        return null;
    }
}
