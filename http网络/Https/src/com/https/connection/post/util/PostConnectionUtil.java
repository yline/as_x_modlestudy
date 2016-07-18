package com.https.connection.post.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.https.activity.MainApplication;
import com.https.help.util.HttpsHelperUtils;
import com.yline.log.LogFileUtil;

import android.os.Handler;

/**
 * 请求的URL,区分大小写
 * @author YLine
 *
 * 2016年7月5日 下午10:46:01
 */
public class PostConnectionUtil
{
    private static final String TAG = "connection_post";
    
    private static final int HTTPCONNECT_SUCCESS_CODE = 200; // 网络请求,正确返回码
    
    private static final int CONNNECT_TIMEOUT = 5000; // 连接超时时间 ms 
    
    private static final int READ_TIMEOUT = 5000; // 读取超时时间 ms
    
    private static final String CA_CRT_ASSERT = "CA.crt"; // asserts目录下文件名【区分大小写】
    
    private static Handler mHandler = new Handler();
    
    public static void doHttpsLocal(String ip, String projectName, String classStr,
        final PostConnectionCallback callback)
    {
        String httpsUrl = String.format("https://%s:443/%s/%s", ip, projectName, classStr);
        doPostHttpsAsyn(httpsUrl, callback);
    }
    
    /**
     * Post 异步请求
     * @param httpsUrl   URL
     * @param callback  网络请求回调
     */
    private static void doPostHttpsAsyn(final String httpsUrl, final PostConnectionCallback callback)
    {
        LogFileUtil.v(TAG, "doPostHttpsAsyn -> in -> httpsUrl:" + httpsUrl);
        
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                URL url = null;
                HttpsURLConnection httpsURLConnection = null;
                
                BufferedReader bufferedReader = null;
                
                StringBuffer result = new StringBuffer();
                
                try
                {
                    url = new URL(httpsUrl);
                    LogFileUtil.v(TAG, "doPostHttpsAsyn -> new url over");
                    
                    httpsURLConnection = (HttpsURLConnection)url.openConnection();
                    httpsURLConnection.setRequestMethod("POST");
                    initHttpsConnection(httpsURLConnection);
                    
                    /** 请求头,日志 */
                    String requestHeader = HttpsHelperUtils.getHttpsRequestHeader(httpsURLConnection);
                    LogFileUtil.i(TAG, "doPostHttpsAsyn -> initHttpsConnection over requestHeader\n" + requestHeader);
                    
                    /** 设置参数体 */
                    String json = HttpsHelperUtils.getJsonStr(10, "yline", "1378709");
                    initHttpsConnectionBody(httpsURLConnection, json);
                    
                    int responseCode = httpsURLConnection.getResponseCode();
                    LogFileUtil.i(TAG, "doPostHttpsAsyn -> responseCode = " + responseCode);
                    
                    if (HTTPCONNECT_SUCCESS_CODE == responseCode) // 连接成功
                    {
                        bufferedReader =
                            new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), "utf-8"));
                        String line = null;
                        while (null != (line = bufferedReader.readLine())) // 即:有数据时,一直在读取
                        {
                            result.append(line);
                        }
                        
                        /** 响应头,日志 */
                        String responseHeader = HttpsHelperUtils.getHttpsResponseHeader(httpsURLConnection);
                        LogFileUtil.i(TAG, "doPostHttpsAsyn -> responseHeader\n" + responseHeader);
                        
