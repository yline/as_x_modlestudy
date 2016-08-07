package com.json.gson.parse;

import java.util.List;

import com.google.gson.Gson;
import com.json.gson.bean.Wifi;
import com.json.gson.bean.WifiPolicyBean;
import com.yline.log.LogFileUtil;

/**
 * 在一些大工程里面,需要做 非混淆 处理(proguard-project中添加)
 * 仅仅对父类进行该操作也是可以的
 * @author YLine
 *
 * 2016年7月10日 下午9:51:58
 */
public class WifiPolicyParse
{
    private static final String TAG = "WifiPolicyParse";
    
    public static final String JsonStr =
        "{ \"wifiListNum\": \"2\", \"wifiWhiteList\": [ { \"wifiName\": \"w90005368_SVNTes1\"},"
            + " { \"wifiName\": \"w90005368_SVNTest1\", \"macAddress\": \"a1:12:12:12:1A:45\" } ] }";
    
    public void test(String json)
    {
        Gson gson = new Gson();
        WifiPolicyBean bean = gson.fromJson(json, WifiPolicyBean.class);
        
        if (null != bean)
        {
            int number = bean.getWifiListNum();
            LogFileUtil.v(TAG, "wifiListNum = " + number);
            
            List<Wifi> wifis = bean.getWifiWhiteList();
            for (Wifi wifi : wifis)
            {
                LogFileUtil.v(TAG, "wifi = " + wifi.toString());
            }
        }
        else
        {
            LogFileUtil.v(TAG, "bean is null");
        }
    }
}
