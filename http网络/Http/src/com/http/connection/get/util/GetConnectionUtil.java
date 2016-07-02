package com.http.connection.get.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yline.log.LogFileUtil;

import android.os.Handler;

/**
 * 请求的URL,区分大小写
 * @author YLine
 *
 * 2016年7月2日 下午12:04:15
 */
public class GetConnectionUtil
{
    private static final String TAG = "connection_get";
    
    private static final int HTTPCONNECT_SUCCESS_CODE = 200; // 网络请求,正确返回码
    
    private static final int CONNNECT_TIMEOUT = 5000; // 连接超时时间 ms 
    
    private static final int READ_TIMEOUT = 5000; // 读取超时时间 ms
    
    private static Handler mHandler = new Handler();
    
    public static void doLocal(String ip, String endStr, final GetConnectionCallback callback)
    {
        String httpUrl = String.format("http://%s:8080/WebServletJspFirst/%s", ip, endStr);
        doGetAsyn(httpUrl, callback);
    }
    
    private static void doGetAsyn(final String httpUrl, final GetConnectionCallback callback)
    {
        LogFileUtil.v(TAG, "doGetAsyn -> in -> " + httpUrl);
        
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                URL url = null;
                HttpURLConnection httpURLConnection = null;
                
                BufferedReader bufferedReader = null;
                
                StringBuffer result = new StringBuffer();
                
                try
                {
                    url = new URL(httpUrl); // 创建一个URL对象
                    LogFileUtil.v(TAG, "doGetAsyn -> new url over");
                    
                    // 调用URL的openConnection()方法,获取HttpURLConnection对象
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    
                    // HttpURLConnection默认就是用GET发送请求,因此也可省略
                    httpURLConnection.setRequestMethod("GET");
                    initHttpConnection(httpURLConnection);
                    LogFileUtil.v(TAG, "doGetAsyn -> initHttpConnection over");
                    /*
                    // 在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
                    // conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
                    httpURLConnection.connect();
                    
                    // 调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
                    InputStream inputStream = httpURLConnection.getInputStream();
                    
                    // 将InputStream转换成byte数组,getBytesByInputStream会关闭输入流
                    byte[] responseBody = getBytesFromInputStream(inputStream);
                    
                    // 获得响应头
                    String responseHeader = getResponseHeader(httpURLConnection);
                    */
                    int responseCode = httpURLConnection.getResponseCode();
                    LogFileUtil.i(TAG, "doGetAsyn -> responseCode = " + responseCode);
                    
                    if (HTTPCONNECT_SUCCESS_CODE == responseCode) // 连接成功
                    {
                        bufferedReader =
                            new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                        String line = null;
                        while (null != (line = bufferedReader.readLine())) // 即:有数据时,一直在读取
                        {
                            result.append(line);
                        }
                        
                        handleSuccess(callback, result.toString());
                    }
                    else
                    {
                        LogFileUtil.e(TAG, "doGetAsyn -> responseCode error");
                        handleError(callback, new Exception("doGetAsyn -> responseCode error code = " + responseCode));
                    }
                }
                catch (MalformedURLException e)
                {
                    LogFileUtil.e(TAG, "doGetAsyn -> MalformedURLException", e);
                    handleError(callback, e);
                }
                catch (ProtocolException e)
                {
                    LogFileUtil.e(TAG, "doGetAsyn -> ProtocolException", e);
                    handleError(callback, e);
                }
                catch (IOException e)
                {
                    LogFileUtil.e(TAG, "doGetAsyn -> IOException", e);
                    handleError(callback, e);
                }
                finally
                {
                    if (null != bufferedReader)
                    {
                        try
                        {
                            bufferedReader.close();
                        }
                        catch (IOException e)
                        {
                            LogFileUtil.e(TAG, "doGetAsyn -> close IOException", e);
                            handleError(callback, e);
                        }
                    }
                    if (null != httpURLConnection)
                    {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
    
    /**
     * http 链接  设置
     * @param connection 
     */
    private static HttpURLConnection initHttpConnection(HttpURLConnection connection)
    {
        // 用setRequestProperty方法设置多个自定义的请求头:action，用于后端判断
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        
        // 禁用网络缓存
        connection.setUseCaches(false);
        // 定制
        connection.setDoOutput(true); // 设置此方法,允许向服务器输出内容
        
        // HttpURLConnection默认也支持从服务端读取结果流，因此也可省略
        connection.setDoInput(true);
        connection.setReadTimeout(READ_TIMEOUT); // 设置读取超时为5秒
        connection.setConnectTimeout(CONNNECT_TIMEOUT); // 设置连接网络超时为5秒
        
        /** 为了日志 */
        // 获取请求头
        String requestHeader = getRequestHeader(connection);
        LogFileUtil.v(TAG, "doGetAsyn -> initHttpConnection requestHeader = " + requestHeader);
        
        return connection;
    }
    
    /**
     * 读取请求头
     * @param connection
     * @return null if connection is null
     */
    private static String getRequestHeader(HttpURLConnection connection)
    {
        if (null != connection)
        {
            Map<String, List<String>> requestHeaderMap = connection.getRequestProperties();
            Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
            StringBuilder requestHeaderStringBuilder = new StringBuilder();
            while (requestHeaderIterator.hasNext())
            {
                String requestHeaderKey = requestHeaderIterator.next();
                String requestHeaderValue = connection.getRequestProperty(requestHeaderKey);
                requestHeaderStringBuilder.append(requestHeaderKey);
                requestHeaderStringBuilder.append(":");
                requestHeaderStringBuilder.append(requestHeaderValue);
                requestHeaderStringBuilder.append("\n");
            }
            return requestHeaderStringBuilder.toString();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 读取响应头
     * @param connection
     * @return
     */
    private static String getResponseHeader(HttpURLConnection connection)
    {
        if (null != connection)
        {
            Map<String, List<String>> responseHeaderMap = connection.getHeaderFields();
            int size = responseHeaderMap.size();
            StringBuilder responseHeaderStringBuilder = new StringBuilder();
            for (int i = 0; i < size; i++)
            {
                String responseHeaderKey = connection.getHeaderFieldKey(i);
                String responseHeaderValue = connection.getHeaderField(i);
                responseHeaderStringBuilder.append(responseHeaderKey);
                responseHeaderStringBuilder.append(":");
                responseHeaderStringBuilder.append(responseHeaderValue);
                responseHeaderStringBuilder.append("\n");
            }
            return responseHeaderStringBuilder.toString();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 从InputStream中读取数据，转换成byte数组，最后关闭InputStream
     * @param inputStream
     * @return null if error
     * @throws IOException IO流错误
     */
    private byte[] getBytesFromInputStream(InputStream inputStream)
        throws IOException
    {
        byte[] bytes = null;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = bufferedInputStream.read(buffer)) > 0)
        {
            bufferedOutputStream.write(buffer, 0, length);
        }
        bufferedOutputStream.flush();
        
        bytes = byteArrayOutputStream.toByteArray();
        
        bufferedOutputStream.close();
        bufferedInputStream.close();
        
        return bytes;
    }
    
    /**
     * @param data
     * @return null if UnsupportedEncodingException happened 
     */
    private String getStringFromBytes(byte[] data)
    {
        String tempStr = null;
        try
        {
            tempStr = new String(data, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LogFileUtil.v(TAG, "UnsupportedEncodingException -> ", e);
        }
        return tempStr;
    }
    
    /**
     * 回调,抛出异常到主线程
     * @param callback
     * @param e
     */
    private static void handleError(final GetConnectionCallback callback, final Exception e)
    {
        mHandler.post(new Runnable()
        {
            
            @Override
            public void run()
            {
                if (null != callback)
                {
                    callback.onError(e);
                }
            }
        });
    }
    
    /**
     * 回调,抛出网络请求结果到主线程
     * @param callback
     * @param result
     */
    private static void handleSuccess(final GetConnectionCallback callback, final String result)
    {
        mHandler.post(new Runnable()
        {
            
            @Override
            public void run()
            {
                if (null != callback)
                {
                    callback.onSuccess(result);
                }
            }
        });
    }
    
    public interface GetConnectionCallback
    {
        /**
         * 请求成功,返回结果
         * @param result
         */
        void onSuccess(String result);
        
        /**
         * 网络错误
         * @param e
         */
        void onError(Exception e);
    }
}