                        handleSuccess(callback, result.toString());
                    }
                    else
                    {
                        LogFileUtil.e(TAG, "doPostHttpsAsyn -> responseCode error");
                        handleError(callback,
                            new Exception("doPostHttpsAsyn -> responseCode error code = " + responseCode));
                    }
                }
                catch (MalformedURLException e)
                {
                    LogFileUtil.e(TAG, "doPostHttpsAsyn -> ProtocolException", e);
                    handleError(callback, e);
                }
                catch (IOException e)
                {
                    LogFileUtil.e(TAG, "doPostHttpsAsyn -> IOException", e);
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
                            LogFileUtil.e(TAG, "doPostHttpsAsyn -> close IOException", e);
                            handleError(callback, e);
                        }
                    }
                    if (null != httpsURLConnection)
                    {
                        httpsURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
    
    /**
     * http 链接  设置
     * RequestHeader : setRequestProperty
     * parameter     : 直接在后面加的参数
     * @param httpsURLConnection 
     */
    private static void initHttpsConnection(HttpsURLConnection httpsURLConnection)
    {
        // 用setRequestProperty方法设置多个自定义的请求头:action，用于后端判断
        httpsURLConnection.setRequestProperty("accept", "*/*");
        httpsURLConnection.setRequestProperty("connection", "Keep-Alive");
        httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpsURLConnection.setRequestProperty("charset", "utf-8");
        httpsURLConnection.setRequestProperty("action", "post");
        
        // 禁用网络缓存
        httpsURLConnection.setUseCaches(false);
        // 定制
        httpsURLConnection.setDoOutput(true); // 设置此方法,允许向服务器输出内容
        
        // HttpURLConnection默认也支持从服务端读取结果流，因此也可省略
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setReadTimeout(READ_TIMEOUT); // 设置读取超时为5秒
        httpsURLConnection.setConnectTimeout(CONNNECT_TIMEOUT); // 设置连接网络超时为5秒
        
        LogFileUtil.v(TAG, "initHttpsConnection -> https set start");
        // https
        try
        {
            httpsURLConnection.setSSLSocketFactory(getSSLContext().getSocketFactory());
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier()
            {
                
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    // 进行证书校验,本地默认 认证通过校验
                    return true;
                }
            });
        }
        catch (Exception e)
        {
            LogFileUtil.e(TAG, "initHttpsConnection -> getSSLContext -> Exception", e);
        }
        LogFileUtil.v(TAG, "initHttpsConnection -> https set end");
    }
    
    /**
     * 证书认证
     * @return SSLContext or exception
     * @throws CertificateException
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLContext getSSLContext()
        throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        // 从asserts目录中获取CA.cer证书的文件流
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream in = MainApplication.getApplication().getAssets().open(CA_CRT_ASSERT); // 区分大小写
        
        // 将该文件流转化为一个证书对象Certificate
        Certificate ca = cf.generateCertificate(in);
        
        // 创建一个默认类型的KeyStore实例，keyStore用于存储着我们信赖的数字证书
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setCertificateEntry("CA", ca);
        
        // 获取一个默认的TrustManagerFactory的实例，并用之前的keyStore初始化它
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keystore);
        
        // 创建一个TLS类型的SSLContext实例，并用之前的TrustManager数组初始化sslContext实例，这样该sslContext实例也会信任CA.cer证书
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        TrustManager[] trustManagers = tmf.getTrustManagers();
        if (null != trustManagers)
        {
            sslContext.init(null, trustManagers, new SecureRandom());
        }
        else
        {
            LogFileUtil.e(MainApplication.TAG, "PostConnectionUtil -> initHttpsConnection trustManagers is null");
        }
        
        return sslContext;
    }
    
    /**
     * 设置请求体
     * @param httpsURLConnection
     * @param json  json字符串
     */
    private static void initHttpsConnectionBody(HttpsURLConnection httpsURLConnection, String json)
    {
        OutputStream outputStream = null;
        try
        {
            outputStream = httpsURLConnection.getOutputStream();
            byte[] bytes = HttpsHelperUtils.getByteFromString(json);
            outputStream.write(bytes);
        }
        catch (IOException e)
        {
            LogFileUtil.e(TAG, "initHttpsConnectionBody -> IOException", e);
        }
        finally
        {
            try
            {
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 回调,抛出异常到主线程
     * @param callback
     * @param e
     */
    private static void handleError(final PostConnectionCallback callback, final Exception e)
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
    private static void handleSuccess(final PostConnectionCallback callback, final String result)
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
    
    public interface PostConnectionCallback
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
