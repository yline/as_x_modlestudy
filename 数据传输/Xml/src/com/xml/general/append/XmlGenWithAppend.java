package com.xml.general.append;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.xml.activity.MainApplication;
import com.xml.general.GenInfo;
import com.yline.log.LogFileUtil;

import android.content.Context;

public class XmlGenWithAppend
{
    /**
     * 这一种方法存在一些问题，例如 <> 这些东西会出现bug;而第二种会自动转义
     */
    public static void generalWithAppend(Context context, List<GenInfo> genInfos)
    {
        //假设已经获取到了所有的一组短信， 
        StringBuilder sb = new StringBuilder();
        
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<smss>\n");
        
        if (null != genInfos)
        {
            LogFileUtil.v(MainApplication.TAG, "XmlGenWithAppend -> size = " + genInfos.size());
            
            for (GenInfo info : genInfos)
            {
                sb.append("<sms>");
                
                sb.append("<address>");
                sb.append(info.getAddress());
                sb.append("</address>\n");
                
                sb.append("<body>");
                sb.append(info.getBody());
                sb.append("</body>\n");
                
                sb.append("<data>");
                sb.append(info.getData());
                sb.append("</data>\n");
                
                sb.append("<type>");
                sb.append(info.getType());
                sb.append("</type>");
                
                sb.append("</sms>\n");
            }
            
            sb.append("</smss>\n");
            
            FileOutputStream fos = null;
            ;
            try
            {
                File file = new File(context.getFilesDir(), "backup.xml");
                fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                
                LogFileUtil.v(MainApplication.TAG, "XmlGenWithAppend -> " + sb.toString());
                LogFileUtil.v(MainApplication.TAG, "XmlGenWithAppend -> 生成xml成功");
            }
            catch (FileNotFoundException e1)
            {
                LogFileUtil.e(MainApplication.TAG, "XmlGenWithAppend -> FileNotFoundException", e1);
            }
            catch (IOException e1)
            {
                LogFileUtil.e(MainApplication.TAG, "XmlGenWithAppend -> IOException", e1);
            }
            finally
            {
                if (null != fos)
                {
                    try
                    {
                        fos.close();
                    }
                    catch (IOException e)
                    {
                        LogFileUtil.e(MainApplication.TAG, "XmlGenWithAppend -> IOException", e);
                    }
                }
            }
        }
    }
}
