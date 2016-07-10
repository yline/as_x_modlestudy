package com.json.gson.bean;

public class Wifi
{
    private String wifiName;
    
    private String macAddress;
    
    public String getWifiName()
    {
        return wifiName;
    }
    
    public void setWifiName(String wifiName)
    {
        this.wifiName = wifiName;
    }
    
    public String getMacAddress()
    {
        return macAddress;
    }
    
    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }
    
    @Override
    public String toString()
    {
        return "Wifi [wifiName=" + wifiName + ", macAddress=" + macAddress + "]";
    }
}
