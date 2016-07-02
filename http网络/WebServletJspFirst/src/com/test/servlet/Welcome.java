package com.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author YLine
 *
 * 2016年7月1日 下午11:06:17
 */
@WebServlet("/Welcome")
public class Welcome extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Welcome()
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
        String requestParamStr = request.getParameter("select");
        
        System.out.println("requestParamStr = " + requestParamStr);
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.print("Served at:     ");
        printWriter.print(request.getContextPath() + "    ");
        if (requestParamStr.equals("yes"))
        {
            printWriter.append("Welcome!");
        }
        else
        {
            printWriter.append("I don't like you!");
        }
        
        // response.getWriter().append("Served at: ").append(request.getContextPath());
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }
    
}
