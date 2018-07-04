package com.dm.command.uml.invoker;

import com.dm.command.uml.command.ICommand;

public class Invoker {
    private ICommand iCommand;

    public Invoker(ICommand command) {
        this.iCommand = command;
    }

    public void action() {
        iCommand.execute();
    }
}
