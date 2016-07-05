package com.connection;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://192.168.1.102:8080/WebHttps/HttpsConnection?json="json"
 * 
 * 这个不是Https的后台,只能作为Http,因此不能成为测试对象
 * Blog:http://blog.csdn.net/iispring/article/details/51615631
 * 作为问题遗留
 */
@WebServlet("/HttpsConnection")
public class HttpsConnection extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpsConnection()
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
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        System.out.println("doPost in");
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }
    
}
