package com.https.help.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.https.connection.post.bean.PostTestBean;
import com.yline.log.LogFileUtil;

public class HttpsHelperUtils
{
    private static final String TAG = "HttpsHelperUtils";
    
    /**
     * 读取请求头
     * @param httpsURLConnection
     * @return null if httpsURLConnection is null
     */
    public static String getHttpsRequestHeader(HttpsURLConnection httpsURLConnection)
    {
        if (null != httpsURLConnection)
        {
            Map<String, List<String>> requestHeaderMap = httpsURLConnection.getRequestProperties();
            Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
            StringBuilder requestHeaderStringBuilder = new StringBuilder();
            while (requestHeaderIterator.hasNext())
            {
                String requestHeaderKey = requestHeaderIterator.next();
                String requestHeaderValue = httpsURLConnection.getRequestProperty(requestHeaderKey);
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
     * @param httpsURLConnection
     * @return null if httpsURLConnection is null
     */
    public static String getHttpsResponseHeader(HttpsURLConnection httpsURLConnection)
    {
        if (null != httpsURLConnection)
        {
            Map<String, List<String>> responseHeaderMap = httpsURLConnection.getHeaderFields();
            int size = responseHeaderMap.size();
            StringBuilder responseHeaderStringBuilder = new StringBuilder();
            for (int i = 0; i < size; i++)
            {
                String responseHeaderKey = httpsURLConnection.getHeaderFieldKey(i);
                String responseHeaderValue = httpsURLConnection.getHeaderField(i);
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
     * 直接从流中,读取字符串
     * @param inputStream
     * @return "" if exception happened
     */
    public String getStringFromIStreamEasy(InputStream inputStream)
    {
        BufferedReader bufferedReader = null;
        StringBuffer result = new StringBuffer();
        
        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            while (null != (line = bufferedReader.readLine())) // 即:有数据时,一直在读取
            {
                result.append(line);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            LogFileUtil.e(TAG, "getStringFromIStreamEasy -> UnsupportedEncodingException", e);
        }
        catch (IOException e)
        {
            LogFileUtil.e(TAG, "getStringFromIStreamEasy -> IOException", e);
        }
        
        return result.toString();
    }
    
    /**
     * 从InputStream中读取数据，转换成byte数组，最后关闭InputStream
     * @param inputStream
     * @return null if error
     */
    public byte[] getBytesFromIStream(InputStream inputStream)
    {
        byte[] bytes = null;
        
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        
        bufferedInputStream = new BufferedInputStream(inputStream);
        byteArrayOutputStream = new ByteArrayOutputStream();
        bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        
        try
        {
            byte[] buffer = new byte[1024];
            int tempLength = 0;
            while ((tempLength = bufferedInputStream.read(buffer)) > 0) // 循环读取
            {
                bufferedOutputStream.write(buffer, 0, tempLength);
            }
            bufferedOutputStream.flush();
            
            bytes = byteArrayOutputStream.toByteArray();
        }
        catch (IOException e)
        {
            LogFileUtil.e(TAG, "getBytesFromIStream -> IOException", e);
        }
        finally
        {
            try
            {
                bufferedOutputStream.close();
                bufferedInputStream.close();
                byteArrayOutputStream.close();
            }
            catch (IOException e)
            {
                LogFileUtil.e(TAG, "getBytesFromIStream -> close IOException", e);
            }
        }
        
        return bytes;
    }
    
    /**
     * @param json
     * @return null if exception
     * @throws UnsupportedEncodingException
     */
    public static byte[] getByteFromString(String json)
        throws UnsupportedEncodingException
    {
        return json.getBytes("utf-8");
    }
    
    /**
     * byte[] 转 String
     * @param data
     * @return null if UnsupportedEncodingException happened
     */
    public static String getStringFromBytes(byte[] data)
    {
        String tempStr = "";
        
        try
        {
            tempStr = new String(data, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LogFileUtil.e(TAG, "getStringFromBytes -> UnsupportedEncodingException", e);
        }
        return tempStr;
    }
    
    public static String getJsonStr(int length, String name, String number)
    {
        Gson gson = new Gson();
        
        ArrayList<PostTestBean> postTestBeans = new ArrayList<PostTestBean>();
        PostTestBean postTestBean;
        for (int i = 0; i < length; i++)
        {
            postTestBean = new PostTestBean();
            postTestBean.setId(i);
            postTestBean.setName(name + "_" + i);
            postTestBean.setNumber(number + "_" + i);
            
            postTestBeans.add(postTestBean);
        }
        
        return gson.toJson(postTestBeans);
    }
}
