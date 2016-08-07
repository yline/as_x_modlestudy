package com.udp.demo.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.udp.demo.activity.MainApplication;
import com.yline.log.LogFileUtil;

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
            
            LogFileUtil.v(MainApplication.TAG,
                "UDPHelper -> before send mSendDatagramSocket = " + mSendDatagramSocket + ",mSendDatagramPacket = "
                    + mSendDatagramPacket + ",data = " + new String(data) + ",inetAddress = " + inetAddress + ",port = "
                    + port);
            mSendDatagramSocket.send(mSendDatagramPacket);
            
            return true;
        }
        catch (SocketException e)
        {
            LogFileUtil.e(MainApplication.TAG, "UDPHelper -> send SocketException", e);
        }
        catch (UnknownHostException e)
        {
            LogFileUtil.e(MainApplication.TAG, "UDPHelper -> send UnknownHostException", e);
        }
        catch (IOException e)
        {
            LogFileUtil.e(MainApplication.TAG, "UDPHelper -> send IOException", e);
        }
        
        return false;
    }
    
    /**
     * @param aPort  本地接收用的端口
     * @param timeout 超时时间
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
            
            mReceiverDatagramSocket.receive(mReceiverDatagramPacket);
            // mReceiverDatagramSocket.setSoTimeout(timeout);
            
            /** 除去1024无用的数据 */
            byte[] result = new byte[mReceiverDatagramPacket.getLength()];
            System.arraycopy(mReceiverDatagramPacket.getData(), 0, result, 0, mReceiverDatagramPacket.getLength());
            
            return result;
        }
        catch (SocketException e)
        {
            LogFileUtil.e(MainApplication.TAG, "UDPHelper -> receiver SocketException", e);
        }
        catch (IOException e)
        {
            LogFileUtil.e(MainApplication.TAG, "UDPHelper -> receiver IOException", e);
        }
        
        return null;
    }
}
