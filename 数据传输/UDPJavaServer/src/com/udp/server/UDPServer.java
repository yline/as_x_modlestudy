package com.udp.server;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 作为 发送端
 * 2016年7月22日 下午11:14:38
 */
public class UDPServer extends JFrame
{
    private static final long serialVersionUID = -5280640205777786533L;
    
    private static final String TAG_JAVA_SEND = "java_send -> ";
    
    public UDPServer()
    {
        initJFrame();
    }
    
    private void initJFrame()
    {
        System.out.println(TAG_JAVA_SEND + "initJFrame start");
        
        // java界面
        this.setVisible(true);
        this.setBounds(100, 100, 300, 100);
        this.setLayout(null);
        JPanel topPanel = getPanel();
        Container con = this.getContentPane();
        con.add(topPanel);
        this.validate();
        this.repaint();
        // setDefaultCloseOperation(EXIT_ON_CLOSE); 设置这一句,关闭对话框就是关闭程序
    }
    
    private JPanel getPanel()
    {
        System.out.println(TAG_JAVA_SEND + "getPanel start");
        
        JPanel topPanel = new JPanel();
        topPanel.setBounds(1, 1, 200, 100);
        topPanel.setBackground(new Color(244, 244, 244));
        topPanel.setLayout(null);
        JButton sendButton = new JButton("发送信息");
        sendButton.setBounds(40, 15, 150, 20);
        topPanel.add(sendButton);
        
        sendButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println(TAG_JAVA_SEND + "getPanel sendMessage");
                // send
                final String msg = "hello world";
                final String serverIp = "";
                int sendPort = 1101;
                sendMessage(msg, serverIp, sendPort);
            }
        });
        return topPanel;
    }
    
    private void sendMessage(String msg, String serverIp, int sendPort)
    {
        DatagramSocket sendSocket = null;
        InetAddress clientAddress = null;
        
        try
        {
            sendSocket = new DatagramSocket(sendPort);
            clientAddress = InetAddress.getByName(serverIp);
        }
        catch (SocketException e1)
        {
            e1.printStackTrace();
        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
        }
        
        byte data[] = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, clientAddress, sendPort);
        try
        {
            sendSocket.send(sendPacket);
            System.out.println(TAG_JAVA_SEND + "send - succenss");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            sendSocket.close();
        }
    }
}
