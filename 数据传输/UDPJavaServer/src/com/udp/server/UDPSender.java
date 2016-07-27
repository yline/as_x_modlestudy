package com.udp.server;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.udp.server.helper.UDPHelper;

/**
 * 作为 发送端
 * 2016年7月22日 下午11:14:38
 */
public class UDPSender extends JFrame
{
    private static final long serialVersionUID = -5280640205777786533L;
    
    private static final String TAG_JAVA_SEND = "java_send -> ";
    
    public UDPSender()
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
    
    private JButton sendButton;
    
    private JPanel getPanel()
    {
        System.out.println(TAG_JAVA_SEND + "getPanel start");
        
        JPanel topPanel = new JPanel();
        topPanel.setBounds(1, 1, 200, 100);
        topPanel.setBackground(new Color(244, 244, 244));
        topPanel.setLayout(null);
        
        sendButton = new JButton("发送信息");
        sendButton.setBounds(40, 15, 150, 20);
        topPanel.add(sendButton);
        
        return topPanel;
    }
    
    public void send(final String msg, final String serverIp, final int serverPort)
    {
        sendButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println(TAG_JAVA_SEND + "serverIp = " + serverIp + ",serverPort = " + serverPort);
                
                new UDPHelper().send(msg.getBytes(), serverIp, serverPort);
            }
        });
    }
}
