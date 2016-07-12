package com.dm.observer.uml.observable;

import java.util.Observable;

/** 通知者 */
public class DevTechFrontier extends Observable
{
    
    /**
     * 通知观察者内容
     * @param content
     */
    public void postNewPublication(String content)
    {
        // 标识状态或者内容发生改变
        setChanged();
        // 通知所有观察者
        notifyObservers(content);
    }
}
