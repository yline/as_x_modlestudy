package com.dm.mediator.colleague;

import com.dm.mediator.mediator.Mediator;

public abstract class Collleague
{
    protected Mediator mediator;
    
    public Collleague(Mediator mediator)
    {
        this.mediator = mediator;
    }
}
