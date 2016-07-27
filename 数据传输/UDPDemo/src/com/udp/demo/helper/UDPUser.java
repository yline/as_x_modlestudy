package com.udp.demo.helper;

import com.udp.demo.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 不能使用单例模式,因为两个方法会异步被阻塞
 * @author YLine
 *
 * 2016年7月27日 下午9:41:17
 */
public class UDPUser
{
    private static final Handler sHandler = new InternalHandler();
    
    private UDPHelper mUdpHelper = new UDPHelper();
    
    /**
     * @param content 发送的数据
     * @param host 服务端host
     * @param port 服务端端口
     */
    public void sendMessage(final String content, final String host, final int port)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                LogFileUtil.v(MainApplication.TAG, "content = " + content + ",host = " + host + ",port = " + port);
                
                boolean result = mUdpHelper.send(content.getBytes(), host, port);
                
                LogFileUtil.v(MainApplication.TAG, (result ? "success" : "failed"));
            }
        }).start();
    }
    
    public void receiverMessage(final int aPort, final int timeout)
    {
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                while (true)
                {
                    LogFileUtil.v(MainApplication.TAG, "aPort = " + aPort + ",timeout = " + timeout);
                    
                    byte[] result = mUdpHelper.receiver(aPort, timeout);
                    LogFileUtil.v(MainApplication.TAG, (null != result ? new String(result) : "failed"));
                }
            }
        }).start();
    }
    
    final static class InternalHandler extends Handler
    {
        public InternalHandler()
        {
            super(Looper.getMainLooper());
        }
        
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
        }
    }
}
