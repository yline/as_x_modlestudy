package com.json.gson.bean;

import java.util.List;

public class WifiPolicyBean
{
    private int wifiListNum;
    
    private List<Wifi> wifiWhiteList;
    
    public int getWifiListNum()
    {
        return wifiListNum;
    }
    
    public void setWifiListNum(int wifiListNum)
    {
        this.wifiListNum = wifiListNum;
    }
    
    public List<Wifi> getWifiWhiteList()
    {
        return wifiWhiteList;
    }
    
    public void setWifiWhiteList(List<Wifi> wifiWhiteList)
    {
        this.wifiWhiteList = wifiWhiteList;
    }
}
