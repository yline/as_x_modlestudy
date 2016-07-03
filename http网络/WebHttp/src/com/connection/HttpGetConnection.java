package com.connection;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://192.168.1.102:8080/WebHttp/HttpGetConnection?json="json"
 */
@WebServlet("/HttpGetConnection")
public class HttpGetConnection extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpGetConnection()
    {
        super();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        System.out.println("doGet in");
        doLogRequestHeader(request);
        doLogRequestParameter(request);
        // 这里就可以直接写入json字符串,那边拿到的就是一个json字符串了
        String json = "json";
        response.getWriter().append(json);
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        System.out.println("doPost in");
        doLogRequestHeader(request);
        doLogRequestParameter(request);
        
        // 这里处理返回码
    }
    
    /**
     * 打印请求头,日志
     * @param request
     */
    public void doLogRequestHeader(HttpServletRequest request)
    {
        Enumeration<String> list = request.getHeaderNames();
        String key = "";
        String value = "";
        while (list.hasMoreElements())
        {
            key = list.nextElement();
            value = request.getHeader(key);
            System.out.println("header,key = " + key + ",value = " + value);
        }
    }
    
    /**
     * 打印请求参数,日志
     * @param request
     */
    public void doLogRequestParameter(HttpServletRequest request)
    {
        Map<String, String[]> requestMap = request.getParameterMap();
        Set<String> keySet = requestMap.keySet();
        String[] values = {};
        for (String key : keySet)
        {
            values = requestMap.get(key);
            System.out.println("parameter,key = " + key + ",value = " + getString(values));
        }
    }
    
    /**
     * @param values String[] 数组
     * @return  String 字符串
     */
    public String getString(String[] values)
    {
        StringBuffer buffer = new StringBuffer();
        for (String value : values)
        {
            buffer.append(value);
        }
        
        return buffer.toString();
    }
    
}
