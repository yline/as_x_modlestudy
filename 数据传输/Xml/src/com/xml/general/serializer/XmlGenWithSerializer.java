package com.xml.general.serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.xml.activity.MainApplication;
import com.xml.general.GenInfo;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.util.Xml;

public class XmlGenWithSerializer
{
    /**
     * 使用序列化器,生成xml
     */
    public static void generalWithSerializer(Context context, List<GenInfo> genInfos)
    {
        if (null != genInfos)
        {
            LogFileUtil.v(MainApplication.TAG, "generalWithSerializer -> size = " + genInfos.size());
            
            try
            {
                XmlSerializer serializer = Xml.newSerializer();
                File file = new File(context.getFilesDir(), "backupSms2.xml");
                FileOutputStream fos = new FileOutputStream(file);
                //初始化序列号器，指定写入文件的目录、以及文件的编码格式
                serializer.setOutput(fos, "utf-8");
                
                //开始写入数据
                //确定文件开头的格式，以及文件是否独立
                serializer.startDocument("utf-8", true);
                //写入命名空间、名字
                serializer.startTag(null, "smss");
                
                for (GenInfo info : genInfos)
                { //SmsInfo为构成的类，info是类的名称，SmsInfos是一个类组;遇到空类则跳出
                    serializer.startTag(null, "sms");
                    // 增加id 为 <sms id> 中
                    serializer.attribute(null, "id", info.getId() + "");
                    
                    //写入一条单独的字符串 body
                    serializer.startTag(null, "body");
                    serializer.text(info.getBody());
                    serializer.endTag(null, "body");
                    
                    //写入一条单独的字符串 address
                    serializer.startTag(null, "address");
                    serializer.text(info.getAddress());
                    serializer.endTag(null, "address");
                    
                    //写入一条单独的字符串 data
                    serializer.startTag(null, "data");
                    serializer.text(info.getData() + "");
                    serializer.endTag(null, "data");
                    
                    //写入一条单独的字符串 type 
                    serializer.startTag(null, "type");
                    serializer.text(info.getType() + "");
                    serializer.endTag(null, "type");
                    
                    serializer.endTag(null, "sms");
                }
                
                serializer.endTag(null, "smss");
                serializer.endDocument();
                
                LogFileUtil.v(MainApplication.TAG, "generalWithSerializer -> " + fos.toString());
                fos.close();
                
                LogFileUtil.v(MainApplication.TAG, "generalWithSerializer -> 生成xml成功");
            }
            catch (Exception e)
            {
                LogFileUtil.v(MainApplication.TAG, "generalWithSerializer -> 生成xml失败");
            }
        }
    }
}
