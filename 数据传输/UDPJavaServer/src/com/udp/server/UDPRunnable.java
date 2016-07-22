package com.udp.server;

/**
 * 作为  服务端
 * @author YLine
 *
 * 2016年7月22日 下午11:15:01
 */
public class UDPRunnable implements Runnable
{
    public static final String TAG_JAVA_SERVER = "java_server -> ";
    
    private long mCurrentTime;
    
    public UDPRunnable()
    {
        mCurrentTime = System.currentTimeMillis();
    }
    
    @Override
    public void run()
    {
        System.out.println(TAG_JAVA_SERVER + "UDPRunnable run start");
        
        while (true)
        {
            log("UDPRunnable is running");
        }
    }
    
    private void log(String content)
    {
        // 10 s 打印一次日志
        if (System.currentTimeMillis() - mCurrentTime > 10000)
        {
            System.out.println(TAG_JAVA_SERVER + content);
            mCurrentTime = System.currentTimeMillis();
        }
    }
}
