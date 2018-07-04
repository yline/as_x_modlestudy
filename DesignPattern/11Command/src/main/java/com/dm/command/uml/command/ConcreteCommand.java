package com.dm.command.uml.command;

import com.dm.command.uml.receiver.Receiver;

public class ConcreteCommand implements ICommand {
    /**
     * 持有一个对接收者对象的引用
     */
    private Receiver mReceiver;

    public ConcreteCommand(Receiver receiver) {
        this.mReceiver = receiver;
    }

    @Override
    public void execute() {
        mReceiver.action();
    }

}
