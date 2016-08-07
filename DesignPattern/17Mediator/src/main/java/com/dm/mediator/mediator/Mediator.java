package com.dm.mediator.mediator;

import com.dm.mediator.colleague.Collleague;

/** 抽象中介者 */
public abstract class Mediator
{
    /**
     * 同事对象改变时,通知中介者的方法
     * 在同事对象改变时由中介者去通知其他的同时对象
     * @param colleage
     */
    public abstract void changed(Collleague colleage);
}
