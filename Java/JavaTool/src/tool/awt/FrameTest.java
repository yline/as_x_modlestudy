package tool.awt;

import java.awt.Frame;

public class FrameTest
{
    public static void main(String[] args)
    {
        Frame frame = new Frame("测试窗口");
        
        // 设置窗口的大小、位置
        frame.setBounds(30, 30, 250, 200);
        // 将窗口显示出来(Frame对象默认处于隐藏状态)
        frame.setVisible(true);
    }
}
