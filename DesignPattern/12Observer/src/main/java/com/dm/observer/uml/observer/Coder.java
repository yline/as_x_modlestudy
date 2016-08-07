package com.dm.observer.uml.observer;

import java.util.Observable;
import java.util.Observer;

import com.dm.observer.uml.activity.MainApplication;
import com.yline.log.LogFileUtil;

/** 观察者 */
public class Coder implements Observer
{
    private String name;
    
    public Coder(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return "Coder [name=" + name + "]";
    }
    
    @Override
    public void update(Observable observable, Object data)
    {
        LogFileUtil.v(MainApplication.TAG, "To: " + name + ", 内容更新:" + data);
    }
    
}
